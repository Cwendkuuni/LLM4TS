package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    /**
     * Tests Frequency constructor.
     */
    @Test
    public void testFrequencyConstruction() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.freqTable);

        Frequency comparatorFreq = new Frequency(new NaturalComparator());
        assertEquals(0, comparatorFreq.getSumFreq());
        assertNotNull(comparatorFreq.freqTable);
    }

    /**
     * Tests addValue method with validComparable object.
     */
    @Test
    public void testAddValueValidComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(4, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertNull(freq.getCount(3));

        assertEquals(0.75, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);

        long cumFreq = freq.getCumFreq(1);
        assertEquals(1, cumFreq);
        cumFreq = freq.getCumFreq(2);
        assertEquals(2, cumFreq);
        cumFreq = freq.getCumFreq(3);
        assertEquals(4, cumFreq);

        double cumPct = freq.getCumPct(1);
        assertEquals(0.25, cumPct, 0.001);
        cumPct = freq.getCumPct(2);
        assertEquals(0.5, cumPct, 0.001);
        cumPct = freq.getCumPct(3);
        assertEquals(1.0, cumPct, 0.001);
    }

    /**
     * Tests addValue method with invalidComparable object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidComparable() {
        Frequency freq = new Frequency();
        freq.addValue('a');
    }

    /**
     * Tests addValue method with invalidComparable objects of the same type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidComparableSameType() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue("1");
    }

    /**
     * Tests clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.freqTable);
    }

    /**
     * Tests valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        Iterator<Object> valuesIterator = freq.valuesIterator();
        assertTrue(valuesIterator.hasNext());
        assertEquals(1, valuesIterator.next().longValue());
        assertTrue(valuesIterator.hasNext());
        assertEquals(2, valuesIterator.next().longValue());
        assertTrue(valuesIterator.hasNext());
        assertEquals(1, valuesIterator.next().longValue());
        assertFalse(valuesIterator.hasNext());
    }

    /**
     * Tests getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(4, freq.getSumFreq());
    }

    /**
     * Tests getCount method with validComparable object.
     */
    @Test
    public void testGetCountValidComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(3, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertNull(freq.getCount(3));
    }

    /**
     * Tests getCount method with invalidComparable object.
     */
    @Test
    public void testGetCountInvalidComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertNull(freq.getCount('a'));
    }

    /**
     * Tests getPct method with validComparable object.
     */
    @Test
    public void testGetPctValidComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.75, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
    }

    /**
     * Tests getPct method with invalidComparable object.
     */
    @Test
    public void testGetPctInvalidComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertNull(freq.getPct('a'));
    }

    /**
     * Tests getCumFreq method with validComparable object.
     */
    @Test
    public void testGetCumFreqValidComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(4, freq.getCumFreq(3));
    }

    /**
     * Tests getCumFreq method with invalidComparable object.
     */
    @Test
    public void testGetCumFreqInvalidComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0, freq.getCumFreq('a'));
    }

    /**
     * Tests getCumPct method with validComparable object.
     */
    @Test
    public void testGetCumPctValidComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.25, freq.getCumPct(1), 0.001);
        assertEquals(0.5, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Tests getCumPct method with invalidComparable object.
     */
    @Test
    public void testGetCumPctInvalidComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.0, freq.getCumPct('a'), 0.001);
    }
}