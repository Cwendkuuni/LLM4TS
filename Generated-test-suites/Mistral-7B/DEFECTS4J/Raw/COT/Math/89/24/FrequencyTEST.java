package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Test cases for {@link Frequency} class
 */
public class FrequencyTest {

    /** Test data for various methods */
    private Frequency testFrequency;

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        testFrequency = new Frequency();
        assertNotNull(testFrequency);

        testFrequency = new Frequency(new NaturalComparator());
        assertNotNull(testFrequency);
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.addValue(2);
        testFrequency.addValue(2);
        String result = testFrequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.4 \t 0.4\n2 \t 2 \t 0.4 \t 0.8\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method with Integer values
     */
    @Test
    public void testAddValueInteger() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        assertEquals(1, testFrequency.getCount(1));
        testFrequency.addValue(1);
        assertEquals(2, testFrequency.getCount(1));
        testFrequency.addValue(2);
        assertEquals(1, testFrequency.getCount(2));
    }

    /**
     * Test addValue method with Long values
     */
    @Test
    public void testAddValueLong() {
        testFrequency = new Frequency();
        testFrequency.addValue(1L);
        assertEquals(1, testFrequency.getCount(1L));
        testFrequency.addValue(1L);
        assertEquals(2, testFrequency.getCount(1L));
        testFrequency.addValue(2L);
        assertEquals(1, testFrequency.getCount(2L));
    }

    /**
     * Test addValue method with Character values
     */
    @Test
    public void testAddValueCharacter() {
        testFrequency = new Frequency();
        testFrequency.addValue('A');
        assertEquals(1, testFrequency.getCount('A'));
        testFrequency.addValue('A');
        assertEquals(2, testFrequency.getCount('A'));
        testFrequency.addValue('B');
        assertEquals(1, testFrequency.getCount('B'));
    }

    /**
     * Test addValue method with non-comparable values (exception handling)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableValues() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue('A');
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.clear();
        assertTrue(testFrequency.getSumFreq() == 0);
    }

    /**
     * Test getSumFreq method
     */
    @Test
    public void testGetSumFreq() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.addValue(2);
        testFrequency.addValue(2);
        assertEquals(6, testFrequency.getSumFreq());
    }

    /**
     * Test getCount methods with various values
     */
    @Test
    public void testGetCount() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.addValue(2);
        testFrequency.addValue(2);
        assertEquals(2, testFrequency.getCount(1));
        assertEquals(2, testFrequency.getCount(2));
        assertEquals(0, testFrequency.getCount('A'));
    }

    /**
     * Test getCount method with negative value (exception handling)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCountNegativeValue() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.getCount(-1);
    }

    /**
     * Test getPct methods with various values
     */
    @Test
    public void testGetPct() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.addValue(2);
        testFrequency.addValue(2);
        assertEquals(0.25, testFrequency.getPct(1), 0.01);
        assertEquals(0.5, testFrequency.getPct(2), 0.01);
        assertEquals(Double.NaN, testFrequency.getPct('A'), 0.01);

        testFrequency = new Frequency();
        testFrequency.addValue(1);
        assertEquals(0.0, testFrequency.getPct(1), 0.01);
    }

    /**
     * Test getCumFreq methods with various values
     */
    @Test
    public void testGetCumFreq() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.addValue(2);
        testFrequency.addValue(2);
        assertEquals(1, testFrequency.getCumFreq(1));
        assertEquals(3, testFrequency.getCumFreq(2));
        assertEquals(6, testFrequency.getCumFreq(4));

        testFrequency = new Frequency();
        testFrequency.addValue(1);
        assertEquals(0, testFrequency.getCumFreq(1));
    }

    /**
     * Test getCumFreq method with non-comparable values (exception handling)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNonComparableValues() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue('A');
        testFrequency.getCumFreq(2);
    }

    /**
     * Test getCumPct methods with various values
     */
    @Test
    public void testGetCumPct() {
        testFrequency = new Frequency();
        testFrequency.addValue(1);
        testFrequency.addValue(1);
        testFrequency.addValue(2);
        testFrequency.addValue(2);
        assertEquals(0.25, testFrequency.getCumPct(1), 0.01);
        assertEquals(0.5, testFrequency.getCumPct(2), 0.01);
        assertEquals(1.0, testFrequency.getCumPct(3), 0.01);

        testFrequency = new Frequency();
        testFrequency.addValue(1);
        assertEquals(0.0, testFrequency.getCumPct(1), 0.01);
    }
}