package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testFrequencyConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testFrequencyConstructorWithComparator() {
        Comparator comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        long sumFreq = freq.getSumFreq();
        assertEquals(2, sumFreq);
    }

    /**
     * Test getCount method with valid value.
     */
    @Test
    public void testGetCountValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Test getCount method with invalid value.
     */
    @Test
    public void testGetCountInvalidValue() {
        Frequency freq = new Frequency();
        long count = freq.getCount(2);
        assertNull(count);
    }

    /**
     * Test getCount method with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCountNullValue() {
        Frequency freq = new Frequency();
        freq.getCount(null);
    }

    /**
     * Test getPct method with valid value.
     */
    @Test
    public void testGetPctValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        double pct = freq.getPct(1);
        assertEquals(1.0, pct, 0.001);
    }

    /**
     * Test getPct method with invalid value.
     */
    @Test
    public void testGetPctInvalidValue() {
        Frequency freq = new Frequency();
        double pct = freq.getPct(2);
        assertEquals(0.0, pct, 0.001);
    }

    /**
     * Test getPct method with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPctNullValue() {
        Frequency freq = new Frequency();
        freq.getPct(null);
    }

    /**
     * Test getCumFreq method with valid value.
     */
    @Test
    public void testGetCumFreqValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long cumFreq = freq.getCumFreq(1);
        assertEquals(1, cumFreq);
    }

    /**
     * Test getCumFreq method with invalid value.
     */
    @Test
    public void testGetCumFreqInvalidValue() {
        Frequency freq = new Frequency();
        long cumFreq = freq.getCumFreq(2);
        assertEquals(0, cumFreq);
    }

    /**
     * Test getCumFreq method with null value.
     */
    @Test
    public void testGetCumFreqNullValue() {
        Frequency freq = new Frequency();
        long cumFreq = freq.getCumFreq(null);
        assertEquals(0, cumFreq);
    }

    /**
     * Test getCumPct method with valid value.
     */
    @Test
    public void testGetCumPctValidValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        double cumPct = freq.getCumPct(1);
        assertEquals(1.0, cumPct, 0.001);
    }

    /**
     * Test getCumPct method with invalid value.
     */
    @Test
    public void testGetCumPctInvalidValue() {
        Frequency freq = new Frequency();
        double cumPct = freq.getCumPct(2);
        assertEquals(0.0, cumPct, 0.001);
    }

    /**
     * Test getCumPct method with null value.
     */
    @Test
    public void testGetCumPctNullValue() {
        Frequency freq = new Frequency();
        double cumPct = freq.getCumPct(null);
        assertEquals(0.0, cumPct, 0.001);
    }

    /**
     * Test addValue method with valid integer value.
     */
    @Test
    public void testAddValueValidIntegerValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Test addValue method with valid long value.
     */
    @Test
    public void testAddValueValidLongValue() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        long count = freq.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Test addValue method with valid Integer value.
     */
    @Test
    public void testAddValueValidIntegerObjectValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Integer(1));
        long count = freq.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Test addValue method with valid Long value.
     */
    @Test
    public void testAddValueValidLongObjectValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Long(1));
        long count = freq.getCount(1);
        assertEquals(1, count);
    }

    /**
     * Test addValue method with invalid object value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidObjectValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    /**
     * Test addValue method with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test addValue method with incomparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIncomparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        long count = freq.getCount(1);
        assertEquals(0, count);
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        Iterator values = freq.valuesIterator();
        assertEquals(1, values.next().longValue());
        assertEquals(2, values.next().longValue());
        assertNull(values.next());
    }
}