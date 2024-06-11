package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(3);
        freq.clear();
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(4);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(7);
        freq.addValue(7);
        freq.addValue(8);
        assertEquals(2, freq.getCount(7));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.6666666666666666, freq.getPct(1), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(4);
        assertEquals(2, freq.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.6666666666666666, freq.getCumPct(2), 0.0001);
    }
}