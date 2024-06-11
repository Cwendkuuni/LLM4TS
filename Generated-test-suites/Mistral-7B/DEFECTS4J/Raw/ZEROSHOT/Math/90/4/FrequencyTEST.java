package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    /**
     * Test addValue method with int.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(Long.valueOf(1), frequency.getCount(5));
        assertEquals(1, frequency.getSumFreq());
        frequency.addValue(5);
        assertEquals(Long.valueOf(2), frequency.getCount(5));
        assertEquals(2, frequency.getSumFreq());
    }

    /**
     * Test addValue method with long.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(5L);
        assertEquals(Long.valueOf(1), frequency.getCount(5L));
        assertEquals(1, frequency.getSumFreq());
        frequency.addValue(5L);
        assertEquals(Long.valueOf(2), frequency.getCount(5L));
        assertEquals(2, frequency.getSumFreq());
    }

    /**
     * Test addValue method with String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueString() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
    }

    /**
     * Test addValue method with char.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(Character.valueOf('a'), frequency.getCount('a'));
        assertEquals(1, frequency.getSumFreq());
        frequency.addValue('a');
        assertEquals(Character.valueOf('a'), frequency.getCount('a'));
        assertEquals(2, frequency.getSumFreq());
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(Long.valueOf(1), frequency.getCount(5));
        assertEquals(1, frequency.getSumFreq());
        frequency.clear();
        assertEquals(Long.valueOf(0), frequency.getCount(5));
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        assertEquals(Long.valueOf(0), frequency.getCount(5));
        assertEquals(Long.valueOf(0), frequency.getCount(6));
        assertEquals(0, frequency.getSumFreq());
        frequency.addValue(5);
        assertEquals(Long.valueOf(1), frequency.getCount(5));
        assertEquals(Long.valueOf(1), frequency.getCount(6));
        assertEquals(2, frequency.getSumFreq());
    }

    /**
     * Test getCount method with int.
     */
    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(Long.valueOf(1), frequency.getCount(5));
        assertEquals(0, frequency.getCount(6));
    }

    /**
     * Test getCount method with long.
     */
    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(5L);
        assertEquals(Long.valueOf(1), frequency.getCount(5L));
        assertEquals(0, frequency.getCount(6L));
    }

    /**
     * Test getCount method with char.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(Character.valueOf('a'), frequency.getCount('a'));
        assertEquals(Character.valueOf('a'), frequency.getCount('a'));
        assertEquals(Long.valueOf(2), frequency.getSumFreq());
    }

    /**
     * Test getCount method with non-existent value.
     */
    @Test
    public void testGetCountNonExistentValue() {
        Frequency frequency = new Frequency();
        assertEquals(Long.valueOf(0), frequency.getCount(5));
    }

    /**
     * Test getPct method with int.
     */
    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(6);
        assertEquals(0.3333333333333333, frequency.getPct(5), 0.0000001);
    }

    /**
     * Test getPct method with long.
     */
    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(5L);
        frequency.addValue(6L);
        assertEquals(0.3333333333333333, frequency.getPct(5L), 0.0000001);
    }

    /**
     * Test getPct method with char.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct('a'), 0.0000001);
    }

    /**
     * Test getPct method with non-existent value.
     */
    @Test
    public void testGetPctNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(6);
        assertEquals(Double.NaN, frequency.getPct(7), 0.0000001);
    }

    /**
     * Test getCumFreq method with int.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(Long.valueOf(1), frequency.getCumFreq(5));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(5));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(6));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(7));
    }

    /**
     * Test getCumFreq method with long.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(5L);
        assertEquals(Long.valueOf(1), frequency.getCumFreq(5L));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(5L));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(6L));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(7L));
    }

    /**
     * Test getCumFreq method with char.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(Character.valueOf('a'), frequency.getCumFreq('a'));
        assertEquals(Character.valueOf('a'), frequency.getCumFreq('a'));
        assertEquals(Character.valueOf('a'), frequency.getCumFreq('b'));
        assertEquals(Character.valueOf('a'), frequency.getCumFreq('c'));
    }

    /**
     * Test getCumFreq method with non-existent value.
     */
    @Test
    public void testGetCumFreqNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(Long.valueOf(0), frequency.getCumFreq(6));
    }

    /**
     * Test getCumPct method with int.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(6);
        assertEquals(0.3333333333333333, frequency.getPct(5), 0.0000001);
        assertEquals(0.6666666666666667, frequency.getCumPct(6), 0.0000001);
    }

    /**
     * Test getCumPct method with long.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(5L);
        frequency.addValue(6L);
        assertEquals(0.3333333333333333, frequency.getPct(5L), 0.0000001);
        assertEquals(0.6666666666666667, frequency.getCumPct(6L), 0.0000001);
    }

    /**
     * Test getCumPct method with char.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct('a'), 0.0000001);
        assertEquals(1, frequency.getCumPct('b'), 0.0000001);
    }

    /**
     * Test getCumPct method with non-existent value.
     */
    @Test
    public void testGetCumPctNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(6);
        assertEquals(0.3333333333333333, frequency.getPct(5), 0.0000001);
        assertEquals(0.6666666666666667, frequency.getCumPct(6), 0.0000001);
        assertEquals(1, frequency.getCumPct(7), 0.0000001);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(6);
        frequency.addValue(5);
        Iterator iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(5, ((Number) iterator.next()).longValue());
        assertTrue(iterator.hasNext());
        assertEquals(6, ((Number) iterator.next()).longValue());
        assertFalse(iterator.hasNext());
    }
}