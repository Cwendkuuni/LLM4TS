package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testFrequencyConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency frequencyComparator = new Frequency(new NaturalComparator());
        assertNotNull(frequencyComparator);
    }

    /**
     * Test addValue method for Integer value
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);

        frequency.clear();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
    }

    /**
     * Test addValue method for Long value
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(2));
        assertEquals(1.0, frequency.getPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);

        frequency.clear();
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
        assertEquals(1.0, frequency.getPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test addValue method for Character value
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');

        assertEquals(3, frequency.getSumFreq());
        assertEquals(3, frequency.getCount('a'));
        assertEquals(1.0, frequency.getPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('a'), 0.001);

        frequency.clear();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1.0, frequency.getPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('a'), 0.001);
    }

    /**
     * Test addValue method for non-comparable value
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test addValue method for non-Comparable value
     */
    @org.junit.Test(expected = ClassCastException.class)
    public void testAddValueNonComparable2() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new String("1"));
    }

    /**
     * Test clear method
     */
    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    /**
     * Test valuesIterator method
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        Iterator values = frequency.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals(1, ((Number)values.next()).longValue());
        assertTrue(values.hasNext());
        assertEquals(2, ((Number)values.next()).longValue());
        assertTrue(values.hasNext());
        assertEquals(1, ((Number)values.next()).longValue());
        assertFalse(values.hasNext());
    }

    /**
     * Test getSumFreq method
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(3, frequency.getSumFreq());
    }

    /**
     * Test getCount method for Integer value
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(3, frequency.getCount(1));
    }

    /**
     * Test getCount method for Long value
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        assertEquals(3, frequency.getCount(2));
    }

    /**
     * Test getCount method for Character value
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');

        assertEquals(3, frequency.getCount('a'));
    }

    /**
     * Test getCount method for non-comparable value
     */
    @org.junit.Test
    public void testGetCountNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());

        assertEquals(0, frequency.getCount(new Object()));
    }

    /**
     * Test getPct method for Integer value
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(0.333, frequency.getPct(1), 0.001);
    }

    /**
     * Test getPct method for Long value
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        assertEquals(0.667, frequency.getPct(2), 0.001);
    }

    /**
     * Test getPct method for Character value
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');

        assertEquals(0.667, frequency.getPct('a'), 0.001);
    }

    /**
     * Test getPct method for non-comparable value
     */
    @org.junit.Test
    public void testGetPctNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());

        assertEquals(Double.NaN, frequency.getPct(new Object()), 0.0);
    }

    /**
     * Test getPct method when no values added
     */
    @org.junit.Test
    public void testGetPctNoValues() {
        Frequency frequency = new Frequency();

        assertEquals(Double.NaN, frequency.getPct(1), 0.0);
    }

    /**
     * Test getCumFreq method for Integer value
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
    }

    /**
     * Test getCumFreq method for Long value
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(3));
    }

    /**
     * Test getCumFreq method for Character value
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');

        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(1, frequency.getCumFreq('b'));
        assertEquals(3, frequency.getCumFreq('c'));
    }

    /**
     * Test getCumFreq method for non-comparable value
     */
    @org.junit.Test
    public void testGetCumFreqNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());

        assertEquals(0, frequency.getCumFreq(new Object()));
    }

    /**
     * Test getCumFreq method when no values added
     */
    @org.junit.Test
    public void testGetCumFreqNoValues() {
        Frequency frequency = new Frequency();

        assertEquals(0, frequency.getCumFreq(1));
    }

    /**
     * Test getCumPct method for Integer value
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(0.333, frequency.getCumPct(1), 0.001);
        assertEquals(0.667, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    /**
     * Test getCumPct method for Long value
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        assertEquals(0.0, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    /**
     * Test getCumPct method for Character value
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');

        assertEquals(0.333, frequency.getCumPct('a'), 0.001);
        assertEquals(0.667, frequency.getCumPct('b'), 0.001);
        assertEquals(1.0, frequency.getCumPct('c'), 0.001);
    }

    /**
     * Test getCumPct method for non-comparable value
     */
    @org.junit.Test
    public void testGetCumPctNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());

        assertEquals(Double.NaN, frequency.getCumPct(new Object()), 0.0);
    }

    /**
     * Test getCumPct method when no values added
     */
    @org.junit.Test
    public void testGetCumPctNoValues() {
        Frequency frequency = new Frequency();

        assertEquals(0.0, frequency.getCumPct(1), 0.001);
    }
}