package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        Frequency comparatorFreq = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFreq);
    }

    /**
     * Test addValue method with int value
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(count, 1);
    }

    /**
     * Test addValue method with long value
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        long count = freq.getCount(2);
        assertEquals(count, 1);
    }

    /**
     * Test addValue method with char value
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        long count = freq.getCount('a');
        assertEquals(count, 1);
    }

    /**
     * Test addValue method with Object value
     */
    @org.junit.Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(new Integer(1));
        long count = freq.getCount(1);
        assertEquals(count, 1);
    }

    /**
     * Test addValue method with uncomparable values
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    /**
     * Test clear method
     */
    @org.junit.Test
     public void testClear() {
         Frequency freq = new Frequency();
         freq.addValue(1);
         freq.clear();
         long count = freq.getCount(1);
         assertEquals(count, 0);
     }

    /**
     * Test valuesIterator method
     */
     @org.junit.Test
     public void testValuesIterator() {
         Frequency freq = new Frequency();
         freq.addValue(1);
         freq.addValue(2);
         Iterator iter = freq.valuesIterator();
         long count = 0;
         while (iter.hasNext()) {
             long value = (long) iter.next();
             count++;
             long frequency = freq.getCount(value);
             assertEquals(frequency, 1);
         }
         assertEquals(count, 2);
     }

    /**
     * Test getSumFreq method
     */
     @org.junit.Test
     public void testGetSumFreq() {
         Frequency freq = new Frequency();
         freq.addValue(1);
         freq.addValue(2);
         long sumFreq = freq.getSumFreq();
         assertEquals(sumFreq, 2);
     }

    /**
     * Test getCount method with int value
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(count, 1);
    }

    /**
     * Test getCount method with long value
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        long count = freq.getCount(2);
        assertEquals(count, 1);
    }

    /**
     * Test getCount method with char value
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        long count = freq.getCount('a');
        assertEquals(count, 1);
    }

    /**
     * Test getCount method with uncomparable values
     */
    @org.junit.Test
    public void testGetCountUncomparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        long count = freq.getCount(2);
        assertEquals(count, 0);
    }

    /**
     * Test getPct method with int value
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        double pct = freq.getPct(1);
        assertTrue(pct > 0 && pct < 1);
    }

    /**
     * Test getPct method with long value
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        double pct = freq.getPct(2);
        assertTrue(pct > 0 && pct < 1);
    }

    /**
     * Test getPct method with char value
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        double pct = freq.getPct('a');
        assertTrue(pct > 0 && pct < 1);
    }

    /**
     * Test getPct method with uncomparable values
     */
    @org.junit.Test
    public void testGetPctUncomparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        double pct = freq.getPct(2);
        assertEquals(pct, Double.NaN);
    }

    /**
     * Test getCumFreq method with int value
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long cumFreq = freq.getCumFreq(1);
        assertEquals(cumFreq, 1);
    }

    /**
     * Test getCumFreq method with long value
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long cumFreq = freq.getCumFreq(2);
        assertEquals(cumFreq, 2);
    }

    /**
     * Test getCumFreq method with char value
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        long cumFreq = freq.getCumFreq('b');
        assertEquals(cumFreq, 2);
    }

    /**
     * Test getCumFreq method with uncomparable values
     */
    @org.junit.Test
    public void testGetCumFreqUncomparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        long cumFreq = freq.getCumFreq(2);
        assertEquals(cumFreq, 0);
    }

    /**
     * Test getCumPct method with int value
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        double cumPct = freq.getCumPct(1);
        assertTrue(cumPct > 0 && cumPct <= 1);
    }

    /**
     * Test getCumPct method with long value
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        double cumPct = freq.getCumPct(2);
        assertTrue(cumPct > 0 && cumPct <= 1);
    }

    /**
     * Test getCumPct method with char value
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        double cumPct = freq.getCumPct('b');
        assertTrue(cumPct > 0 && cumPct <= 1);
    }

    /**
     * Test getCumPct method with uncomparable values
     */
    @org.junit.Test
    public void testGetCumPctUncomparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        double cumPct = freq.getCumPct(2);
        assertEquals(cumPct, Double.NaN);
    }
}