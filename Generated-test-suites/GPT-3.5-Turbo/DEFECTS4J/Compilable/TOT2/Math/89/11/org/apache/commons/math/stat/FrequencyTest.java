package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValueInteger() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
        freq.addValue(5);
        assertEquals(2, freq.getCount(5));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(100L);
        assertEquals(1, freq.getCount(100L));
    }

    @Test
    public void testAddValueCharacter() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.clear();
        assertEquals(0, freq.getCount(10));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(2);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(4);
        assertEquals(0.5, freq.getPct(2), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(2);
        assertEquals(2, freq.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(4);
        freq.addValue(4);
        assertEquals(0.666, freq.getCumPct(4), 0.001);
    }
}