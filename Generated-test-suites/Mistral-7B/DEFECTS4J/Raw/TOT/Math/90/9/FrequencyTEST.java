package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Tests the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(0.6666666666666666, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.6666666666666666, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(2), 0.001);

        Assert.assertEquals(1, frequency.valuesIterator().next().longValue());
        Assert.assertEquals(1, frequency.toString().split("\n")[0].split("\t")[0].hashCode());
    }

    /**
     * Test constructor with Comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };

        Frequency frequency = new Frequency(comparator);
        frequency.addValue(Character.valueOf('A'));
        frequency.addValue(Character.valueOf('B'));
        frequency.addValue(Character.valueOf('A'));

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(Character.valueOf('A')));
        Assert.assertEquals(1, frequency.getCount(Character.valueOf('B')));
        Assert.assertEquals(0.6666666666666666, frequency.getPct(Character.valueOf('A')), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(Character.valueOf('B')), 0.001);
        Assert.assertEquals(0.6666666666666666, frequency.getCumPct(Character.valueOf('A')), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(Character.valueOf('B')), 0.001);

        Assert.assertEquals(Character.valueOf('A'), frequency.valuesIterator().next());
        Assert.assertEquals(Character.valueOf('A').hashCode(), frequency.toString().split("\n")[0].split("\t")[0].hashCode());
    }

    /**
     * Test addValue with different data types.
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));

        frequency.addValue('A');
        frequency.addValue('A');

        Assert.assertEquals(5, frequency.getSumFreq());
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getCount('A'));

        frequency.addValue(1.0);
        frequency.addValue(1.0);

        Assert.assertEquals(7, frequency.getSumFreq());
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(1.0));

        frequency.addValue((long) 12345);
        frequency.addValue((long) 12345);

        Assert.assertEquals(9, festival.getCount(12345));
    }

    /**
     * Test addValue with IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIllegalArgumentException() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        frequency.addValue(new Object());
    }

    /**
     * Test getValueIterator.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue(1.0);
        frequency.addValue(1.0);
        frequency.addValue((long) 12345);
        frequency.addValue((long) 12345);

        Assert.assertEquals(9, frequency.valuesIterator().size());
    }

    /**
     * Test getSumFreq, getCount, getPct, getCumFreq and getCumPct with empty frequency.
     */
    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();

        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertEquals(0, frequency.getCount(1));
        Assert.assertEquals(0.0, frequency.getPct(1), 0.0);
        Assert.assertEquals(0.0, frequency.getCumPct(1), 0.0);
    }

    /**
     * Test getSumFreq, getCount, getPct, getCumFreq and getCumPct with different values.
     */
    @Test
    public void testGetSumFreqGetCountGetPctGetCumFreqGetCumPctWithDifferentValues() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue(1.0);
        frequency.addValue(1.0);
        frequency.addValue((long) 12345);
        frequency.addValue((long) 12345);

        Assert.assertEquals(9, frequency.getSumFreq());
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getCount('A'));
        Assert.assertEquals(2, frequency.getCount(1.0));
        Assert.assertEquals(2, frequency.getCount(12345));

        Assert.assertEquals(0.4444444444444444, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.2222222222222222, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.0, frequency.getPct('A'), 0.0);
        Assert.assertEquals(0.0, frequency.getPct(1.0), 0.0);
        Assert.assertEquals(0.2222222222222222, frequency.getPct(12345), 0.001);

        Assert.assertEquals(0.0, frequency.getCumPct(1), 0.0);
        Assert.assertEquals(0.2222222222222222, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(0.2222222222222222, frequency.getCumPct('A'), 0.001);
        Assert.assertEquals(0.4444444444444444, frequency.getCumPct(1.0), 0.001);
        Assert.assertEquals(0.6666666666666666, frequency.getCumPct(12345), 0.001);

        Assert.assertEquals(1, frequency.valuesIterator().next().longValue());
        Assert.assertEquals(1.0, frequency.toString().split("\n")[0].split("\t")[1], 0.0);
    }
}