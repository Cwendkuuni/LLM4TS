package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        TreeMap treeMap = new TreeMap();
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        TreeMap treeMap = new TreeMap(comparator);
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test
    public void testAddValueWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        TreeMap treeMap = new TreeMap();
        treeMap.put(1L, 1L);
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test
    public void testAddValueWithLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        TreeMap treeMap = new TreeMap();
        treeMap.put(2L, 1L);
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test
    public void testAddValueWithIntegerString() {
        Frequency frequency = new Frequency();
        frequency.addValue("1");
        TreeMap treeMap = new TreeMap();
        treeMap.put(1L, 1L);
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        TreeMap treeMap = new TreeMap();
        treeMap.put(Character.valueOf('a'), 1L);
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        TreeMap treeMap = new TreeMap();
        assertEquals(treeMap, frequency.freqTable);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);
    }

    @Test
    public void testGetCountWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountWithLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        long count = frequency.getCount(2L);
        assertEquals(1, count);
    }

    @Test
    public void testGetCountWithChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    @Test
    public void testGetCountWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(new Object());
        assertEquals(0, count);
    }

    @Test
    public void testGetPctWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double pct = frequency.getPct(1);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    @Test
    public void testGetPctWithLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        double pct = frequency.getPct(2L);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    @Test
    public void testGetPctWithChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        double pct = frequency.getPct('a');
        assertEquals(0.5, pct, 0.0000001);
    }

    @Test
    public void testGetPctWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double pct = frequency.getPct(new Object());
        assertEquals(0, pct, 0.0000001);
    }

    @Test
    public void testGetCumFreqWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
    }

    @Test
    public void testGetCumFreqWithLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        long cumFreq = frequency.getCumFreq(2L);
        assertEquals(2, cumFreq);
    }

    @Test
    public void testGetCumFreqWithChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        long cumFreq = frequency.getCumFreq('a');
        assertEquals(2, cumFreq);
    }

    @Test
    public void testGetCumFreqWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(new Object());
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumPctWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
    }

    @Test
    public void testGetCumPctWithLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        double cumPct = frequency.getCumPct(2L);
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
    }

    @Test
    public void testGetCumPctWithChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        double cumPct = frequency.getCumPct('a');
        assertEquals(0.5, cumPct, 0.0000001);
    }

    @Test
    public void testGetCumPctWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(new Object());
        assertEquals(0, cumPct, 0.0000001);
    }
}