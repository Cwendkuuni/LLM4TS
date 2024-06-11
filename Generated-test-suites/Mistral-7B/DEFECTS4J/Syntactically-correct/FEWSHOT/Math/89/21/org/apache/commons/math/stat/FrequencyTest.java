package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertEmpty(freq.valuesIterator());
    }

    @Test
    public void testFrequencyWithValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        assertEquals(6, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(3, freq.getCount(2));
        assertEquals(3, freq.getCount(3));
        assertEquals("1.0\t3\t30.0%\t30.0%\n2.0\t3\t30.0%\t60.0%\n3.0\t3\t30.0%\t100.0%\n", freq.toString());

        Long[] expectedValues = {Long.valueOf(1), Long.valueOf(2), Long.valueOf(3)};
        Object[] actualValues = new Object[freq.valuesIterator().size()];
        int i = 0;
        for (Iterator<Object> iterator = freq.valuesIterator(); iterator.hasNext(); i++) {
            actualValues[i] = iterator.next();
        }
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency freq = new Frequency();
        freq.addValue("nonComparableObject");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(new Integer(3));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertEmpty(freq.valuesIterator());
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        assertEquals(0, freq.getCumFreq(null));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(1.5));
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(5, freq.getCumFreq(2.5));
        assertEquals(6, freq.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        assertEquals(0.0, freq.getCumPct(null), 0.0);
        assertEquals(33.3, freq.getCumPct(1), 0.1);
        assertEquals(66.7, freq.getCumPct(2), 0.1);
        assertEquals(100.0, freq.getCumPct(3), 0.1);
    }

    private void assertEmpty(Iterator iterator) {
        assertFalse(iterator.hasNext());
    }
}