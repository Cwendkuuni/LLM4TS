package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link Frequency} class from {@code org.apache.commons.math3.stat} package.
 */
public class FrequencyTest {

    /**
     * Test that default constructor works correctly.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertTrue(frequency.getClass().equals(Frequency.class));
        assertTrue(frequency.freqTable.isEmpty());
    }

    /**
     * Test that constructor with comparator works correctly.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator<Comparable<?>> naturalComparator = new NaturalComparator();
        Frequency frequency = new Frequency(naturalComparator);
        assertNotNull(frequency);
        assertTrue(frequency.getClass().equals(Frequency.class));
        assertTrue(frequency.freqTable.comparator().equals(naturalComparator));
        assertTrue(frequency.freqTable.isEmpty());
    }

    /**
     * Test that constructor with initial value works correctly.
     */
    @Test
    public void testInitialValueConstructor() {
        Frequency frequency = new Frequency(5);
        assertNotNull(frequency);
        assertTrue(frequency.getClass().equals(Frequency.class));
        assertEquals(5L, frequency.getCount(5));
        assertTrue(frequency.freqTable.size() == 1);
    }

    /**
     * Test that addValue method works correctly with int value.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3L, frequency.getCount(1));
        assertEquals(3.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(3.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(3.0, frequency.getSumFreq(), 0.0);
    }

    /**
     * Test that addValue method works correctly with long value.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        assertEquals(1L, frequency.getCount(1L));
        assertEquals(1.0 / 3.0, frequency.getPct(1L), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1L), 0.001);
        assertEquals(3.0, frequency.getSumFreq(), 0.0);
    }

    /**
     * Test that addValue method works correctly with char value.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(2L, frequency.getCount('a'));
        assertEquals(2.0 / 3.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(3.0, frequency.getSumFreq(), 0.0);
    }

    /**
     * Test that addValue method throws IllegalArgumentException when value is not comparable.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test that addValue method throws IllegalArgumentException when value is not comparable with previous entries.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableWithPreviousEntries() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    /**
     * Test that clear method works correctly.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertTrue(frequency.freqTable.isEmpty());
    }

    /**
     * Test that valuesIterator method works correctly.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Comparable<?>> valuesIterator = frequency.valuesIterator();
        assertTrue(valuesIterator.hasNext());
        assertEquals(1, ((Comparable<Comparable<?>>) valuesIterator.next()).compareTo(1));
        assertTrue(valuesIterator.hasNext());
        assertEquals(2, ((Comparable<Comparable<?>>) valuesIterator.next()).compareTo(2));
        assertFalse(valuesIterator.hasNext());
    }

    /**
     * Test that getSumFreq method works correctly.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3.0, frequency.getSumFreq(), 0.0);
    }

    /**
     * Test that getCount method works correctly with int value.
     */
    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3L, frequency.getCount(1));
    }

    /**
     * Test that getCount method works correctly with long value.
     */
    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        assertEquals(1L, frequency.getCount(1L));
    }

    /**
     * Test that getCount method works correctly with char value.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(2L, frequency.getCount('a'));
    }

    /**
     * Test that getCount method returns 0 when value is not comparable.
     */
    @Test
    public void testGetCountNotComparable() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(new Object()));
    }

    /**
     * Test that getCount method returns 0 when value is not comparable with previous entries.
     */
    @Test
    public void testGetCountNotComparableWithPreviousEntries() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test that getPct method works correctly with int value.
     */
    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(1.0 / 6.0, frequency.getPct(1), 0.001);
    }

    /**
     * Test that getPct method works correctly with long value.
     */
    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        assertEquals(1.0 / 6.0, frequency.getPct(1L), 0.001);
    }

    /**
     * Test that getPct method works correctly with char value.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(2.0 / 6.0, frequency.getPct('a'), 0.001);
    }

    /**
     * Test that getPct method returns NaN when no values have been added.
     */
    @Test
    public void testGetPctEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(1), 0.0);
        assertEquals(Double.NaN, frequency.getPct(1L), 0.0);
        assertEquals(Double.NaN, frequency.getPct('a'), 0.0);
    }

    /**
     * Test that getCumFreq method works correctly with int value.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1L, frequency.getCumFreq(1));
        assertEquals(2L, frequency.getCumFreq(2));
        assertEquals(3L, frequency.getCumFreq(3));
        assertEquals(3L, frequency.getCumFreq(4));
    }

    /**
     * Test that getCumFreq method works correctly with long value.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        assertEquals(1L, frequency.getCumFreq(1L));
        assertEquals(2L, frequency.getCumFreq(2L));
        assertEquals(3L, frequency.getCumFreq(3L));
        assertEquals(3L, frequency.getCumFreq(4L));
    }

    /**
     * Test that getCumFreq method works correctly with char value.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(1L, frequency.getCumFreq('a'));
        assertEquals(2L, frequency.getCumFreq('a'));
        assertEquals(2L, frequency.getCumFreq('b'));
        assertEquals(3L, frequency.getCumFreq('c'));
    }

    /**
     * Test that getCumFreq method returns 0 when value is not comparable.
     */
    @Test
    public void testGetCumFreqNotComparable() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCumFreq(new Object()));
    }

    /**
     * Test that getCumFreq method returns 0 when value is not comparable with previous entries.
     */
    @Test
    public void testGetCumFreqNotComparableWithPreviousEntries() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumFreq('a'));
    }

    /**
     * Test that getCumPct method works correctly with int value.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(1.0 / 6.0, frequency.getPct(1), 0.001);
        assertEquals(2.0 / 6.0, frequency.getCumPct(1), 0.001);
        assertEquals(3.0 / 6.0, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    /**
     * Test that getCumPct method works correctly with long value.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        assertEquals(1.0 / 6.0, frequency.getPct(1L), 0.001);
        assertEquals(2.0 / 6.0, frequency.getCumPct(1L), 0.001);
        assertEquals(3.0 / 6.0, frequency.getCumPct(2L), 0.001);
        assertEquals(1.0, frequency.getCumPct(3L), 0.001);
    }

    /**
     * Test that getCumPct method works correctly with char value.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(1.0 / 6.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 6.0, frequency.getCumPct('a'), 0.001);
        assertEquals(2.0 / 6.0, frequency.getCumPct('b'), 0.001);
        assertEquals(1.0, frequency.getCumPct('c'), 0.001);
    }

    /**
     * Test that getCumPct method returns NaN when no values have been added.
     */
    @Test
    public void testGetCumPctEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getCumPct(1), 0.0);
        assertEquals(Double.NaN, frequency.getCumPct(1L), 0.0);
        assertEquals(Double.NaN, frequency.getCumPct('a'), 0.0);
    }
}