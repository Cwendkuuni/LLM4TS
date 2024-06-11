package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency implementation
 */
public class FrequencyTest {

    /**
     * Test constructor without comparator
     */
    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency instanceof Frequency);
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount('a'));
        assertNull(frequency.getCount('z'));
        Iterator iterator = frequency.valuesIterator();
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor with comparator
     */
    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency instanceof Frequency);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(1));
        assertNull(frequency.getCount('a'));
        assertNull(frequency.getCount('z'));
        Iterator iterator = frequency.valuesIterator();
        assertFalse(iterator.hasNext());
    }

    /**
     * Test addValue method with valid values
     */
    @Test
    public void testAddValueValidValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        Iterator iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(1, iterator.next().longValue());
        assertFalse(iterator.hasNext());
        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('z'));
    }

    /**
     * Test addValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('z');
        frequency.addValue(new Object());
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('z');
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(1));
        assertNull(frequency.getCount('a'));
        assertNull(frequency.getCount('z'));
        Iterator iterator = frequency.valuesIterator();
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getSumFreq method
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(4, frequency.getSumFreq());
    }

    /**
     * Test getCount method with valid values
     */
    @Test
    public void testGetCountValidValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('z'));
    }

    /**
     * Test getCount method with invalid value
     */
    @Test
    public void testGetCountInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(0, frequency.getCount(2));
    }

    /**
     * Test getPct method with valid values
     */
    @Test
    public void testGetPctValidValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct('a'), 0.001);
        assertEquals(0.16666666666666667, frequency.getPct('z'), 0.0000001);
    }

    /**
     * Test getPct method with invalid value
     */
    @Test
    public void testGetPctInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(Double.NaN, frequency.getPct(2));
    }

    /**
     * Test getCumFreq method with valid values
     */
    @Test
    public void testGetCumFreqValidValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq('z'));
    }

    /**
     * Test getCumFreq method with invalid value
     */
    @Test
    public void testGetCumFreqInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(0, frequency.getCumFreq(2));
    }

    /**
     * Test getCumPct method with valid values
     */
    @Test
    public void testGetCumPctValidValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct('a'), 0.001);
        assertEquals(0.75, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct('z'), 0.001);
    }

    /**
     * Test getCumPct method with invalid value
     */
    @Test
    public void testGetCumPctInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('z');
        assertEquals(0.0, frequency.getCumPct(2), 0.001);
    }
}