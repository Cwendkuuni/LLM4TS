package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
  
        freq.addValue("test");
        assertEquals(1, freq.getCount("test"));

        freq.addValue('c');
        assertEquals(1, freq.getCount('c'));
    }

    @Test
    public void testAddValueException() {
        Frequency freq = new Frequency();
        try {
            freq.addValue(new Object()); // Adding non-comparable object
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.clear();
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue("test");
        freq.addValue('c');
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue("test");
        freq.addValue('c');
        assertEquals(1, freq.getCount(3));
        assertEquals(1, freq.getCount("test"));
        assertEquals(1, freq.getCount('c'));
    }

    @Test
    public void testGetCountNotComparable() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCount(new Object()));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(3);
        assertEquals(0.6, freq.getPct(3), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(3, freq.getCumFreq(2));
    }

    @Test
    public void testGetCumFreqNotComparable() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq(new Object()));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.5, freq.getCumPct(2), 0.01);
    }

}