package org.apache.commons.math.stat.test;

import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("Default constructor should initialize an empty TreeMap", 0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator<Comparable<?>> comparator = new Comparator<Comparable<?>>() {
            @Override
            public int compare(Comparable o1, Comparable o2) {
                return o1.compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        Assert.assertEquals("Comparator constructor should initialize an empty TreeMap", 0, frequency.getSumFreq());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals("AddValue(int) should increment frequency count by 1", 1, frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals("AddValue(char) should convert to Character and increment frequency count by 1", 1, frequency.getCount('a'));
    }

    @Test
    public void testAddValueString() {
        Frequency frequency = new Frequency();
        frequency.addValue("hello");
        Assert.assertEquals("AddValue(String) should be treated as Object and not comparable, so no change in frequency count", 0, frequency.getCount("hello"));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        Assert.assertEquals("Clear should reset the frequency count to 0", 0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Comparable<?>> iterator = frequency.valuesIterator();
        Assert.assertTrue("Values iterator should return the values in the order they were added", iterator.next().compareTo(1) < 0 || iterator.next().compareTo(2) < 0);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals("GetSumFreq should return the sum of all frequencies", 2, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        Assert.assertEquals("GetCount(int) should return the frequency count for the given integer value", 2, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        Assert.assertEquals("GetCount(long) should return the frequency count for the given long value", 2, frequency.getCount(1L));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        Assert.assertEquals("GetCount(char) should return the frequency count for the given char value", 2, frequency.getCount('a'));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals("GetPct(int) should return the percentage of values equal to the given integer value", 0.5, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        Assert.assertEquals("GetPct(long) should return the percentage of values equal to the given long value", 0.5, frequency.getPct(1L), 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("GetPct(char) should return the percentage of values equal to the given char value", 0.5, frequency.getPct('a'), 0.001);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals("GetCumFreq(int) should return the cumulative frequency of values less than or equal to the given integer value", 1, frequency.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        Assert.assertEquals("GetCumFreq(long) should return the cumulative frequency of values less than or equal to the given long value", 1, frequency.getCumFreq(1L));
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("GetCumFreq(char) should return the cumulative frequency of values less than or equal to the given char value", 1, frequency.getCumFreq('a'));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals("GetCumPct(int) should return the cumulative percentage of values less than or equal to the given integer value", 0.5, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        Assert.assertEquals("GetCumPct(long) should return the cumulative percentage of values less than or equal to the given long value", 0.5, frequency.getCumPct(1L), 0.001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("GetCumPct(char) should return the cumulative percentage of values less than or equal to the given char value", 0.5, frequency.getCumPct('a'), 0.001);
    }
}