package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

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
        // Add some values and check the result
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        // Check the size of the frequency table
        assertEquals("Size of the frequency table should be 4", 4, frequency.valuesIterator().size());
        // Check the frequency of each value
        assertEquals("Frequency of value 1 should be 2", 2, frequency.getCount(1));
        assertEquals("Frequency of value 1 should be 2", 2, frequency.getCount(1L));
        assertEquals("Frequency of value 1 should be 2", 2, frequency.getCount(Integer.valueOf(1)));
        assertEquals("Frequency of value 2 should be 2", 2, frequency.getCount(2));
        assertEquals("Frequency of value 2 should be 2", 2, frequency.getCount(2L));
        assertEquals("Frequency of value 2 should be 2", 2, frequency.getCount(Integer.valueOf(2)));
        // Check the sum of all frequencies
        assertEquals("Sum of all frequencies should be 4", 4, frequency.getSumFreq());
        // Check the percentage of values equal to 1
        assertEquals("Percentage of values equal to 1 should be 0.5", 0.5, frequency.getPct(1), 0.01);
        assertEquals("Percentage of values equal to 1 should be 0.5", 0.5, frequency.getPct(1L), 0.01);
        assertEquals("Percentage of values equal to 1 should be 0.5", 0.5, frequency.getPct(Integer.valueOf(1)), 0.01);
        // Check the percentage of values equal to 2
        assertEquals("Percentage of values equal to 2 should be 0.5", 0.5, frequency.getPct(2), 0.01);
        assertEquals("Percentage of values equal to 2 should be 0.5", 0.5, frequency.getPct(2L), 0.01);
        assertEquals("Percentage of values equal to 2 should be 0.5", 0.5, frequency.getPct(Integer.valueOf(2)), 0.01);
        // Check the cumulative frequency of values less than or equal to 1
        assertEquals("Cumulative frequency of values less than or equal to 1 should be 2", 2, frequency.getCumFreq(1));
        assertEquals("Cumulative frequency of values less than or equal to 1 should be 2", 2, frequency.getCumFreq(1L));
        assertEquals("Cumulative frequency of values less than or equal to 1 should be 2", 2, frequency.getCumFreq(Integer.valueOf(1)));
        // Check the cumulative frequency of values less than or equal to 2
        assertEquals("Cumulative frequency of values less than or equal to 2 should be 4", 4, frequency.getCumFreq(2));
        assertEquals("Cumulative frequency of values less than or equal to 2 should be 4", 4, frequency.getCumFreq(2L));
        assertEquals("Cumulative frequency of values less than or equal to 2 should be 4", 4, frequency.getCumFreq(Integer.valueOf(2)));
        // Check the cumulative percentage of values less than or equal to 1
        assertEquals("Cumulative percentage of values less than or equal to 1 should be 0.5", 0.5, frequency.getCumPct(1), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 1 should be 0.5", 0.5, frequency.getCumPct(1L), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 1 should be 0.5", 0.5, frequency.getCumPct(Integer.valueOf(1)), 0.01);
        // Check the cumulative percentage of values less than or equal to 2
        assertEquals("Cumulative percentage of values less than or equal to 2 should be 1", 1, frequency.getCumPct(2), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 2 should be 1", 1, frequency.getCumPct(2L), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 2 should be 1", 1, frequency.getCumPct(Integer.valueOf(2)), 0.01);
        // Clear the frequency table
        frequency.clear();
        // Check the size of the frequency table after clearing it
        assertEquals("Size of the frequency table should be 0", 0, frequency.valuesIterator().size());
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
        Frequency frequency = new Frequency(comparator);
        // Add some values and check the result
        frequency.addValue(2);
        frequency.addValue(1);
        // Check the size of the frequency table
        assertEquals("Size of the frequency table should be 2", 2, frequency.valuesIterator().size());
        // Check the frequency of each value
        assertEquals("Frequency of value 1 should be 1", 1, frequency.getCount(1));
        assertEquals("Frequency of value 1 should be 1", 1, frequency.getCount(1L));
        assertEquals("Frequency of value 1 should be 1", 1, frequency.getCount(Integer.valueOf(1)));
        assertEquals("Frequency of value 2 should be 1", 1, frequency.getCount(2));
        assertEquals("Frequency of value 2 should be 1", 1, frequency.getCount(2L));
        assertEquals("Frequency of value 2 should be 1", 1, frequency.getCount(Integer.valueOf(2)));
        // Check the sum of all frequencies
        assertEquals("Sum of all frequencies should be 2", 2, frequency.getSumFreq());
        // Check the percentage of values equal to 1
        assertEquals("Percentage of values equal to 1 should be 0.5", 0.5, frequency.getPct(1), 0.01);
        assertEquals("Percentage of values equal to 1 should be 0.5", 0.5, frequency.getPct(1L), 0.01);
        assertEquals("Percentage of values equal to 1 should be 0.5", 0.5, frequency.getPct(Integer.valueOf(1)), 0.01);
        // Check the percentage of values equal to 2
        assertEquals("Percentage of values equal to 2 should be 0.5", 0.5, frequency.getPct(2), 0.01);
        assertEquals("Percentage of values equal to 2 should be 0.5", 0.5, frequency.getPct(2L), 0.01);
        assertEquals("Percentage of values equal to 2 should be 0.5", 0.5, frequency.getPct(Integer.valueOf(2)), 0.01);
        // Check the cumulative frequency of values less than or equal to 1
        assertEquals("Cumulative frequency of values less than or equal to 1 should be 1", 1, frequency.getCumFreq(1));
        assertEquals("Cumulative frequency of values less than or equal to 1 should be 1", 1, frequency.getCumFreq(1L));
        assertEquals("Cumulative frequency of values less than or equal to 1 should be 1", 1, frequency.getCumFreq(Integer.valueOf(1)));
        // Check the cumulative frequency of values less than or equal to 2
        assertEquals("Cumulative frequency of values less than or equal to 2 should be 2", 2, frequency.getCumFreq(2));
        assertEquals("Cumulative frequency of values less than or equal to 2 should be 2", 2, frequency.getCumFreq(2L));
        assertEquals("Cumulative frequency of values less than or equal to 2 should be 2", 2, frequency.getCumFreq(Integer.valueOf(2)));
        // Check the cumulative percentage of values less than or equal to 1
        assertEquals("Cumulative percentage of values less than or equal to 1 should be 0.5", 0.5, frequency.getCumPct(1), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 1 should be 0.5", 0.5, frequency.getCumPct(1L), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 1 should be 0.5", 0.5, frequency.getCumPct(Integer.valueOf(1)), 0.01);
        // Check the cumulative percentage of values less than or equal to 2
        assertEquals("Cumulative percentage of values less than or equal to 2 should be 1", 1, frequency.getCumPct(2), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 2 should be 1", 1, frequency.getCumPct(2L), 0.01);
        assertEquals("Cumulative percentage of values less than or equal to 2 should be 1", 1, frequency.getCumPct(Integer.valueOf(2)), 0.01);
        // Clear the frequency table
        frequency.clear();
        // Check the size of the frequency table after clearing it
        assertEquals("Size of the frequency table should be 0", 0, frequency.valuesIterator().size());
    }

    /**
     * Test adding values of different types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValuesOfDifferentTypes() {
        Frequency frequency = new Frequency();
        // Add some integer values
        frequency.addValue(1);
        frequency.addValue(1);
        // Add a char value
        frequency.addValue('a');
        // This should throw an IllegalArgumentException
        frequency.addValue('b');
    }

    /**
     * Test adding values of the same type but not comparable.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValuesNotComparable() {
        Frequency frequency = new Frequency();
        // Add some values of the same type but not comparable
        frequency.addValue("apple");
        frequency.addValue("banana");
        // This should throw an IllegalArgumentException
        frequency.addValue("orange");
    }
}