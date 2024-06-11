package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * Test class for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        
        Frequency comparatorFreq = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFreq);
    }

    /**
     * Test addValue method with comparable values.
     */
    @Test
    public void testAddValueComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));

        // test getCount with non-comparable value
        try {
            freq.getCount('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test getCount with null
        try {
            freq.getCount(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test getValue method with non-comparable value
        try {
            freq.getValue('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test addValue method with non-comparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue('a'); // should throw IllegalArgumentException
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.clear();

        assertEquals(0, freq.getSumFreq());
    }

    /**
     * Test getCount method with int values.
     */
    @Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
    }

    /**
     * Test getCount method with long values.
     */
    @Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(2, freq.getCount(1L));
        assertEquals(2, freq.getCount(2L));
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(4, freq.getSumFreq());
    }

    /**
     * Test getValues method.
     */
    @Test
    public void testGetValues() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        List<Object> values = new ArrayList<>(freq.values());
        assertEquals(4, values.size());
        assertTrue(values.contains(1));
        assertTrue(values.contains(1));
        assertTrue(values.contains(2));
        assertTrue(values.contains(2));
    }

    /**
     * Test getValues method with custom Comparator.
     */
    @Test
    public void testGetValuesComparator() {
        Frequency freq = new Frequency(new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });

        freq.addValue(2);
        freq.addValue(1);

        List<Object> values = new ArrayList<>(freq.values());
        assertEquals(2, values.size());
        assertTrue(values.get(0).equals(1));
        assertTrue(values.get(1).equals(2));
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.4 \t 0.4\n2 \t 2 \t 0.4 \t 0.8\n", freq.toString());
    }
}