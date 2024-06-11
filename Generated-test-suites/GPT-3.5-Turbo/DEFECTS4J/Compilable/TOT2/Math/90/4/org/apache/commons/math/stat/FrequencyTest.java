package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testAddValueObject() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValueInt() {
        frequency.addValue(10);
        assertEquals(1, frequency.getCount(10));
    }

    @Test
    public void testAddValueLong() {
        frequency.addValue(15);
        assertEquals(1, frequency.getCount(15));
    }

    @Test
    public void testAddValueChar() {
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
    }

    @Test
    public void testClear() {
        frequency.addValue(5);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
    }

    @Test
    public void testGetSumFreq() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCountObject() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testGetCountInt() {
        frequency.addValue(10);
        assertEquals(1, frequency.getCount(10));
    }

    @Test
    public void testGetCountLong() {
        frequency.addValue(15);
        assertEquals(1, frequency.getCount(15));
    }

    @Test
    public void testGetCountChar() {
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
    }

    @Test
    public void testGetPctObject() {
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(0.5, frequency.getPct(5), 0.01);
    }

    @Test
    public void testGetPctInt() {
        frequency.addValue(10);
        assertEquals(1.0, frequency.getPct(10), 0.01);
    }

    @Test
    public void testGetPctLong() {
        frequency.addValue(15);
        assertEquals(1.0, frequency.getPct(15), 0.01);
    }

    @Test
    public void testGetPctChar() {
        frequency.addValue('A');
        assertEquals(1.0, frequency.getPct('A'), 0.01);
    }

    @Test
    public void testGetCumFreqObject() {
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(1, frequency.getCumFreq(5));
    }

    @Test
    public void testGetCumFreqInt() {
        frequency.addValue(10);
        assertEquals(1, frequency.getCumFreq(10));
    }

    @Test
    public void testGetCumFreqLong() {
        frequency.addValue(15);
        assertEquals(1, frequency.getCumFreq(15));
    }

    @Test
    public void testGetCumFreqChar() {
        frequency.addValue('A');
        assertEquals(1, frequency.getCumFreq('A'));
    }

    @Test
    public void testGetCumPctObject() {
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(0.5, frequency.getCumPct(5), 0.01);
    }

    @Test
    public void testGetCumPctInt() {
        frequency.addValue(10);
        assertEquals(1.0, frequency.getCumPct(10), 0.01);
    }

    @Test
    public void testGetCumPctLong() {
        frequency.addValue(15);
        assertEquals(1.0, frequency.getCumPct(15), 0.01);
    }

    @Test
    public void testGetCumPctChar() {
        frequency.addValue('A');
        assertEquals(1.0, frequency.getCumPct('A'), 0.01);
    }
}