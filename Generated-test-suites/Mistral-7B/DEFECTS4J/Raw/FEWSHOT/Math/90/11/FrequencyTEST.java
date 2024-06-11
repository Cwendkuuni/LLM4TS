package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        // Test default constructor
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        // Test getSumFreq
        long sumFreq = frequency.getSumFreq();
        assertEquals(15, sumFreq);

        // Test getCount
        long count1 = frequency.getCount(1);
        assertEquals(3, count1);
        long count2 = frequency.getCount(2);
        assertEquals(3, count2);
        long count3 = frequency.getCount(3);
        assertEquals(4, count3);
        long count5 = frequency.getCount(5);
        assertEquals(5, count5);
        long countNull = frequency.getCount(null);
        assertEquals(0, countNull);

        // Test getPct
        double pct1 = frequency.getPct(1);
        assertEquals(0.2, pct1, 0.001);
        double pct2 = frequency.getPct(2);
        assertEquals(0.2, pct2, 0.001);
        double pct3 = frequency.getPct(3);
        assertEquals(0.26666666666666664, pct3, 0.0000001);
        double pct5 = frequency.getPct(5);
        assertEquals(0.3333333333333333, pct5, 0.0000001);
        double pctNull = frequency.getPct(null);
        assertEquals(Double.NaN, pctNull);

        // Test getCumFreq
        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(3, cumFreq1);
        long cumFreq2 = frequency.getCumFreq(2);
        assertEquals(6, cumFreq2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(10, cumFreq3);
        long cumFreq5 = frequency.getCumFreq(5);
        assertEquals(12, cumFreq5);
        long cumFreqNull = frequency.getCumFreq(null);
        assertEquals(0, cumFreqNull);

        // Test getCumPct
        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.2, cumPct1, 0.001);
        double cumPct2 = frequency.getCumPct(2);
        assertEquals(0.4, cumPct2, 0.001);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(0.5333333333333333, cumPct3, 0.0000001);
        double cumPct5 = frequency.getCumPct(5);
        assertEquals(0.8, cumPct5, 0.001);
        double cumPctNull = frequency.getCumPct(null);
        assertEquals(0.0, cumPctNull);
    }

    @Test
    public void testFrequencyComparator() {
        Frequency frequency = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });
        // Test Frequency with Comparator constructor
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(3);
        frequency.addValue(1);

        // Test getSumFreq
        long sumFreq = frequency.getSumFreq();
        assertEquals(6, sumFreq);

        // Test getCount
        long count1 = frequency.getCount(1);
        assertEquals(3, count1);
        long count2 = frequency.getCount(2);
        assertEquals(2, count2);
        long count3 = frequency.getCount(3);
        assertEquals(1, count3);
        long countNull = frequency.getCount(null);
        assertEquals(0, countNull);

        // Test getPct
        double pct1 = frequency.getPct(1);
        assertEquals(0.5, pct1, 0.001);
        double pct2 = frequency.getPct(2);
        assertEquals(0.3333333333333333, pct2, 0.0000001);
        double pct3 = frequency.getPct(3);
        assertEquals(0.16666666666666668, pct3, 0.0000001);
        double pctNull = frequency.getPct(null);
        assertEquals(Double.NaN, pctNull);

        // Test getCumFreq
        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(3, cumFreq1);
        long cumFreq2 = frequency.getCumFreq(2);
        assertEquals(4, cumFreq2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(5, cumFreq3);
        long cumFreqNull = frequency.getCumFreq(null);
        assertEquals(0, cumFreqNull);

        // Test getCumPct
        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.5, cumPct1, 0.001);
        double cumPct2 = frequency.getCumPct(2);
        assertEquals(0.6, cumPct2, 0.001);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(0.75, cumPct3, 0.001);
        double cumPctNull = frequency.getCumPct(null);
        assertEquals(0.0, cumPctNull);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        // Test toString with default constructor
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        String output = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n" +
                "1 \t 3 \t 0.2 \t 0.2 \t\n" +
                "2 \t 3 \t 0.2 \t 0.4 \t\n" +
                "3 \t 4 \t 0.26666666666666664 \t 0.5333333333333333 \t\n" +
                "5 \t 5 \t 0.3333333333333333 \t 0.8 \t\n";
        assertEquals(expected, output);

        // Test toString with Comparator constructor
        Frequency frequencyComparator = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });
        frequencyComparator.addValue(2);
        frequencyComparator.addValue(1);
        frequencyComparator.addValue(3);
        frequencyComparator.addValue(1);
        frequencyComparator.addValue(3);
        frequencyComparator.addValue(1);
        String outputComparator = frequencyComparator.toString();
        String expectedComparator = "Value \t Freq. \t Pct. \t Cum Pct. \n" +
                "1 \t 3 \t 0.5 \t 0.5 \t\n" +
                "2 \t 2 \t 0.3333333333333333 \t 0.6 \t\n" +
                "3 \t 1 \t 0.16666666666666668 \t 0.75 \t\n";
        assertEquals(expectedComparator, outputComparator);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        // Test clear method
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }
}