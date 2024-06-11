package org.apache.commons.math.stat;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(4, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(0, frequency.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.42857142857142854, frequency.getPct(1), 0.00001);
        Assert.assertEquals(0.2857142857142857, frequency.getPct(2), 0.00001);
        Assert.assertEquals(0, frequency.getPct(3));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(2));
        Assert.assertEquals(4, frequency.getCumFreq(3));
        Assert.assertEquals(0, frequency.getCumFreq(0));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.25, frequency.getCumPct(1), 0.00001);
        Assert.assertEquals(0.5, frequency.getCumPct(1), 0.00001);
        Assert.assertEquals(0.5, frequency.getCumPct(2), 0.00001);
        Assert.assertEquals(1, frequency.getCumPct(3));
        Assert.assertEquals(0, frequency.getCumPct(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue(1);
        frequency.addValue(2.0);
    }
}