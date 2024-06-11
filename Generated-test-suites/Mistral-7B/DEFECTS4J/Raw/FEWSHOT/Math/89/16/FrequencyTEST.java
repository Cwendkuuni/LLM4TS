package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency frequency = new Frequency();
        assertEquals("", frequency.toString());
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue("A");
        frequency.addValue("B");
        frequency.addValue("A");
        frequency.addValue("A");
        frequency.addValue("B");
        frequency.addValue("C");
        frequency.addValue("B");
        frequency.addValue("B");
        frequency.addValue("A");

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \nA \t 5 \t 0.453235294117647 \t 0.453235294117647\nB \t 6 \t 0.5192307692307693 \t 0.9724660631480113\nC \t 1 \t 0.0865079759183607 \t 1.059113063156422\n", frequency.toString());
    }

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.2 \t 0.2\n2 \t 2 \t 0.2 \t 0.4\n3 \t 2 \t 0.2 \t 0.6\n4 \t 3 \t 0.3 \t 0.9\n", frequency.toString());
    }

    @Test
    public void testFrequencyAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 3 \t 0.06153846153846154 \t 0.06153846153846154\n2 \t 2 \t 0.0425531914893617 \t 0.06399165255277226\n3 \t 4 \t 0.0850746268656718 \t 0.1490662833224447\n4 \t 6 \t 0.1215151515151515 \t 0.2705814328776162\n5 \t 5 \t 0.1 \t 0.3711764705882677\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue("A");
        frequency.addValue(1);
    }

    @Test
    public void testFrequencyGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);

        long sumFreq = frequency.getSumFreq();
        assertEquals(12, sumFreq);
    }

    @Test
    public void testFrequencyGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        long count1 = frequency.getCount(1);
        assertEquals(3, count1);

        long count2 = frequency.getCount(2);
        assertEquals(2, count2);

        long count3 = frequency.getCount(3);
        assertEquals(4, count3);

        long count4 = frequency.getCount(4);
        assertEquals(6, count4);

        long count5 = frequency.getCount(5);
        assertEquals(5, count5);

        long countA = frequency.getCount("A");
        assertEquals(0, countA);
    }

    @Test
    public void testFrequencyGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        double pct1 = frequency.getPct(1);
        assertEquals(0.06153846153846154, pct1, 0.0001);

        double pct2 = frequency.getPct(2);
        assertEquals(0.0425531914893617, pct2, 0.0001);

        double pct3 = frequency.getPct(3);
        assertEquals(0.0850746268656718, pct3, 0.0001);

        double pct4 = frequency.getPct(4);
        assertEquals(0.1215151515151515, pct4, 0.0001);

        double pct5 = frequency.getPct(5);
        assertEquals(0.1, pct5, 0.0001);

        double pctA = frequency.getPct("A");
        assertEquals(0.0, pctA, 0.0001);
    }

    @Test
    public void testFrequencyGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(3, cumFreq1);

        long cumFreq2 = frequency.getCumFreq(2);
        assertEquals(5, cumFreq2);

        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(9, cumFreq3);

        long cumFreq4 = frequency.getCumFreq(4);
        assertEquals(12, cumFreq4);

        long cumFreq5 = frequency.getCumFreq(5);
        assertEquals(17, cumFreq5);

        long cumFreqA = frequency.getCumFreq("A");
        assertEquals(0, cumFreqA);
    }

    @Test
    public void testFrequencyGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.06153846153846154, cumPct1, 0.0001);

        double cumPct2 = frequency.getCumPct(2);
        assertEquals(0.08394433894433895, cumPct2, 0.0001);

        double cumPct3 = frequency.getCumPct(3);
        assertEquals(0.13619230769230769, cumPct3, 0.0001);

        double cumPct4 = frequency.getCumPct(4);
        assertEquals(0.18793650793650793, cumPct4, 0.0001);

        double cumPct5 = frequency.getCumPct(5);
        assertEquals(0.2396551686567184, cumPct5, 0.0001);

        double cumPctA = frequency.getCumPct("A");
        assertEquals(0.0, cumPctA, 0.0001);
    }

    @Test
    public void testFrequencyClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();

        assertEquals("", frequency.toString());
    }
}