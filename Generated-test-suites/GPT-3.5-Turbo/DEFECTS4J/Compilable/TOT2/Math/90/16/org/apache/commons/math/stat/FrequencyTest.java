package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(100L);
        assertEquals(1, freq.getCount(100L));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.5, freq.getPct(1), 0.001);
    }

    @Test
    public void testCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
    }

    @Test
    public void testCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
    }
}