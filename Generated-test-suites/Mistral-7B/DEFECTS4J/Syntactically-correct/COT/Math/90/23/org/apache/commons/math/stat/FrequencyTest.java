package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

import java.util.Comparator;

/**
 * Test cases for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    /**
     * Test constructor with custom comparator
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency.isEmpty());
    }

    /**
     * Test addValue method with valid int value
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumPct(1));
    }

    /**
     * Test addValue method with valid long value
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(1, frequency.getCumPct(2));
    }

    /**
     * Test addValue method with valid char value
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
        assertEquals(1, frequency.getCumFreq('A'));
        assertEquals(1, frequency.getCumPct('A'));
    }

    /**
     * Test addValue method with valid Comparable value
     */
    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumPct(1));
    }

    /**
     * Test addValue method with invalid value (non-comparable value)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("nonComparableValue");
    }

    /**
     * Test addValue method with non-comparable values of different types
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
    }

    /**
     * Test getSumFreq method
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());
    }

    /**
     * Test getCount method with valid value
     */
    @Test
    public void testGetCountValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    /**
     * Test getCount method with invalid value
     */
    @Test
    public void testGetCountInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0, frequency.getCount(2));
    }

    /**
     * Test getPct method with valid value
     */
    @Test
    public void testGetPctValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1.0, frequency.getPct(1), 0.001);
    }

    /**
     * Test getPct method with invalid value
     */
    @Test
    public void testGetPctInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(Double.NaN, frequency.getPct(2));
    }

    /**
     * Test getCumFreq method with valid value
     */
    @Test
    public void testGetCumFreqValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
    }

    /**
     * Test getCumFreq method with invalid value
     */
    @Test
    public void testGetCumFreqInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0, frequency.getCumFreq(2));
    }

    /**
     * Test getCumPct method with valid value
     */
    @Test
    public void testGetCumPctValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
    }

    /**
     * Test getCumPct method with invalid value
     */
    @Test
    public void testGetCumPctInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0.0, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test valuesIterator method
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }
}