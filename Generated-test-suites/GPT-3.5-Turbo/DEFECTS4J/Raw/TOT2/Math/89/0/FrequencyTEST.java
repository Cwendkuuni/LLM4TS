package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n", frequency.toString());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        frequency.clear();
        assertEquals(0, frequency.getCount(3));
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(2));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(4);
        assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        assertEquals(1.0, frequency.getPct(3), 0.001);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
    }

}