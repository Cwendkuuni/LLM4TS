package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Comparator comparator = new NaturalComparator();
        Frequency frequencyWithComparator = new Frequency(comparator);
        assertNotNull(frequencyWithComparator);
    }

    /**
     * Test addValue method with Integer values.
     */
    @Test
    public void testAddValue_Integer() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        // Test getCount method
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));

        // Test getSumFreq method
        assertEquals(4, frequency.getSumFreq());

        // Test getCount method with negative value
        assertEquals(0, frequency.getCount(-1));

        // Test addValue method with duplicate value
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));

        // Test getCumFreq method
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(4, frequency.getCumFreq(2));

        // Test getCumPct method
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);

        // Test toString method
        String actual = frequency.toString();
        String expected = "Value  \t Freq.  \t Pct.  \t Cum Pct.\n1 \t 3 \t 0.75 \t 0.75\n2 \t 1 \t 0.25 \t 1.00\n";
        assertEquals(expected, actual);
    }

    /**
     * Test addValue method with Long values.
     */
    @Test
    public void testAddValue_Long() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);

        // Test getCount method
        assertEquals(2, frequency.getCount(1L));
        assertEquals(1, frequency.getCount(2L));

        // Test getSumFreq method
        assertEquals(4, frequency.getSumFreq());

        // Test getCount method with negative value
        assertEquals(0, frequency.getCount(-1L));

        // Test addValue method with duplicate value
        frequency.addValue(1L);
        assertEquals(3, frequency.getCount(1L));

        // Test getCumFreq method
        assertEquals(2, frequency.getCumFreq(1L));
        assertEquals(4, frequency.getCumFreq(2L));

        // Test getCumPct method
        assertEquals(0.5, frequency.getCumPct(1L), 0.001);
        assertEquals(1.0, frequency.getCumPct(2L), 0.001);

        // Test toString method
        String actual = frequency.toString();
        String expected = "Value  \t Freq.  \t Pct.  \t Cum Pct.\n1 \t 3 \t 0.75 \t 0.75\n2 \t 1 \t 0.25 \t 1.00\n";
        assertEquals(expected, actual);
    }

    /**
     * Test addValue method with Character values.
     */
    @Test
    public void testAddValue_Character() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        // Test getCount method
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));

        // Test getSumFreq method
        assertEquals(3, frequency.getSumFreq());

        // Test getCount method with negative char value
        assertEquals(0, frequency.getCount('\u0001'));

        // Test addValue method with duplicate char value
        frequency.addValue('a');
        assertEquals(3, frequency.getCount('a'));

        // Test getCumFreq method
        assertEquals(2, frequency.getCumFreq('a'));
        assertEquals(3, frequency.getCumFreq('b'));

        // Test getCumPct method
        assertEquals(0.6666666666666667, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('b'), 0.001);

        // Test toString method
        String actual = frequency.toString();
        String expected = "Value  \t Freq.  \t Pct.  \t Cum Pct.\n'a' \t 3 \t 1.00 \t 1.00\n'b' \t 1 \t 0.3333333333333333 \t 1.3333333333333333\n";
        assertEquals(expected, actual);
    }

    /**
     * Test addValue method with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_Null() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();

        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
        assertEquals(0, frequency.getSumFreq());

        String actual = frequency.toString();
        assertEquals("", actual);
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertFalse(iterator.hasNext());
    }
}