package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;

/**
 * Tests the {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Tests the default constructor.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency.freqTable);
    }

    /**
     * Tests the constructor with a custom comparator.
     */
    @org.junit.Test
    public void testCustomComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency.freqTable);
    }

    /**
     * Tests the toString method.
     */
    @org.junit.Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 2 \t 0.3333333333333333 \t 0.6666666666666667\n\n";
        assertEquals(expected, result);
    }

    /**
     * Tests the addValue method with an integer value.
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Tests the addValue method with a long value.
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(1, count);
    }

    /**
     * Tests the addValue method with a short value.
     */
    @org.junit.Test
    public void testAddValueShort() {
        Frequency frequency = new Frequency();
        frequency.addValue((short) 1);
        long count = frequency.getCount((short) 1);
        assertEquals(1, count);
    }

    /**
     * Tests the addValue method with a char value.
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        long count = frequency.getCount('A');
        assertEquals(1, count);
    }

    /**
     * Tests the addValue method with an Object value.
     */
    @org.junit.Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        long count = frequency.getCount(new Integer(1));
        assertEquals(1, count);
    }

    /**
     * Tests the getSumFreq method.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        assertEquals(4, sumFreq);
    }

    /**
     * Tests the getCount method with an integer value.
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    /**
     * Tests the getCount method with a long value.
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(2, count);
    }

    /**
     * Tests the getCount method with a char value.
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        long count = frequency.getCount('A');
        assertEquals(2, count);
    }

    /**
     * Tests the getCount method with an Object value.
     */
    @org.junit.Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        long count = frequency.getCount(new Integer(1));
        assertEquals(2, count);
    }

    /**
     * Tests the getPct method with an integer value.
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double pct = frequency.getPct(1);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    /**
     * Tests the getPct method with a long value.
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        double pct = frequency.getPct(1L);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    /**
     * Tests the getPct method with a char value.
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        double pct = frequency.getPct('A');
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    /**
     * Tests the getPct method with an Object value.
     */
    @org.junit.Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(2));
        double pct = frequency.getPct(new Integer(1));
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    /**
     * Tests the getCumFreq method with an integer value.
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
    }

    /**
     * Tests the getCumFreq method with a long value.
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        long cumFreq = frequency.getCumFreq(1L);
        assertEquals(1, cumFreq);
    }

    /**
     * Tests the getCumFreq method with a char value.
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        long cumFreq = frequency.getCumFreq('A');
        assertEquals(1, cumFreq);
    }

    /**
     * Tests the getCumFreq method with an Object value.
     */
    @org.junit.Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(2));
        long cumFreq = frequency.getCumFreq(new Integer(1));
        assertEquals(1, cumFreq);
    }

    /**
     * Tests the getCumPct method with an integer value.
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
    }

    /**
     * Tests the getCumPct method with a long value.
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        double cumPct = frequency.getCumPct(1L);
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
    }

    /**
     * Tests the getCumPct method with a char value.
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        double cumPct = frequency.getCumPct('A');
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
    }

    /**
     * Tests the getCumPct method with an Object value.
     */
    @org.junit.Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(2));
        double cumPct = frequency.getCumPct(new Integer(1));
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
    }

    /**
     * Tests the clear method.
     */
    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        long count = frequency.getCount(1);
        assertEquals(0, count);
    }
}