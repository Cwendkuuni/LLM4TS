package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.valuesIterator().isEmpty());
    }

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getSumFreq());

        freq.addValue(2);
        assertEquals(2, freq.getCount(2));
        assertEquals(2, freq.getSumFreq());

        freq.addValue(1);
        assertEquals(3, freq.getCount(1));
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testAddValueComparableObject() {
        Frequency freq = new Frequency();
        freq.addValue(new Integer(1));
        freq.addValue(new Integer(2));
        freq.addValue(new Integer(1));
        assertEquals(3, freq.getCount(1));
        assertEquals(3, freq.getSumFreq());

        freq.addValue(new Integer(3));
        assertEquals(3, freq.getCount(1));
        assertEquals(4, freq.getCount(3));
        assertEquals(7, freq.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue('a');
    }

    @Test
    public void testAddValueNotComparableCharToInt() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue(1);
        assertEquals(0, freq.getCount('a'));
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getSumFreq());
    }

    @Test
    public void testAddValueNotComparableCharToLong() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue(Long.valueOf(1));
        assertEquals(0, freq.getCount('a'));
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getSumFreq());
    }

    @Test
    public void testAddValueComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        freq.addValue(new Integer(2));
        freq.addValue(new Integer(1));
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        String expected = "Value  \t Freq.  \t Pct.  \t Cum Pct. \n1  \t 2  \t 33.33%  \t 33.33% \n2  \t 1  \t 16.67%  \t 50.00% \n";
        assertEquals(expected, freq.toString());
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(3));
        assertEquals(3, freq.getCumFreq(4));
        assertNull(freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(33.33, freq.getCumPct(1), 0.01);
        assertEquals(50.00, freq.getCumPct(2), 0.01);
        assertEquals(100.00, freq.getCumPct(3), 0.01);
        assertNull(freq.getCumPct(4));
    }

    @Test
    public void testGetCountNegativeValue() {
        Frequency freq = new Frequency();
        freq.addValue(-1);
        freq.addValue(-1);
        freq.addValue(1);
        assertEquals(2, freq.getCount(-1));
        assertEquals(1, freq.getCount(1));
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCountZeroValue() {
        Frequency freq = new Frequency();
        freq.addValue(0);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(1, freq.getCount(0));
        assertEquals(2, freq.getCount(1));
        assertEquals(3, freq.getSumFreq());
    }
}