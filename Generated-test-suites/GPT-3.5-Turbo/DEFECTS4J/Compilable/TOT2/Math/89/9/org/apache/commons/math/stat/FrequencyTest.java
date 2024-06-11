package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValueWithInvalidInput() {
        Frequency freq = new Frequency();
        try {
            freq.addValue("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        freq.clear();
        assertEquals(0, freq.getCount(2));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(7);
        freq.addValue(7);
        assertEquals(2, freq.getCount(7));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(4);
        assertEquals(0.5, freq.getPct(2), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(2, freq.getCumFreq(4));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.6666, freq.getCumPct(1), 0.0001);
    }
}