package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Frequency.
 */
public class FrequencyTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    /**
     * Test the constructor with a comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };

        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency.isEmpty());
    }

    /**
     * Test the addValue method with a Comparable value.
     */
    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        long value1 = frequency.getCount(new Integer(1));
        assertEquals(2, value1.longValue());
    }

    /**
     * Test the addValue method with a non-Comparable value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test the addValue method with values of different types.
     */
    @Test
    public void testAddValueDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        long value1 = frequency.getCount(new Integer(1));
        long value2 = frequency.getCount(Character.valueOf('a'));
        assertEquals(1, value1.longValue());
        assertEquals(1, value2.longValue());
    }

    /**
     * Test the clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }

    /**
     * Test the valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        Iterator iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(2), iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test the getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        long sumFreq = frequency.getSumFreq();
        assertEquals(2, sumFreq);
    }

    /**
     * Test the getCount method with a Comparable value.
     */
    @Test
    public void testGetCountComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        long value1 = frequency.getCount(new Integer(1));
        assertEquals(2, value1.longValue());
    }

    /**
     * Test the getCount method with a non-Comparable value.
     */
    @Test
    public void testGetCountNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        long value1 = frequency.getCount(new Integer(1));
        long value2 = frequency.getCount(Character.valueOf('a'));
        assertEquals(1, value1.longValue());
        assertEquals(0, value2.longValue());
    }

    /**
     * Test the getCount method with values of different types.
     */
    @Test
    public void testGetCountDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        long value1 = frequency.getCount(new Integer(1));
        long value2 = frequency.getCount(Character.valueOf('a'));
        assertEquals(1, value1.longValue());
        assertEquals(0, value2.longValue());
    }

    /**
     * Test the getPct method
     */
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        double pct1 = frequency.getPct(new Integer(1));
        double pct2 = frequency.getPct(new Integer(2));
        assertEquals(0.5, pct1, 0.001);
        assertEquals(0.0, pct2, 0.001);
    }

    /**
     * Test the getPct method with a non-Comparable value.
     */
    @Test
    public void testGetPctNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        double pct1 = frequency.getPct(new Integer(1));
        double pct2 = frequency.getPct(Character.valueOf('a'));
        assertEquals(0.5, pct1, 0.001);
        assertEquals(0.0, pct2, 0.001);
    }

    /**
     * Test the getPct method with values of different types.
     */
    @Test
    public void testGetPctDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        double pct1 = frequency.getPct(new Integer(1));
        double pct2 = frequency.getPct(Character.valueOf('a'));
        assertEquals(0.5, pct1, 0.001);
        assertEquals(0.0, pct2, 0.001);
    }

    /**
     * Test the getCumFreq method with a Comparable value.
     */
    @Test
    public void testGetCumFreqComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        long cumFreq1 = frequency.getCumFreq(new Integer(1));
        long cumFreq2 = frequency.getCumFreq(new Integer(2));
        assertEquals(1, cumFreq1);
        assertEquals(2, cumFreq2);
    }

    /**
     * Test the getCumFreq method with a non-Comparable value.
     */
    @Test
    public void testGetCumFreqNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        long cumFreq1 = frequency.getCumFreq(new Integer(1));
        long cumFreq2 = frequency.getCumFreq(Character.valueOf('a'));
        assertEquals(1, cumFreq1);
        assertEquals(1, cumFreq2);
    }

    /**
     * Test the getCumFreq method with values of different types.
     */
    @Test
    public void testGetCumFreqDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        long cumFreq1 = frequency.getCumFreq(new Integer(1));
        long cumFreq2 = frequency.getCumFreq(Character.valueOf('a'));
        assertEquals(1, cumFreq1);
        assertEquals(1, cumFreq2);
    }

    /**
     * Test the getCumPct method.
     */
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        double cumPct1 = frequency.getCumPct(new Integer(1));
        double cumPct2 = frequency.getCumPct(new Integer(2));
        assertEquals(0.5, cumPct1, 0.001);
        assertEquals(1.0, cumPct2, 0.001);
    }

    /**
     * Test the getCumPct method with a non-Comparable value.
     */
    @Test
    public void testGetCumPctNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        double cumPct1 = frequency.getCumPct(new Integer(1));
        double cumPct2 = frequency.getCumPct(Character.valueOf('a'));
        assertEquals(0.5, cumPct1, 0.001);
        assertEquals(0.5, cumPct2, 0.001);
    }

    /**
     * Test the getCumPct method with values of different types.
     */
    @Test
    public void testGetCumPctDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Character('a'));
        double cumPct1 = frequency.getCumPct(new Integer(1));
        double cumPct2 = frequency.getCumPct(Character.valueOf('a'));
        assertEquals(0.5, cumPct1, 0.001);
        assertEquals(0.5, cumPct2, 0.001);
    }
}