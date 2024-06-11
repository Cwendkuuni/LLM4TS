package org.apache.commons.math.stat;

import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValueAndGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddIntegerValueAndGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(Integer.valueOf(10));
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddCharValueAndGetCount() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));
    }

    @Test
    public void testAddValueAndGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1.0, freq.getPct(5), 0.01);
    }

    @Test
    public void testAddValueAndGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(1.0, freq.getCumPct(5), 0.01);
    }

    @Test
    public void testExceptionHandling() {
        Frequency freq = new Frequency();
        assertThrows(IllegalArgumentException.class, () -> freq.addValue("abc"));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.clear();
        assertEquals(0, freq.getCount(5));
    }
}