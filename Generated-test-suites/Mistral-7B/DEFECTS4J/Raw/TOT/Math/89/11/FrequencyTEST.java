package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import junit.framework.JUnit4TestAdapter;

public class FrequencyTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FrequencyTest.class);
    }

    /**
     * Test constructor with default comparator.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
        assertFalse(freq.valuesIterator().hasNext());
    }

    /**
     * Test constructor with custom comparator.
     */
    @org.junit.Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
        assertFalse(freq.valuesIterator().hasNext());
    }

    /**
     * Test addValue method with int value.
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals(1, ((Comparable) freq.valuesIterator().next()).intValue());

        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals(1, ((Comparable) freq.valuesIterator().next()).intValue());
    }

    /**
     * Test addValue method with long value.
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        assertEquals(1, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals(1, ((Comparable) freq.valuesIterator().next()).longValue());

        freq.addValue(2L);
        assertEquals(3, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals(1, ((Comparable) freq.valuesIterator().next()).longValue());
    }

    /**
     * Test addValue method with String value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueString() {
        Frequency freq = new Frequency();
        freq.addValue("test");
    }

    /**
     * Test addValue method with char value.
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals('a', ((Comparable) freq.valuesIterator().next()).charValue());

        freq.addValue('a');
        assertEquals(2, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals('a', ((Comparable) freq.valuesIterator().next()).charValue());
    }

    /**
     * Test addValue method with null value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test clear method.
     */
    @org.junit.Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(2, freq.getSumFreq());
        assertTrue(freq.valuesIterator().hasNext());
        assertTrue(freq.valuesIterator().hasNext());

        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertFalse(freq.valuesIterator().hasNext());
    }

    /**
     * Test valuesIterator method with empty Frequency.
     */
    @org.junit.Test
    public void testEmptyValuesIterator() {
        Frequency freq = new Frequency();
        assertFalse(freq.valuesIterator().hasNext());
    }

    /**
     * Test valuesIterator method with nonempty Frequency.
     */
    @org.junit.Test
    public void testNonemptyValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);

        assertTrue(freq.valuesIterator().hasNext());
        assertEquals(1, ((Comparable) freq.valuesIterator().next()).intValue());
        assertTrue(freq.valuesIterator().hasNext());
        assertEquals(2, ((Comparable) freq.valuesIterator().next()).intValue());
    }

    /**
     * Test getCount method
     */
    @org.junit.Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(1, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(0, freq.getCount(3));
    }

    /**
     * Test getCount method with null value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCountNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getCount(null);
    }

    /**
     * Test getCount method with non-Comparable value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCountNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getCount(new Object());
    }

    /**
     * Test getSumFreq method.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(4, freq.getSumFreq());
    }

    /**
     * Test getPct method with empty Frequency.
     */
    @org.junit.Test
    public void testGetPctEmpty() {
        Frequency freq = new Frequency();
        assertNaN(freq.getPct(1));
        assertNaN(freq.getPct(2));
        assertNaN(freq.getPct(3));
    }

    /**
     * Test getPct method with nonempty Frequency.
     */
    @org.junit.Test
    public void testGetPctNonEmpty() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(0.333, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.167, freq.getPct(3), 0.001);
    }

    /**
     * Test getPct method with null value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetPctNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getPct(null);
    }

    /**
     * Test getPct method with non-Comparable value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetPctNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getPct(new Object());
    }

    /**
     * Test getCumFreq method with empty Frequency.
     */
    @org.junit.Test
    public void testGetCumFreqEmpty() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq(null));
        assertEquals(0, freq.getCumFreq(1));
        assertEquals(0, freq.getCumFreq(2));
        assertEquals(0, freq.getCumFreq(3));
    }

    /**
     * Test getCumFreq method with nonempty Frequency.
     */
    @org.junit.Test
    public void testGetCumFreqNonEmpty() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(3));
        assertEquals(4, freq.getCumFreq(4));
    }

    /**
     * Test getCumFreq method with null value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getCumFreq(null);
    }

    /**
     * Test getCumFreq method with non-Comparable value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getCumFreq(new Object());
    }

    /**
     * Test getCumPct method with empty Frequency.
     */
    @org.junit.Test
    public void testGetCumPctEmpty() {
        Frequency freq = new Frequency();
        assertNaN(freq.getCumPct(1));
        assertNaN(freq.getCumPct(2));
        assertNaN(freq.getCumPct(3));
    }

    /**
     * Test getCumPct method with nonempty Frequency.
     */
    @org.junit.Test
    public void testGetCumPctNonEmpty() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(0.333, freq.getCumPct(1), 0.001);
        assertEquals(0.667, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test getCumPct method with null value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCumPctNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getCumPct(null);
    }

    /**
     * Test getCumPct method with non-Comparable value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCumPctNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.getCumPct(new Object());
    }

    private void assertNaN(double value) {
        assertNotEquals(0.0, value);
        assertFalse(Double.isFinite(value));
    }
}