package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Tests for the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test case for adding values to an empty Frequency object.
     */
    @Test
    public void testEmptyFrequency() {
        Frequency freq = new Frequency();
        // add values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);
        // test size
        assertEquals("Size not correct", 6, freq.values().size());
        // test getCount
        assertEquals("Count of 1 is not 2", 2, freq.getCount(1));
        assertEquals("Count of 2 is not 2", 2, freq.getCount(2));
        assertEquals("Count of 3 is not 2", 2, freq.getCount(3));
        // test getPct
        assertEquals("Percentage of 1 is not 1/3", 0.3333, freq.getPct(1), 0.001);
        assertEquals("Percentage of 2 is not 1/3", 0.3333, freq.getPct(2), 0.001);
        assertEquals("Percentage of 3 is not 1/3", 0.3333, freq.getPct(3), 0.001);
        // test getCumFreq
        assertEquals("Cumulative frequency of 1 is not 2", 2, freq.getCumFreq(1));
        assertEquals("Cumulative frequency of 2 is not 4", 4, freq.getCumFreq(2));
        assertEquals("Cumulative frequency of 3 is not 6", 6, freq.getCumFreq(3));
        // test getCumPct
        assertEquals("Cumulative percentage of 1 is not 1/3", 0.3333, freq.getCumPct(1), 0.001);
        assertEquals("Cumulative percentage of 2 is not 2/3", 0.6667, freq.getCumPct(2), 0.001);
        assertEquals("Cumulative percentage of 3 is not 1", 1, freq.getCumPct(3), 0.001);
    }

    /**
     * Test case for adding values to a Frequency object with a Comparator.
     */
    @Test
    public void testComparatorFrequency() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency freq = new Frequency(comparator);
        // add values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);
        // test size
        assertEquals("Size not correct", 6, freq.values().size());
        // test getCount
        assertEquals("Count of 1 is not 2", 2, freq.getCount(1));
        assertEquals("Count of 2 is not 2", 2, freq.getCount(2));
        assertEquals("Count of 3 is not 2", 2, freq.getCount(3));
        // test getPct
        assertEquals("Percentage of 1 is not 1/3", 0.3333, freq.getPct(1), 0.001);
        assertEquals("Percentage of 2 is not 1/3", 0.3333, freq.getPct(2), 0.001);
        assertEquals("Percentage of 3 is not 1/3", 0.3333, freq.getPct(3), 0.001);
        // test getCumFreq
        assertEquals("Cumulative frequency of 1 is not 2", 2, freq.getCumFreq(1));
        assertEquals("Cumulative frequency of 2 is not 4", 4, freq.getCumFreq(2));
        assertEquals("Cumulative frequency of 3 is not 6", 6, freq.getCumFreq(3));
        // test getCumPct
        assertEquals("Cumulative percentage of 1 is not 1/3", 0.3333, freq.getCumPct(1), 0.001);
        assertEquals("Cumulative percentage of 2 is not 2/3", 0.6667, freq.getCumPct(2), 0.001);
        assertEquals("Cumulative percentage of 3 is not 1", 1, freq.getCumPct(3), 0.001);
    }

    /**
     * Test case for adding different types of values to a Frequency object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDifferentTypes() {
        Frequency freq = new Frequency();
        // add values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);
        // add a char value
        try {
            freq.addValue('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test case for adding the same value multiple times to a Frequency object.
     */
    @Test
    public void testSameValueMultipleTimes() {
        Frequency freq = new Frequency();
        // add values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        // test size
        assertEquals("Size not correct", 3, freq.values().size());
        // test getCount
        assertEquals("Count of 1 is not 3", 3, freq.getCount(1));
        // test getPct
        assertEquals("Percentage of 1 is not 1", 1, freq.getPct(1), 0.001);
        // test getCumFreq
        assertEquals("Cumulative frequency of 1 is not 3", 3, freq.getCumFreq(1));
        // test getCumPct
        assertEquals("Cumulative percentage of 1 is not 1", 1, freq.getCumPct(1), 0.001);
    }

    /**
     * Test case for adding different values to a Frequency object.
     */
    @Test
    public void testDifferentValues() {
        Frequency freq = new Frequency();
        // add values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(4);
        freq.addValue(4);
        freq.addValue(5);
        freq.addValue(5);
        // test size
        assertEquals("Size not correct", 9, freq.values().size());
        // test getCount
        assertEquals("Count of 1 is not 2", 2, freq.getCount(1));
        assertEquals("Count of 2 is not 2", 2, freq.getCount(2));
        assertEquals("Count of 3 is not 2", 2, freq.getCount(3));
        assertEquals("Count of 4 is not 2", 2, freq.getCount(4));
        assertEquals("Count of 5 is not 2", 2, freq.getCount(5));
        // test getPct
        assertEquals("Percentage of 1 is not 1/9", 0.1111, freq.getPct(1), 0.001);
        assertEquals("Percentage of 2 is not 1/9", 0.1111, freq.getPct(2), 0.001);
        assertEquals("Percentage of 3 is not 1/9", 0.1111, freq.getPct(3), 0.001);
        assertEquals("Percentage of 4 is not 1/9", 0.1111, freq.getPct(4), 0.001);
        assertEquals("Percentage of 5 is not 1/9", 0.1111, freq.getPct(5), 0.001);
        // test getCumFreq
        assertEquals("Cumulative frequency of 1 is not 2", 2, freq.getCumFreq(1));
        assertEquals("Cumulative frequency of 2 is not 4", 4, freq.getCumFreq(2));
        assertEquals("Cumulative frequency of 3 is not 6", 6, freq.getCumFreq(3));
        assertEquals("Cumulative frequency of 4 is not 8", 8, freq.getCumFreq(4));
        assertEquals("Cumulative frequency of 5 is not 10", 10, freq.getCumFreq(5));
        // test getCumPct
        assertEquals("Cumulative percentage of 1 is not 1/9", 0.1111, freq.getCumPct(1), 0.001);
        assertEquals("Cumulative percentage of 2 is not 1/3", 0.3333, freq.getCumPct(2), 0.001);
        assertEquals("Cumulative percentage of 3 is not 2/3", 0.6667, freq.getCumPct(3), 0.001);
        assertEquals("Cumulative percentage of 4 is not 3/3", 0.8889, freq.getCumPct(4), 0.001);
        assertEquals("Cumulative percentage of 5 is not 1", 1, freq.getCumPct(5), 0.001);
    }

    /**
     * Test case for clearing a Frequency object.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        // add values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);
        // clear
        freq.clear();
        // test size
        assertEquals("Size not correct", 0, freq.values().size());
        // test getCount
        assertEquals("Count of 1 is not 0", 0, freq.getCount(1));
        // test getPct
        assertEquals("Percentage of 1 is not 0", 0, freq.getPct(1), 0.001);
        // test getCumFreq
        assertEquals("Cumulative frequency of 1 is not 0", 0, freq.getCumFreq(1));
        // test getCumPct
        assertEquals("Cumulative percentage of 1 is not 0", 0, freq.getCumPct(1), 0.001);
    }
}