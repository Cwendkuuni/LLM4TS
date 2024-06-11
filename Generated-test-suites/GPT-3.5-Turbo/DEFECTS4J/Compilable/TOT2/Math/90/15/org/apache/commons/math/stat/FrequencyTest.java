package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.stat.Frequency;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5); // Typical use case
        assertEquals(1, freq.getCount(5));

        freq.addValue(10); // Edge case
        assertEquals(1, freq.getCount(10));

        try {
            freq.addValue("test"); // Error scenario
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);

        freq.clear();
        assertEquals(0, freq.getCount(5));
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        freq.addValue(5);

        assertEquals(2, freq.getCount(5));
        assertEquals(1, freq.getCount(10));
        assertEquals(0, freq.getCount(15)); // Value not present
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        freq.addValue(5);

        assertEquals(0.6666666666666666, freq.getPct(5), 0.00001);
        assertEquals(0.3333333333333333, freq.getPct(10), 0.00001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        freq.addValue(5);

        assertEquals(2, freq.getCumFreq(5));
        assertEquals(3, freq.getCumFreq(10));
        assertEquals(0, freq.getCumFreq(15));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        freq.addValue(5);

        assertEquals(0.6666666666666666, freq.getCumPct(5), 0.00001);
        assertEquals(1.0, freq.getCumPct(10), 0.00001);
    }
}