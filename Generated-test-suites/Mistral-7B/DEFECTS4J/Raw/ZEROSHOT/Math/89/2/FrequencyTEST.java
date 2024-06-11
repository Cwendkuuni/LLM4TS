package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test class for the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq.freqTable);

        Frequency comparatorFreq = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFreq.freqTable);
    }

    /**
     * Test addValue method with int values.
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getCount(1));

        freq.addValue(2);
        freq.addValue(2);
        assertEquals(3, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
    }

    /**
     * Test addValue method with long values.
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        assertEquals(3, freq.getCount(1L));

        freq.addValue(2L);
        freq.addValue(2L);
        assertEquals(3, freq.getCount(1L));
        assertEquals(2, freq.getCount(2L));
    }

    /**
     * Test addValue method with char values.
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        assertEquals(2, freq.getCount('a'));
        assertEquals(1, freq.getCount('b'));
    }

    /**
     * Test addValue method with Object values.
     */
    @org.junit.Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getCount(1));

        freq.addValue(new Integer(1));
        freq.addValue(new Integer(1));
        freq.addValue(new Integer(1));
        assertEquals(3, freq.getCount(1));

        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        assertEquals(3, freq.getCount('a'));

        freq.addValue(new Character('a'));
        freq.addValue(new Character('a'));
        freq.addValue(new Character('a'));
        assertEquals(3, freq.getCount('a'));

        freq.addValue(new Integer(2));
        freq.addValue(new Integer(2));
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
    }

    /**
     * Test addValue method with non-comparable values.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    /**
     * Test clear method.
     */
    @org.junit.Test
     public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getCount(1));
    }

    /**
     * Test valuesIterator method.
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(2);
        Iterator<Comparable<?>> iterator = freq.valuesIterator();
        assertEquals(1, iterator.next().compareTo(1L));
        assertEquals(1, iterator.next().compareTo(1L));
        assertEquals(1, iterator.next().compareTo(1L));
        assertEquals(2, iterator.next().compareTo(2L));
        assertEquals(2, iterator.next().compareTo(2L));
        assertEquals(2, iterator.next().compareTo(2L));
    }

    /**
     * Test getSumFreq method.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
    }

    /**
     * Test getCount method with int values.
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getCount(1));
    }

    /**
     * Test getCount method with long values.
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        assertEquals(3, freq.getCount(1L));
    }

    /**
     * Test getCount method with char values.
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        assertEquals(3, freq.getCount('a'));
    }

    /**
     * Test getCount method with non-comparable values.
     */
    @org.junit.Test
    public void testGetCountNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        assertEquals(0, freq.getCount(1));
    }

    /**
     * Test getPct method with int values.
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(1.0 / 6.0, freq.getPct(1), 0.001);
        assertEquals(2.0 / 6.0, freq.getPct(2), 0.001);
    }

    /**
     * Test getPct method with long values.
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(2L);
        freq.addValue(2L);
        assertEquals(1.0 / 6.0, freq.getPct(1L), 0.001);
        assertEquals(2.0 / 6.0, freq.getPct(2L), 0.001);
    }

    /**
     * Test getPct method with char values.
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('b');
        freq.addValue('b');
        assertEquals(3.0 / 6.0, freq.getPct('a'), 0.001);
        assertEquals(3.0 / 6.0, freq.getPct('b'), 0.001);
    }

    /**
     * Test getPct method with non-comparable values.
     */
    @org.junit.Test
    public void testGetPctNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        assertEquals(Double.NaN, freq.getPct(1), 0.0);
    }

    /**
     * Test getCumFreq method with int values.
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(6, freq.getCumFreq(3));
    }

    /**
     * Test getCumFreq method with long values.
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(2L);
        freq.addValue(2L);
        assertEquals(1, freq.getCumFreq(1L));
        assertEquals(3, freq.getCumFreq(2L));
        assertEquals(6, freq.getCumFreq(3L));
    }

    /**
     * Test getCumFreq method with char values.
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('b');
        freq.addValue('b');
        assertEquals(1, freq.getCumFreq('a'));
        assertEquals(3, freq.getCumFreq('b'));
        assertEquals(6, freq.getCumFreq('c'));
    }

    /**
     * Test getCumFreq method with non-comparable values.
     */
    @org.junit.Test
    public void testGetCumFreqNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        assertEquals(0, freq.getCumFreq(1));
    }

    /**
     * Test getCumPct method with int values.
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test getCumPct method with long values.
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(2L);
        freq.addValue(2L);
        assertEquals(0.5, freq.getCumPct(1L), 0.001);
        assertEquals(1.0, freq.getCumPct(2L), 0.001);
        assertEquals(1.0, freq.getCumPct(3L), 0.001);
    }

    /**
     * Test getCumPct method with char values.
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('b');
        freq.addValue('b');
        assertEquals(0.5, freq.getCumPct('a'), 0.001);
        assertEquals(1.0, freq.getCumPct('b'), 0.001);
        assertEquals(1.0, freq.getCumPct('c'), 0.001);
    }

    /**
     * Test getCumPct method with non-comparable values.
     */
    @org.junit.Test
    public void testGetCumPctNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        assertEquals(0.0, freq.getCumPct(1), 0.0);
    }
}