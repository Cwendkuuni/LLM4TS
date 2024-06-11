package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * JUnit test file for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency comparatorFrequency = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFrequency);
    }

    /**
     * Test addValue methods.
     */
    @org.junit.Test
    public void testAddValue() {
        Frequency frequency = new Frequency();

        // Test int type
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(5, frequency.getCount(5));
        assertEquals(5.0 / 6.0, frequency.getPct(5), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(5), 0.001);

        // Test long type
        frequency.addValue(10L);
        frequency.addValue(10L);
        frequency.addValue(10L);
        assertEquals(10, frequency.getCount(10));
        assertEquals(1.0 / 3.0, frequency.getPct(10), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(10), 0.001);

        // Test char type
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(2, frequency.getCount('A'));
        assertEquals(2.0 / 4.0, frequency.getPct('A'), 0.001);
        assertEquals(1.0 / 2.0, frequency.getCumPct('A'), 0.001);

        // Test String type
        frequency.addValue("hello");
        frequency.addValue("hello");
        assertEquals(2, frequency.getCount("hello"));
        assertEquals(2.0 / 4.0, frequency.getPct("hello"), 0.001);
        assertEquals(1.0, frequency.getCumPct("hello"), 0.001);

        // Test addValue with invalid argument type
        try {
            frequency.addValue(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test addValue with non-comparable arguments
        try {
            frequency.addValue(5);
            frequency.addValue('A');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test addValue with null argument
        try {
            frequency.addValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test getCount methods.
     */
    @org.junit.Test
    public void testGetCount() {
        Frequency frequency = new Frequency();

        // Test int type
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(5, frequency.getCount(5));

        // Test long type
        frequency.addValue(10L);
        frequency.addValue(10L);
        frequency.addValue(10L);
        assertEquals(10, frequency.getCount(10));

        // Test char type
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(2, frequency.getCount('A'));

        // Test String type
        frequency.addValue("hello");
        frequency.addValue("hello");
        assertEquals(2, frequency.getCount("hello"));

        // Test getCount with null argument
        try {
            frequency.getCount(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // Test getCount with non-comparable arguments
        try {
            frequency.getCount(5);
            frequency.addValue('A');
            frequency.getCount(5);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }

    /**
     * Test getPct methods.
     */
    @org.junit.Test
    public void testGetPct() {
        Frequency frequency = new Frequency();

        // Test int type
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(5.0 / 6.0, frequency.getPct(5), 0.001);

        // Test long type
        frequency.addValue(10L);
        frequency.addValue(10L);
        frequency.addValue(10L);
        assertEquals(1.0 / 3.0, frequency.getPct(10), 0.001);

        // Test char type
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(2.0 / 4.0, frequency.getPct('A'), 0.001);

        // Test String type
        frequency.addValue("hello");
        frequency.addValue("hello");
        assertEquals(2.0 / 4.0, frequency.getPct("hello"), 0.001);

        // Test getPct with empty frequency distribution
        frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(5), 0.0);
        assertEquals(0.0, frequency.getPct(10), 0.0);
        assertEquals(0.0, frequency.getPct('A'), 0.0);
        assertEquals(0.0, frequency.getPct("hello"), 0.0);
    }

    /**
     * Test getCumPct methods.
     */
    @org.junit.Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();

        // Test int type
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(1.0 / 3.0, frequency.getCumPct(5), 0.001);
        assertEquals(1.0, frequency.getCumPct(5 + 5), 0.001);

        // Test long type
        frequency.addValue(10L);
        frequency.addValue(10L);
        frequency.addValue(10L);
        assertEquals(1.0 / 3.0, frequency.getCumPct(10), 0.001);
        assertEquals(1.0, frequency.getCumPct(10 + 10), 0.001);

        // Test char type
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(1.0 / 2.0, frequency.getCumPct('A'), 0.001);
        assertEquals(1.0, frequency.getCumPct('A' + 'A'), 0.001);

        // Test String type
        frequency.addValue("hello");
        frequency.addValue("hello");
        assertEquals(1.0, frequency.getCumPct("hello"), 0.001);
        assertEquals(1.0, frequency.getCumPct("hello" + "hello"), 0.001);

        // Test getCumPct with empty frequency distribution
        frequency = new Frequency();
        assertEquals(0.0, frequency.getCumPct(5), 0.0);
        assertEquals(0.0, frequency.getCumPct(10), 0.0);
        assertEquals(0.0, frequency.getCumPct('A'), 0.0);
        assertEquals(0.0, frequency.getCumPct("hello"), 0.0);
    }

    /**
     * Test clear method.
     */
    @org.junit.Test
     public void testClear() {
        Frequency frequency = new Frequency();

        // Add some values
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        // Test size after adding values
        assertEquals(5, frequency.getSumFreq());

        // Clear the frequency distribution
        frequency.clear();

        // Test size after clearing
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test valuesIterator method.
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();

        // Add some values
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        // Test valuesIterator returns correct values
        Iterator<Object> valuesIterator = frequency.valuesIterator();
        Object value;
        long count = 0;
        while (valuesIterator.hasNext()) {
            value = valuesIterator.next();
            assertEquals(5, frequency.getCount(value));
            count++;
        }
        assertEquals(3, count);
    }

    /**
     * Test getSumFreq method.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();

        // Test getSumFreq with empty frequency distribution
        assertEquals(0, frequency.getSumFreq());

        // Add some values
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        // Test getSumFreq returns correct value
        assertEquals(5, frequency.getSumFreq());
    }
}