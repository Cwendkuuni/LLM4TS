package org.apache.commons.math.stat.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Tests for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test of default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    /**
     * Test of constructor allowing values Comparator to be specified.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertEquals(comparator, frequency.getComparator());
    }

    /**
     * Test of toString method, returns a string representation of this frequency
     * distribution.
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        String result = frequency.toString();
        assertTrue(result.length() > 50);
    }

    /**
     * Test of addValue method, add 1 to the frequency count for v.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalid() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue('a');
    }

    /**
     * Test of addValue method, add 1 to the frequency count for v.
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, frequency.getCount(i));
        }
    }

    /**
     * Test of clear method, clears the frequency table.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test of valuesIterator method, returns an Iterator over the set of values that have been added.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        Iterator iterator = frequency.valuesIterator();
        for (int i = 0; i < 10; i++) {
            assertTrue(iterator.hasNext());
            Object value = iterator.next();
            assertEquals(Long.valueOf(i), value);
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test of getSumFreq method, returns the sum of all frequencies.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        assertEquals(55, frequency.getSumFreq());
    }

    /**
     * Test of getCount method, returns the number of values = v.
     */
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, frequency.getCount(i));
        }
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test of getPct method, returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     */
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            double pct = frequency.getPct(i);
            assertEquals(1.0 * (i + 1) / 10.0, pct, 0.001);
        }
        assertEquals(Double.NaN, frequency.getPct('a'));
    }

    /**
     * Test of getCumFreq method, returns the cumulative frequency of values less than or equal to v.
     */
    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            long cumFreq = frequency.getCumFreq(i);
            assertEquals(i + 1, cumFreq);
        }
        assertEquals(10, frequency.getCumFreq(10));
        assertEquals(0, frequency.getCumFreq(-1));
    }

    /**
     * Test of getCumPct method, returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     */
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            double cumPct = frequency.getCumPct(i);
            assertEquals((double) (i + 1) / 11.0, cumPct, 0.001);
        }
        assertEquals(1, frequency.getCumPct(10));
        assertEquals(0.0, frequency.getCumPct(-1));
    }
}