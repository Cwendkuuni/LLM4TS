package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.TreeMap;

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
        Iterator iterator = freq.valuesIterator();
        assertTrue(iterator.hasNext());
        assertSame(1, iterator.next().longValue());

        freq.addValue(1);
        assertEquals(2, freq.getCount(1));

        freq.addValue(2);
        assertEquals(2, freq.getCount(1));
        assertEquals(3, freq.getCount(2));
        assertEquals(3, freq.getSumFreq());

        freq.addValue(1);
        assertEquals(3, freq.getCount(1));

        freq.addValue("a");
        assertFalse(freq.valuesIterator().hasNext());
        assertEquals(3, freq.getSumFreq());
        assertNull(freq.getCount("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        String result = freq.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n"
                + "2 \t 1 \t 0.1666666666666667 \t 0.4999999999999999 \n";
        assertEquals(expected, result);
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertTrue(freq.valuesIterator().isEmpty());
        assertEquals(0, freq.getSumFreq());
    }
}