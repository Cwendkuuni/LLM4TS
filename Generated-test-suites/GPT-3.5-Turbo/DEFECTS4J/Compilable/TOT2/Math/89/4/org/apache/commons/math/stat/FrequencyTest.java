package org.apache.commons.math.stat;
import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(10);
        frequency.clear();
        assertEquals(0, frequency.getCount(10));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("hello");
        assertEquals(1, frequency.getCount("hello"));
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1.0, frequency.getPct(5), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(4);
        frequency.addValue(4);
        assertEquals(1.0, frequency.getCumPct(4), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_Exception() {
        Frequency frequency = new Frequency();
        frequency.addValue("string"); // Trying to add non-comparable value
    }
}