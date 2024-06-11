package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test method for {@link Frequency#Frequency()}.
     */
    @Test
    public void testFrequencyConstructor() {
        Frequency freq = new Frequency();
        Assert.assertNotNull(freq);
    }

    /**
     * Test method for {@link Frequency#Frequency(Comparator)}.
     */
    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency freq = new Frequency(comparator);
        Assert.assertNotNull(freq);
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     */
    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        long count = freq.getCount("hello");
        Assert.assertEquals(2, count);

        count = freq.getCount("world");
        Assert.assertEquals(1, count);

        long total = freq.getSumFreq();
        Assert.assertEquals(3, total);
    }

    /**
     * Test method for {@link Frequency#addValue(int)}.
     */
    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        long count = freq.getCount(1);
        Assert.assertEquals(2, count);

        count = freq.getCount(2);
        Assert.assertEquals(1, count);

        long total = freq.getSumFreq();
        Assert.assertEquals(3, total);
    }

    /**
     * Test method for {@link Frequency#addValue(char)}.
     */
    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');

        long count = freq.getCount('a');
        Assert.assertEquals(2, count);

        count = freq.getCount('b');
        Assert.assertEquals(1, count);

        long total = freq.getSumFreq();
        Assert.assertEquals(3, total);
    }

    /**
     * Test method for {@link Frequency#addValue(long)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueLongIllegalArgumentException() {
        Frequency freq = new Frequency();
        freq.addValue(123456789012345L);
    }

    /**
     * Test method for {@link Frequency#clear()}.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.clear();

        long count = freq.getCount("hello");
        Assert.assertEquals(0, count);

        count = freq.getCount("world");
        Assert.assertEquals(0, count);

        long total = freq.getSumFreq();
        Assert.assertEquals(0, total);
    }

    /**
     * Test method for {@link Frequency#toString()}.
     */
    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        String result = freq.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "hello\t 2 \t 33.33% \t 33.33%\n" +
                "world\t 1 \t 33.33% \t 66.67%\n" +
                "100.0%";

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link Frequency#valuesIterator()}.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        Iterator<Object> iterator = freq.valuesIterator();

        Object value = iterator.next();
        Assert.assertEquals("hello", value);

        value = iterator.next();
        Assert.assertEquals("world", value);

        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link Frequency#getSumFreq()}.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        long result = freq.getSumFreq();
        long expected = 3;

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link Frequency#getCount(Object)}.
     */
    @Test
    public void testGetCountObject() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        long count = freq.getCount("hello");
        long expected = 2;

        Assert.assertEquals(expected, count);
    }

    /**
     * Test method for {@link Frequency#getCount(int)}.
     */
    @Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        long count = freq.getCount(1);
        long expected = 2;

        Assert.assertEquals(expected, count);
    }

    /**
     * Test method for {@link Frequency#getCount(char)}.
     */
    @Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');

        long count = freq.getCount('a');
        long expected = 2;

        Assert.assertEquals(expected, count);
    }

    /**
     * Test method for {@link Frequency#getPct(Object)}.
     */
    @Test
    public void testGetPctObject() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        double result = freq.getPct("hello");
        double expected = 33.33;

        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getPct(int)}.
     */
    @Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        double result = freq.getPct(1);
        double expected = 33.33;

        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getPct(char)}.
     */
    @Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');

        double result = freq.getPct('a');
        double expected = 33.33;

        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getCumFreq(Object)}.
     */
    @Test
    public void testgetCumFreqObject() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        long result = freq.getCumFreq("hello");
        long expected = 2;

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link Frequency#getCumFreq(int)}.
     */
    @Test
    public void testgetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        long result = freq.getCumFreq(1);
        long expected = 2;

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link Frequency#getCumFreq(char)}.
     */
    @Test
    public void testgetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');

        long result = freq.getCumFreq('a');
        long expected = 2;

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link Frequency#getCumPct(Object)}.
     */
    @Test
    public void testgetCumPctObject() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        double result = freq.getCumPct("hello");
        double expected = 66.67;

        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getCumPct(int)}.
     */
    @Test
    public void testgetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        double result = freq.getCumPct(1);
        double expected = 66.67;

        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getCumPct(char)}.
     */
    @Test
    public void testgetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');

        double result = freq.getCumPct('a');
        double expected = 66.67;

        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getPcts()}.
     */
    @Test
    public void testGetPcts() {
        Frequency freq = new Frequency();
        freq.addValue("hello");
        freq.addValue("world");
        freq.addValue("hello");

        double[] result = freq.getPcts();
        double expected[] = new double[]{33.33, 33.33, 33.33};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link Frequency#getPercentiles(double)}.
     */
    @Test
    public void testGetPercentiles() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);

        double[] percentiles = freq.getPercentiles(0.5);
        double expected = new double[]{2.0, 2.5};

        Assert.assertArrayEquals(expected, percentiles, 0.001);
    }

    /**
     * Test method for {@link Frequency#getPercentiles(double[])}.
     */
    @Test
    public void testGetPercentilesArray() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);

        double[][] percentiles = freq.getPercentiles(new double[]{0.2, 0.5, 0.8});
        double[][] expected = new double[][]{{1.0, 1.5}, {2.0, 2.5}, {3.0, 3.0}};

        Assert.assertArrayEquals(expected, percentiles, 0.001);
    }

    /**
     * Test method for {@link Frequency#getPercentiles(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPercentilesInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);

        freq.getPercentiles(4);
    }

    /**
     * Test method for {@link Frequency#getPercentiles(double[], int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPercentilesArrayInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);

        freq.getPercentiles(new double[]{0.2, 0.5, 0.8}, 4);
    }
}