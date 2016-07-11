package com.chuongdang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Chuong Dang on 7/11/2016.
 */
public class SimpleKNN {
    public static void main(String... args) {
        if (args.length != 3) {
            System.err.println("com.example.KNNToolMapred <train_dir>  <k> <input_pattern> ");
            System.exit(2);
        }
        long postExecTime = System.currentTimeMillis();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int K = Integer.parseInt(args[1]);
        String[] inputPattern = args[2].split(cvsSplitBy);
        Map<String, Double> store = new HashMap<String, Double>();
        Double[] parsedInputPattern = new Double[inputPattern.length];

        for(int i=0;i<inputPattern.length;i++) {
            parsedInputPattern[i] = Double.parseDouble(inputPattern[i]);
        }
        try {

            br = new BufferedReader(new FileReader(args[0]));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);

                Double[] parsedRow = new Double[row.length - 1];

                for(int i=0;i<parsedRow.length;i++) {
                    parsedRow[i] = Objects.equals(row[i + 1], "?") ? 0: Double.parseDouble(row[i+1]);
                }
                store.put(row[0],getDoubleDistance(parsedInputPattern, parsedRow));
            }
            Map<String, Double> sortedLabelDistance = MapUtil.sortByValue(store);

            Iterator<String> labels = sortedLabelDistance.keySet().iterator();
            List<String> kClosestLabels = new LinkedList<>();
            for(int i = 0; i<K; i++) {
                String lb = labels.next();
                kClosestLabels.add(lb);
                System.out.println("K["+i+"]: "+lb);
            }
            System.out.println("Time elapsed: "+(System.currentTimeMillis()-postExecTime) +" (ms)");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Time elapsed: "+(System.currentTimeMillis()-postExecTime) +" (ms)");

        }
    }
    private static double getDoubleDistance(Object[] p1, Object[] p2) {
        double dist = 0.0;

        for (int i = 0; i < p1.length; i++) {
            double difference = (Double) p1[i] - (Double) p2[i];

            dist += difference * difference;
        }
        return Math.sqrt(dist);
    }
    public static  class MapUtil {
        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
            List<Map.Entry<K, V>> list =
                    new LinkedList<Map.Entry<K, V>>( map.entrySet() );
            Collections.sort( list, new Comparator<Map.Entry<K, V>>()
            {
                public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
                {
                    return (o1.getValue()).compareTo( o2.getValue() );
                }
            } );

            Map<K, V> result = new LinkedHashMap<K, V>();
            for (Map.Entry<K, V> entry : list)
            {
                result.put( entry.getKey(), entry.getValue() );
            }
            return result;
        }
    }
}
