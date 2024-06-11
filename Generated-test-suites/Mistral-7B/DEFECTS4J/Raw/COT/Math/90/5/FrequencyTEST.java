package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency frequencyComparator = new Frequency(new NaturalComparator());
        assertNotNull(frequencyComparator);
    }

    /**
     * Test toString method when empty.
     */
    @Test
    public void testToStringEmpty() {
        Frequency frequency = new Frequency();
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n";
        assertEquals(expected, result);
    }

    /**
     * Test toString method when not empty.
     */
    @Test
    public void testToStringNotEmpty() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n"
                + "2 \t 1 \t 0.16666666666666664 \t 0.4999999999999999 \n"
                + "1 \t 1 \t 0.16666666666666664 \t 1.0 \n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method with int.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(3, count);
    }

    /**
     * Test addValue method with long.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(3, count);
    }

    /**
     * Test addValue method with char.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    /**
     * Test addValue method with non-Comparable object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long sumFreq = frequency.getSumFreq();
        assertEquals(4, sumFreq);
    }

    /**
     * Test getCount method with int.
     */
    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(3, count);
    }

    /**
     * Test getCount method with long.
     */
    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(3, count);
    }

    /**
     * Test getCount method with char.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    /**
     * Test getCount method with non-existent value.
     */
    @Test
    public void testGetCountNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long count = frequency.getCount(3);
        assertEquals(0, count);
    }

    /**
     * Test getPct method with int.
     */
    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    /**
     * Test getPct method with long.
     */
    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        double pct = frequency.getPct(1L);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    /**
     * Test getPct method with char.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        double pct = frequency.getPct('a');
        assertEquals(0.4, pct, 0.0000001);
    }

    /**
     * Test getPct method with non-existent value.
     */
    @Test
    public void testGetPctNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double pct = frequency.getPct(3);
        assertEquals(Double.NaN, pct, 0.0);
    }

    /**
     * Test getCumFreq method with int.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(2, cumFreq);
    }

    /**
     * Test getCumFreq method with long.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        long cumFreq = frequency.getCumFreq(1L);
        assertEquals(2, cumFreq);
    }

    /**
     * Test getCumFreq method with char.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        long cumFreq = frequency.getCumFreq('a');
        assertEquals(2, cumFreq);
    }

    /**
     * Test getCumFreq method with non-existent value.
     */
    @Test
    public void testGetCumFreqNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(3);
        assertEquals(0, cumFreq);
    }

    /**
     * Test getCumPct method with int.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.6666666666666666, cumPct, 0.0000001);
    }

    /**
     * Test getCumPct method with long.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        double cumPct = frequency.getCumPct(1L);
        assertEquals(0.6666666666666666, cumPct, 0.0000001);
    }

    /**
     * Test getCumPct method with char.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        double cumPct = frequency.getCumPct('a');
        assertEquals(0.8, cumPct, 0.0000001);
    }

    /**
     * Test getCumPct method with non-existent value.
     */
    @Test
    public void testGetCumPctNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(3);
        assertEquals(Double.NaN, cumPct, 0.0);
    }
}