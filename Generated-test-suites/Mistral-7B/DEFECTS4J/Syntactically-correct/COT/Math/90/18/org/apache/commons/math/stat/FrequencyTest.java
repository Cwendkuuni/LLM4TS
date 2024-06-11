package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor without comparator
     */
    @Test
    public void testFrequencyConstructorWithoutComparator() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
    }

    /**
     * Test constructor with comparator
     */
    @Test
    public void testFrequencyConstructorWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        String result = freq.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.2 \t 0.5333333333333333\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue with Integer value
     */
    @Test
    public void testAddValueWithInteger() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Test addValue with Long value
     */
    @Test
    public void testAddValueWithLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        long count = freq.getCount(1L);
        assertEquals(1, count);
    }

    /**
     * Test addValue with Character value
     */
    @Test
    public void testAddValueWithCharacter() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        long count = freq.getCount('a');
        assertEquals(1, count);
    }

    /**
     * Test addValue with non-comparable value (char with int)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    /**
     * Test addValue with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test getSumFreq method
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        long sumFreq = freq.getSumFreq();
        assertEquals(4, sumFreq);
    }

    /**
     * Test getCount method with valid value
     */
    @Test
    public void testGetCountWithValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(2, count);
    }

    /**
     * Test getCount method with invalid value
     */
    @Test
    public void testGetCountWithInvalidValue() {
        Frequency freq = new Frequency();
        long count = freq.getCount(2);
        assertEquals(0, count);
    }

    /**
     * Test getPct method with valid value
     */
    @Test
    public void testGetPctWithValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        double pct = freq.getPct(1);
        assertEquals(0.5, pct, 0.001);
    }

    /**
     * Test getPct method with invalid value
     */
    @Test
    public void testGetPctWithInvalidValue() {
        Frequency freq = new Frequency();
        double pct = freq.getPct(2);
        assertEquals(Double.NaN, pct);
    }

    /**
     * Test getCumFreq method with valid value
     */
    @Test
    public void testGetCumFreqWithValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        long cumFreq = freq.getCumFreq(1);
        assertEquals(2, cumFreq);
    }

    /**
     * Test getCumFreq method with invalid value
     */
    @Test
    public void testGetCumFreqWithInvalidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        long cumFreq = freq.getCumFreq(2);
        assertEquals(0, cumFreq);
    }

    /**
     * Test getCumPct method with valid value
     */
    @Test
    public void testGetCumPctWithValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        double cumPct = freq.getCumPct(1);
        assertEquals(0.5, cumPct, 0.001);
    }

    /**
     * Test getCumPct method with invalid value
     */
    @Test
    public void testGetCumPctWithInvalidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        double cumPct = freq.getCumPct(2);
        assertEquals(Double.NaN, cumPct);
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        long sumFreq = freq.getSumFreq();
        assertEquals(0, sumFreq);
    }

    /**
     * Test valuesIterator method
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        Iterator values = freq.valuesIterator();
        assertEquals(1, values.next().longValue());
        assertEquals(1, values.next().longValue());
        assertThrows(NoSuchElementException.class, () -> values.next());
    }
}