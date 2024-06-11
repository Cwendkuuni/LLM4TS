package org.apache.commons.math.stat.test;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Comparator;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testDefaultConstructor() {
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1.0, frequency.getPct(1), 0.01);
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.01);

        frequency.addValue(2);
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(1.5, frequency.getCumPct(2), 0.01);

        Assert.assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);

        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.01);

        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(2.0, frequency.getCumPct(2), 0.01);
        Assert.assertEquals(2.0, frequency.getCumPct(2), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithUncomparableValues() {
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithUncomparableValuesDifferentTypes() {
        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test
    public void testClear() {
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Iterator iterator = frequency.valuesIterator();
        Assert.assertEquals(1, iterator.next().longValue());
        Assert.assertEquals(1, iterator.next().longValue());
        Assert.assertEquals(1, iterator.next().longValue());
        Assert.assertEquals(2, iterator.next().longValue());
        Assert.assertEquals(2, iterator.next().longValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCountNegativeValue() {
        frequency.addValue(1);
        Assert.assertEquals(0, frequency.getCount(-1));
    }

    @Test
    public void testGetCountNullValue() {
        frequency.addValue(1);
        Assert.assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testGetPctNegativeValue() {
        frequency.addValue(1);
        Assert.assertEquals(Double.NaN, frequency.getPct(-1), 0.0);
    }

    @Test
    public void testGetPctNullValue() {
        frequency.addValue(1);
        Assert.assertEquals(Double.NaN, frequency.getPct(null), 0.0);
    }

    @Test
    public void testGetCumFreqNegativeValue() {
        frequency.addValue(1);
        frequency.addValue(1);
        Assert.assertEquals(0, frequency.getCumFreq(-1));
    }

    @Test
    public void testGetCumFreqNullValue() {
        frequency.addValue(1);
        Assert.assertEquals(0, frequency.getCumFreq(null));
    }

    @Test
    public void testGetCumPctNegativeValue() {
        frequency.addValue(1);
        Assert.assertEquals(Double.NaN, frequency.getCumPct(-1), 0.0);
    }

    @Test
    public void testGetCumPctNullValue() {
        frequency.addValue(1);
        Assert.assertEquals(Double.NaN, frequency.getCumPct(null), 0.0);
    }

    @Test
    public void testGetSumFreqNegativeValues() {
        frequency.addValue(1);
        frequency.addValue(-1);
        Assert.assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testGetSumFreqNullValue() {
        frequency.addValue(1);
        frequency.addValue(null);
        Assert.assertEquals(1, frequency.getSumFreq());
    }
}