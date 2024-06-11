package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency freq = new Frequency();
        assertEquals("", freq.toString());
    }

    @Test
    public void testFrequencyDefaultComparator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.2 \t 0.5333333333333333\n", freq.toString());
    }

    @Test
    public void testFrequencyCustomComparator() {
        Comparator<Object> comparator = (o1, o2) -> {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
            return 0;
        };
        Frequency freq = new Frequency(comparator);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 1 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.6666666666666667 \t 1.0\n", freq.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(4L, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(2L, freq.getCount(1L));
        assertEquals(1L, freq.getCount(2L));
        assertNull(freq.getCount("x"));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.3333333333333333, freq.getPct(1L), 0.0000001);
        assertEquals(0.2, freq.getPct(2L), 0.0000001);
        assertNull(freq.getPct("x"));
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(1L, freq.getCumFreq(1L));
        assertEquals(2L, freq.getCumFreq(2L));
        assertEquals(3L, freq.getCumFreq(3L));
        assertNull(freq.getCumFreq("x"));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.3333333333333333, freq.getCumPct(1L), 0.0000001);
        assertEquals(0.6666666666666667, freq.getCumPct(2L), 0.0000001);
        assertEquals(1.0, freq.getCumPct(3L), 0.0000001);
        assertNull(freq.getCumPct("x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue("x");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableWithPreviousEntries() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue("x");
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();
        assertEquals("", freq.toString());
    }
}