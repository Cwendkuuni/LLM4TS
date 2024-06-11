package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.valuesIterator().isEmpty());
    }

    @Test
    public void testAddValuesToFrequency() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(6, freq.getSumFreq());
        assertIterableEquals(asList(1L, 1L, 2L, 2L), freq.valuesIterator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValueToFrequency() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValuesToFrequency() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(new Object());
    }

    @Test
    public void testClearFrequency() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.valuesIterator().isEmpty());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(0.3333333333333333, freq.getPct(1), 0.0001);
        assertEquals(0.6666666666666666, freq.getPct(2), 0.0001);
        assertEquals(0.0, freq.getPct(3), 0.0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(5, freq.getCumFreq(2));
        assertEquals(5, freq.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(0.3333333333333333, freq.getPct(1), 0.0001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(0.6666666666666666, freq.getPct(2), 0.0001);
        assertEquals(0.8333333333333333, freq.getCumPct(2), 0.001);
        assertEquals(1, freq.getCumPct(3), 0.01);
    }

    private static <T> Iterable<T> asList(T... ts) {
        return () -> Arrays.asList(ts).iterator();
    }
}