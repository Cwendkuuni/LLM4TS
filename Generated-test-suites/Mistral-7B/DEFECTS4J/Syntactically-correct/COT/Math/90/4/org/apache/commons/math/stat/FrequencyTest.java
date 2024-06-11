package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency.
 */
public class FrequencyTest {

    /**
     * Test constructor with no comparator.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testConstructorWithComparator() {
        Comparator<Object> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.3333333333333333 \t 0.6666666666666667\nValue \t Freq. \t Pct. \t Cum Pct. \n", frequency.toString());
    }

    /**
     * Test addValue method with int value.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test addValue method with long value.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1L);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test addValue method with char value.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        frequency.addValue('a');
        assertEquals(2, frequency.getCount('a'));
    }

    /**
     * Test addValue method with non-comparable value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test addValue method with null value.
     */
    @Test(expected = NullPointerException.class)
    public void testAddValueNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    /**
     * Test addValue method with duplicate value.
     */
    @Test
    public void testAddValueDuplicateValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test getCount method with valid value.
     */
    @Test
    public void testGetCountValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test getCount method with invalid value.
     */
    @Test
    public void testGetCountInvalidValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCount(new Object()));
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(4, frequency.getSumFreq());
    }

    /**
     * Test getCount method with multiple values.
     */
    @Test
    public void testGetCountMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));
        assertEquals(5, frequency.getSumFreq());
    }

    /**
     * Test getPct method with valid value.
     */
    @Test
    public void testGetPctValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double pct1 = frequency.getPct(1);
        assertEquals(0.3333333333333333, pct1, 0.0000001);
        double pct2 = frequency.getPct(2);
        assertEquals(0.3333333333333333, pct2, 0.0000001);
    }

    /**
     * Test getPct method with invalid value.
     */
    @Test
    public void testGetPctInvalidValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getPct(new Object()));
    }

    /**
     * Test getCumFreq method with valid value.
     */
    @Test
    public void testGetCumFreqValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(3, frequency.getCumFreq(4));
    }

    /**
     * Test getCumFreq method with invalid value.
     */
    @Test
    public void testGetCumFreqInvalidValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumFreq(new Object()));
    }

    /**
     * Test getCumFreq method with value less than the minimum value.
     */
    @Test
    public void testGetCumFreqValueLessThanMinimum() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumFreq(-1));
    }

    /**
     * Test getCumFreq method with value greater than the maximum value.
     */
    @Test
    public void testGetCumFreqValueGreaterThanMaximum() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumFreq(5));
    }

    /**
     * Test getCumPct method with valid value.
     */
    @Test
    public void testGetCumPctValidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.3333333333333333, cumPct1, 0.0000001);
        double cumPct2 = frequency.getCumPct(2);
        assertEquals(0.6666666666666667, cumPct2, 0.0000001);
    }

    /**
     * Test getCumPct method with invalid value.
     */
    @Test
    public void testGetCumPctInvalidValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumPct(new Object()));
    }

    /**
     * Test getCumPct method with value less than the minimum value.
     */
    @Test
    public void testGetCumPctValueLessThanMinimum() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumPct(-1));
    }

    /**
     * Test getCumPct method with value greater than the maximum value.
     */
    @Test
    public void testGetCumPctValueGreaterThanMaximum() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumPct(5));
    }
}