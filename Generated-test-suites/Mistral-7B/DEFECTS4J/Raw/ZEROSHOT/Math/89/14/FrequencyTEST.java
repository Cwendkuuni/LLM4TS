package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency.java.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertTrue(freq instanceof Frequency);
        assertNull(freq.comparator());
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testConstructorWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertTrue(freq instanceof Frequency);
        assertSame(comparator, freq.comparator());
    }

    /**
     * Test addValue method with int.
     */
    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        long result = freq.getCount(1);
        assertEquals(3, result);
    }

    /**
     * Test addValue method with long.
     */
    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        long result = freq.getCount(1L);
        assertEquals(3, result);
    }

    /**
     * Test addValue method with char.
     */
    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        long result = freq.getCount('a');
        assertEquals(3, result);
    }

    /**
     * Test addValue method with Integer.
     */
    @Test
    public void testAddValueInteger() {
        Frequency freq = new Frequency();
        freq.addValue(new Integer(1));
        freq.addValue(new Integer(1));
        freq.addValue(new Integer(1));
        long result = freq.getCount(new Integer(1));
        assertEquals(3, result);
    }

    /**
     * Test addValue method with Long.
     */
    @Test
    public void testAddValueLongObject() {
        Frequency freq = new Frequency();
        freq.addValue(new Long(1));
        freq.addValue(new Long(1));
        freq.addValue(new Long(1));
        long result = freq.getCount(new Long(1));
        assertEquals(3, result);
    }

    /**
     * Test addValue method with Character.
     */
    @Test
    public void testAddValueCharacter() {
        Frequency freq = new Frequency();
        freq.addValue(new Character('a'));
        freq.addValue(new Character('a'));
        freq.addValue(new Character('a'));
        long result = freq.getCount(new Character('a'));
        assertEquals(3, result);
    }

    /**
     * Test addValue method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidArgument() {
        Frequency freq = new Frequency();
        freq.addValue(new Double(1.0));
    }

    /**
     * Test addValue method with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullArgument() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertTrue(freq.getSumFreq() > 0);
        freq.clear();
        assertTrue(freq.getSumFreq() == 0);
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        Iterator iterator = freq.valuesIterator();
        long sumFreq = freq.getSumFreq();
        int count = 0;
        long value;
        while (iterator.hasNext()) {
            value = (Long) iterator.next();
            assertEquals(1, value.longValue());
            assertEquals(1, freq.getCount(value));
            count++;
        }
        assertEquals(sumFreq, count);
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        long result = freq.getSumFreq();
        assertEquals(3, result);
    }

    /**
     * Test getCount method with int.
     */
    @Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        long result = freq.getCount(1);
        assertEquals(3, result);
    }

    /**
     * Test getCount method with long.
     */
    @Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        long result = freq.getCount(1L);
        assertEquals(3, result);
    }

    /**
     * Test getCount method with char.
     */
    @Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        long result = freq.getCount('a');
        assertEquals(3, result);
    }

    /**
     * Test getCount method with invalid argument.
     */
    @Test
    public void testGetCountInvalidArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertNull(freq.getCount(new Double(1.0)));
    }

    /**
     * Test getCount method with null argument.
     */
    @Test
    public void testGetCountNullArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertNull(freq.getCount(null));
    }

    /**
     * Test getPct method with int.
     */
    @Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        double result = freq.getPct(1);
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    /**
     * Test getPct method with long.
     */
    @Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        double result = freq.getPct(1L);
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    /**
     * Test getPct method with char.
     */
    @Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        double result = freq.getPct('a');
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    /**
     * Test getPct method with invalid argument.
     */
    @Test
    public void testGetPctInvalidArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertNull(freq.getPct(new Double(1.0)));
    }

    /**
     * Test getPct method with null argument.
     */
    @Test
    public void testGetPctNullArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertNull(freq.getPct(null));
    }

    /**
     * Test getCumFreq method with int.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        long result = freq.getCumFreq(1);
        assertEquals(3, result);
    }

    /**
     * Test getCumFreq method with long.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        long result = freq.getCumFreq(1L);
        assertEquals(3, result);
    }

    /**
     * Test getCumFreq method with char.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        long result = freq.getCumFreq('a');
        assertEquals(3, result);
    }

    /**
     * Test getCumFreq method with invalid argument.
     */
    @Test
    public void testGetCumFreqInvalidArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertNull(freq.getCumFreq(new Double(1.0)));
    }

    /**
     * Test getCumFreq method with null argument.
     */
    @Test
    public void testGetCumFreqNullArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertNull(freq.getCumFreq(null));
    }

    /**
     * Test getCumPct method with int.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        double result = freq.getCumPct(1);
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    /**
     * Test getCumPct method with long.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        double result = freq.getCumPct(1L);
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    /**
     * Test getCumPct method with char.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        double result = freq.getCumPct('a');
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    /**
     * Test getCumPct method with invalid argument.
     */
    @Test
    public void testGetCumPctInvalidArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertNull(freq.getCumPct(new Double(1.0)));
    }

    /**
     * Test getCumPct method with null argument.
     */
    @Test
    public void testGetCumPctNullArgument() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertNull(freq.getCumPct(null));
    }
}