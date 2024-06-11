package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math.stat.Frequency;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        // Add some values and test getters
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(0.5, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getPct(3), 0.001); // should return 0
        assertEquals(0.25, freq.getCumPct(1), 0.001);
        assertEquals(0.5, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
        // Clear and test addValue with negative value
        freq.clear();
        try {
            freq.addValue(-1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test addValue with non-comparable value
        try {
            freq.addValue('a');
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with custom comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency freq = new Frequency(comparator);
        // Add some values and test getters
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(2));
        assertEquals(2, freq.getCount(1)); // should be 0 with default comparator
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getPct(1), 0.001); // should be 0 with default comparator
        assertEquals(0.5, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
    }
}