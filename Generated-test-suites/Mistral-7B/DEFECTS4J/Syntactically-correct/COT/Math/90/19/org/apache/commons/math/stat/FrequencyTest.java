package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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

        Frequency comparatorFrequency = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFrequency);
    }

    /**
     * Test addValue method with Integer values.
     */
    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    /**
     * Test addValue method with Long values.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(2, count);
    }

    /**
     * Test addValue method with Char values.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        long count = frequency.getCount('A');
        assertEquals(2, count);
    }

    /**
     * Test addValue method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue(1);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        long count = frequency.getCount(1);
        assertEquals(0, count);
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long sumFreq = frequency.getSumFreq();
        assertEquals(2, sumFreq);
    }

    /**
     * Test getCount method with valid arguments.
     */
    @Test
    public void testGetCountValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    /**
     * Test getCount method with invalid argument.
     */
    @Test
    public void testGetCountInvalidArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertNull(frequency.getCount("A"));
    }

    /**
     * Test getPct method with valid arguments.
     */
    @Test
    public void testGetPctValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertEquals(0.5, pct, 0.01);
    }

    /**
     * Test getPct method with invalid argument.
     */
    @Test
    public void testGetPctInvalidArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertNull(frequency.getPct("A"));
    }

    /**
     * Test getCumFreq method with valid arguments.
     */
    @Test
    public void testGetCumFreqValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);

        frequency.addValue(2);
        cumFreq = frequency.getCumFreq(1);
        assertEquals(2, cumFreq);
    }

    /**
     * Test getCumFreq method with invalid argument.
     */
    @Test
    public void testGetCumFreqInvalidArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertNull(frequency.getCumFreq("A"));
    }

    /**
     * Test getCumPct method with valid arguments.
     */
    @Test
    public void testGetCumPctValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.5, cumPct, 0.01);

        frequency.addValue(2);
        cumPct = frequency.getCumPct(1);
        assertEquals(0.6, cumPct, 0.01);
    }

    /**
     * Test getCumPct method with invalid argument.
     */
    @Test
    public void testGetCumPctInvalidArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertNull(frequency.getCumPct("A"));
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        Iterator<Long> iterator = frequency.valuesIterator();
        long value = iterator.next();
        assertEquals(1, value.longValue());
        value = iterator.next();
        assertEquals(1, value.longValue());
        assertFalse(iterator.hasNext());
    }
}