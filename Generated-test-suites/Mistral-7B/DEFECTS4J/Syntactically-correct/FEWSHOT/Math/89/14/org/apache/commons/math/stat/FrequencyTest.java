package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testgetDefaultFrequency() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 1.0 \t 1.0\n", freq.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        assertEquals(0, freq.getCount(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    @Test
    public void testAddValueSameValueDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1L);
        freq.addValue('1');
        assertEquals(3, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(1L));
        assertEquals(1, freq.getCount('1'));
    }

    @Test
    public void testAddValueDifferentValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 0.5 \t 0.5\n2 \t 1 \t 0.5 \t 1.0\n", freq.toString());
    }

    @Test
    public void testAddValueNull() {
        Frequency freq = new Frequency();
        freq.addValue(null);
        assertNull(freq.getCount(null));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount('a'));
        assertEquals(1.0, freq.getPct('a'), 0.001);
        assertEquals(1.0, freq.getCumPct('a'), 0.001);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n"
                + "a \t 2 \t 1.0 \t 1.0\n", freq.toString());
    }

    @Test
    public void testAddValueLargeNumber() {
        Frequency freq = new Frequency();
        freq.addValue(Long.MAX_VALUE);
        freq.addValue(Long.MAX_VALUE);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(Long.MAX_VALUE));
        assertEquals(1.0, freq.getPct(Long.MAX_VALUE), 0.001);
        assertEquals(1.0, freq.getCumPct(Long.MAX_VALUE), 0.001);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n"
                + Long.toString(Long.MAX_VALUE) + " \t 2 \t 1.0 \t 1.0\n", freq.toString());
    }

    @Test
    public void testAddValueLargeNegativeNumber() {
        Frequency freq = new Frequency();
        freq.addValue(-Long.MAX_VALUE);
        freq.addValue(-Long.MAX_VALUE);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(-Long.MAX_VALUE));
        assertEquals(1.0, freq.getPct(-Long.MAX_VALUE), 0.001);
        assertEquals(1.0, freq.getCumPct(-Long.MAX_VALUE), 0.001);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n"
                + Long.toString(-Long.MAX_VALUE) + " \t 2 \t 1.0 \t 1.0\n", freq.toString());
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.getCount(1));
        assertTrue(freq.toString().isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        Iterator values = freq.valuesIterator();
        assertEquals(1, values.next());
        assertEquals(2, values.next());
        assertFalse(values.hasNext());
    }

    @Test
    public void testComparator() {
        Frequency freq = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare((Integer) o1.hashCode(), (Integer) o2.hashCode());
            }
        });
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n"
                + "1 \t 1 \t 0.5 \t 0.5\n2 \t 1 \t 0.5 \t 1.0\n", freq.toString());
    }
}