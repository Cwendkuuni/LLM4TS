package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();

        // test addValue methods
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        // test getSumFreq method
        long sumFreq = frequency.getSumFreq();
        assertEquals(6, sumFreq);

        // test getCount method
        long count1 = frequency.getCount(1);
        assertEquals(2, count1);
        long count2 = frequency.getCount(2);
        assertEquals(2, count2);
        long count3 = frequency.getCount(3);
        assertEquals(1, count3);

        // test getPct method
        double pct1 = frequency.getPct(1);
        assertEquals(1.0 / 3.0, pct1, 0.001);
        double pct2 = frequency.getPct(2);
        assertEquals(1.0 / 3.0, pct2, 0.001);
        double pct3 = frequency.getPct(3);
        assertEquals(1.0 / 6.0, pct3, 0.001);

        // test getCumFreq method
        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(2, cumFreq1);
        long cumFreq2 = frequency.getCumFreq(2);
        assertEquals(4, cumFreq2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(5, cumFreq3);

        // test getCumPct method
        double cumPct1 = frequency.getCumPct(1);
        assertEquals(1.0 / 3.0, cumPct1, 0.001);
        double cumPct2 = frequency.getCumPct(2);
        assertEquals(2.0 / 3.0, cumPct2, 0.001);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(1.0, cumPct3, 0.001);

        // test clear method
        frequency.clear();
        sumFreq = frequency.getSumFreq();
        assertEquals(0, sumFreq);

        // test valuesIterator method
        Iterator iterator = frequency.valuesIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);

        // test addValue methods
        frequency.addValue(3);
        frequency.addValue(2);
        frequency.addValue(1);

        // test getSumFreq method
        sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);

        // test getCount method
        count1 = frequency.getCount(1);
        assertEquals(1, count1);
        count2 = frequency.getCount(2);
        assertEquals(1, count2);
        count3 = frequency.getCount(3);
        assertEquals(1, count3);

        // test getPct method
        pct1 = frequency.getPct(1);
        assertEquals(1.0 / 3.0, pct1, 0.001);
        pct2 = frequency.getPct(2);
        assertEquals(1.0 / 3.0, pct2, 0.001);
        pct3 = frequency.getPct(3);
        assertEquals(1.0 / 3.0, pct3, 0.001);

        // test getCumFreq method
        cumFreq1 = frequency.getCumFreq(1);
        assertEquals(1, cumFreq1);
        cumFreq2 = frequency.getCumFreq(2);
        assertEquals(1, cumFreq2);
        cumFreq3 = frequency.getCumFreq(3);
        assertEquals(3, cumFreq3);

        // test getCumPct method
        cumPct1 = frequency.getCumPct(1);
        assertEquals(1.0 / 3.0, cumPct1, 0.001);
        cumPct2 = frequency.getCumPct(2);
        assertEquals(1.0 / 3.0, cumPct2, 0.001);
        cumPct3 = frequency.getCumPct(3);
        assertEquals(1.0, cumPct3, 0.001);

        // test clear method
        frequency.clear();
        sumFreq = frequency.getSumFreq();
        assertEquals(0, sumFreq);

        // test valuesIterator method
        iterator = frequency.valuesIterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("not a number");
    }

    @Test(expected = ClassCastException.class)
    public void testAddValueWithNonComparableType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }
}