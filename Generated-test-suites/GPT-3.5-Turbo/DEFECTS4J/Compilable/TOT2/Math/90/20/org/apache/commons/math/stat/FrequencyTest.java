package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddIntValue() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddLongValue() {
        Frequency freq = new Frequency();
        freq.addValue(100L);
        assertEquals(1, freq.getCount(100L));
    }

    @Test
    public void testAddCharValue() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        freq.clear();
        assertEquals(0, freq.getCount(5));
        assertEquals(0, freq.getCount(10));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.5, freq.getPct(1), 0.0);
        assertEquals(0.5, freq.getPct(2), 0.0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(2, freq.getCumFreq(3));
        assertEquals(3, freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.5, freq.getCumPct(1), 0.0);
        assertEquals(1.0, freq.getCumPct(2), 0.0);
    }
}