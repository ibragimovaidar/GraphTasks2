package com.company.graph2;

import java.util.*;

public class GraphTasks2Solution implements GraphTasks2 {
    @Override
    public HashMap<Integer, Integer> dijkstraSearch(int[][] adjacencyMatrix, int startIndex) {

        HashMap<Integer, Integer> markers = new HashMap<>();

        boolean [] isUsed = new boolean[adjacencyMatrix.length];
        isUsed[startIndex] = true;


        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (i == startIndex) {
                markers.put(i, 0);
            } else {
                markers.put(i, Integer.MAX_VALUE);
            }
        }

        int cursor = startIndex;
        while (containsFalse(isUsed)){
            int [] edges = adjacencyMatrix[cursor];

            // обновление меток
            for (int i = 0; i < edges.length; i++) {
                if (edges[i] > 0 && edges[i]+markers.get(cursor) < markers.get(i) && !isUsed[i]) {
                    markers.replace(i, edges[i]+markers.get(cursor));
                }
            }

            // выбор наименьшей из неиспользованных меток
            int minValueOfMarker = Integer.MAX_VALUE;


            for (int i = 0; i < markers.size(); i++) {
                if (markers.get(i) < minValueOfMarker && !isUsed[i]){
                    minValueOfMarker = markers.get(i);
                    cursor = i;
                }
            }

            isUsed[cursor] = true;
        }

        return markers;
    }
    private boolean containsFalse(boolean [] array){
        for (boolean b : array){
            if (!b){
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer primaAlgorithm(int[][] adjacencyMatrix) {
        ArrayList<Integer> usedVertexes = new ArrayList<>();
        ArrayList<Integer> unusedVertexes = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            unusedVertexes.add(i);
        }

        int weight = 0;
        Integer cursor = unusedVertexes.get(0);

        while (!unusedVertexes.isEmpty()) {
            unusedVertexes.remove(cursor);
            usedVertexes.add(cursor);

            Integer minWeight = Integer.MAX_VALUE;

            for (int i: usedVertexes) {
                for (int j = 0; j < adjacencyMatrix.length; j++) {
                    if (adjacencyMatrix[i][j] > 0 && adjacencyMatrix[i][j] < minWeight && !usedVertexes.contains(j)){
                        cursor = j;
                        minWeight = adjacencyMatrix[i][j];
                    }
                }
            }

            if (!unusedVertexes.isEmpty()) {
                weight += minWeight;
            }

        }

        return weight;
    }

    class Edge implements Comparable<Edge> {
        public Integer A;
        public Integer B;
        public Integer weight;

        public Edge(Integer a, Integer b, Integer weight) {
            A = a;
            B = b;
            this.weight = weight;
        }

        public HashSet<Integer> getVertexes(){
            return new HashSet<>(Arrays.asList(A,B));
        }


        @Override
        public String toString() {
            return "{"+ A + "-" + B + "} " + weight;
        }

        @Override
        public int compareTo(Edge o) {
            return weight.compareTo(o.weight);
        }
    }


    @Override
    public Integer kraskalAlgorithm(int[][] adjacencyMatrix) {
        int weight = 0;

        ArrayList<Integer> listOfAllVertexes = getListOfAllVertexes(adjacencyMatrix.length);

        ArrayList<Edge> edges = new ArrayList<>();
        // парсинг ребер из матрицы смежности
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i+1; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] > 0){
                    edges.add(new Edge(i, j, adjacencyMatrix[i][j]));
                }
            }
        }

        Collections.sort(edges);

        ArrayList<HashSet<Integer>> partsOfGraph = new ArrayList<>();
        weight+= edges.get(0).weight;
        partsOfGraph.add(edges.remove(0).getVertexes());

        while (!partsOfGraph.get(0).contains(listOfAllVertexes) && !edges.isEmpty()){
            Edge cursor = edges.remove(0);
            // если добавление ребра приведет к появлению цикла в остове
            if (partsOfGraph.get(0).contains(cursor.A) && partsOfGraph.get(0).contains(cursor.B)){
                continue;
            }
            // если добавление ребра не приведет к появлению цикла и ребро инцидентно вершине из остова
            if (partsOfGraph.get(0).contains(cursor.A) || partsOfGraph.get(0).contains(cursor.B)){
                partsOfGraph.get(0).add(cursor.A);
                partsOfGraph.get(0).add(cursor.B);
                weight+=cursor.weight;
                continue;
            }
            // если ребро не инцидентно ни одной из вершин остова
            partsOfGraph.add(cursor.getVertexes());

            // объединение подграфов
            int startSize = partsOfGraph.size();
            int endSize = -1;

            while (startSize != endSize){
                startSize = partsOfGraph.size();
                ArrayList<Integer> toMerge = new ArrayList<>();

                for (int i = 0; i < partsOfGraph.size(); i++) {
                    for (int j = i+1; j < partsOfGraph.size(); j++) {
                        if (containsAnyVertexOf(partsOfGraph.get(i), partsOfGraph.get(j))){
                            toMerge.add(i);
                            toMerge.add(j);
                            break;
                        }
                    }
                    if (!toMerge.isEmpty()){
                        partsOfGraph.get(toMerge.get(0)).addAll(partsOfGraph.remove(toMerge.get(1).intValue()));
                    }
                }
                endSize = partsOfGraph.size();
            }

            weight+=cursor.weight;
        }

        return weight;
    }

    private boolean containsAnyVertexOf(HashSet<Integer> list1, HashSet<Integer> list2){
        for (int i: list2) {
            if (list1.contains(i)){
                return true;
            }
        }
        return false;
    }

    private ArrayList<Integer> getListOfAllVertexes(int n){
        ArrayList<Integer> listOfAllVertexes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            listOfAllVertexes.add(i);
        }
        return listOfAllVertexes;
    }
}
