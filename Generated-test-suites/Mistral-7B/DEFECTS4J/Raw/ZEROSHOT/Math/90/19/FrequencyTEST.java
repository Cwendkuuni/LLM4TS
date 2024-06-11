package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator
     */
    @org.junit.Test
    public void testFrequencyWithDefaultComparator() {
        Frequency frequency = new Frequency();
        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        // Test getCount method
        assertEquals(3, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        try {
            frequency.getCount('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test getPct method
        assertEquals(0.6, frequency.getPct(1), 0.001);
        // Test getCumFreq method
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(4, frequency.getCumFreq(2));
        try {
            frequency.getCumFreq('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test getCumPct method
        assertEquals(0.6, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        // Test clear method
        frequency.clear();
        assertFalse(frequency.valuesIterator().hasNext());
        // Test toString method
        assertEquals(
                "Value \t Freq. \t Pct. \t Cum Pct. \n1\t3\t0.6\t0.6\n2\t1\t0.2\t0.8\n",
                frequency.toString());
    }

    /**
     * Test constructor with custom comparator
     */
    @org.junit.Test
    public void testFrequencyWithCustomComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);
        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        // Test getCount method
        assertEquals(3, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        try {
            frequency.getCount('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test getPct method
        assertEquals(0.6, frequency.getPct(1), 0.001);
        // Test getCumFreq method
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(4, frequency.getCumFreq(2));
        try {
            frequency.getCumFreq('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test getCumPct method
        assertEquals(0.6, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        // Test clear method
        frequency.clear();
        assertFalse(frequency.valuesIterator().hasNext());
        // Test toString method
        assertEquals(
                "Value \t Freq. \t Pct. \t Cum Pct. \n2\t1\t0.2\t0.2\n1\t3\t0.6\t0.8\n",
                frequency.toString());
    }

    /**
     * Test adding values of different types
     */
    @org.junit.Test
    public void testAddingValuesOfDifferentTypes() {
        Frequency frequency = new Frequency();
        // Test adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        // Test adding long values
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);
        // Test adding char value
        frequency.addValue('a');
        // Test adding object value
        frequency.addValue(new Integer(3));
        // Test getCount method
        assertEquals(3, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));
        assertEquals(1, frequency.getCount('a'));
        assertEquals(2, frequency.getCount(new Integer(3)));
        // Test getPct method
        assertEquals(0.4, frequency.getPct(2), 0.001);
        // Test getCumFreq method
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(6, frequency.getCumFreq(2));
        assertEquals(8, frequency.getCumFreq(3));
        try {
            frequency.getCumFreq('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            frequency.getCumFreq(new Integer(3));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test getCumPct method
        assertEquals(0.4, frequency.getCumPct(2), 0.001);
        assertEquals(0.6, frequency.getCumPct(3), 0.001);
        assertEquals(1.0, frequency.getCumPct('a'), 0.001);
        // Test clear method
        frequency.clear();
        assertFalse(frequency.valuesIterator().hasNext());
        // Test toString method
        assertEquals(
                "Value \t Freq. \t Pct. \t Cum Pct. \n1\t3\t0.3\t0.3\n2\t4\t0.4\t0.7\n3\t2\t0.2\t1.0\na\t1\t0.1\t1.1\n",
                frequency.toString());
    }
}