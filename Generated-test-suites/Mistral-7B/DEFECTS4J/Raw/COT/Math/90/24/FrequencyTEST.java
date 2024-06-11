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
    public void testFrequency() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testFrequencyWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    /**
     * Test addValue method with basic input.
     */
    @Test
    public void testAddValueBasic() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Long count = (Long) frequency.freqTable.get(1L);
        assertTrue(count != null && count.longValue() > 0);
    }

    /**
     * Test addValue method with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNegative() {
        Frequency frequency = new Frequency();
        frequency.addValue(-1);
    }

    /**
     * Test addValue method with non-comparable input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue("A");
    }

    /**
     * Test addValue method with double input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDouble() {
        Frequency frequency = new Frequency();
        frequency.addValue(1.5);
    }

    /**
     * Test addValue method with char input.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        Long count = (Long) frequency.freqTable.get('A');
        assertTrue(count != null && count.longValue() > 0);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator values = frequency.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals(1, values.next().longValue());
        assertTrue(values.hasNext());
        assertEquals(2, values.next().longValue());
        assertFalse(values.hasNext());
    }

    /**
     * Test getSumFreq method with empty frequency distribution.
     */
    @Test
    public void testGetSumFreqEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test getSumFreq method with non-empty frequency distribution.
     */
    @Test
    public void testGetSumFreqNonEmpty() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());
    }

    /**
     * Test getCount method with non-existent value.
     */
    @Test
    public void testGetCountNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCount('A'));
    }

    /**
     * Test getCount method with existent value.
     */
    @Test
    public void testGetCountExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test getCount method with char value.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(2, frequency.getCount('A'));
    }

    /**
     * Test getPct method with non-existent value.
     */
    @Test
    public void testGetPctNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(0.0, frequency.getPct(2), 0.01);
    }

    /**
     * Test getPct method with existent value.
     */
    @Test
    public void testGetPctExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(0.5, frequency.getPct(1), 0.01);
    }

    /**
     * Test getPct method with char value.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(0.5, frequency.getPct('A'), 0.01);
    }

    /**
     * Test getCumFreq method with non-comparable value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.getCumFreq('A');
    }

    /**
     * Test getCumFreq method with non-existent value.
     */
    @Test
    public void testGetCumFreqNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumFreq(1));
    }

    /**
     * Test getCumFreq method with existent value.
     */
    @Test
    public void testGetCumFreqExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    /**
     * Test getCumPct method with non-existent value.
     */
    @Test
    public void testGetCumPctNonExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(0.0, frequency.getCumPct(2), 0.01);
    }

    /**
     * Test getCumPct method with existent value.
     */
    @Test
    public void testGetCumPctExistent() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(0.5, frequency.getCumPct(1), 0.01);
        assertEquals(1.0, frequency.getCumPct(2), 0.01);
    }
}