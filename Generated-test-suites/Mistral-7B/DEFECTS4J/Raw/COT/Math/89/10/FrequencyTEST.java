package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {
    
    

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency.freqTable);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        Assert.assertNotNull(frequency.freqTable);
        Assert.assertEquals(comparator, frequency.freqTable.comparator());
    }

    @Test
    public void testAddValueWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("not comparable");
    }

    @Test
    public void testAddValueWithChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testAddValueWithSameValueMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        Assert.assertEquals(2, frequency.getCount(1));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator iterator = frequency.valuesIterator();
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
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCountWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountWithNonExistentValue() {
        Frequency frequency = new Frequency();
        Assert.assertEquals(0, frequency.getCount("non-existent"));
    }

    @Test
    public void testGetPctWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
    }

    @Test
    public void testGetPctWithNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.0, frequency.getPct("non-existent"), 0.0001);
    }

    @Test
    public void testGetCumFreqWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(2));
        Assert.assertEquals(3, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumFreqWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("non-comparable");
        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(0, frequency.getCumFreq("non-comparable"));
    }

    @Test
    public void testGetCumPctWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
        Assert.assertEquals(0.6666666666666666, frequency.getCumPct(2), 0.0001);
    }

    @Test
    public void testGetCumPctWithNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
        Assert.assertEquals(0.0, frequency.getCumPct("non-existent"), 0.0001);
    }

    
}