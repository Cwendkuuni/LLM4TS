package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency frequencyWithComparator = new Frequency(new NaturalComparator());
        assertNotNull(frequencyWithComparator);
    }

    /**
     * Test method addValue
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();

        // Test addValue for int
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));

        // Test addValue for long
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1));

        // Test addValue for String
        frequency.addValue("test");
        assertEquals(0, frequency.getCount("test"));

        // Test addValue for uncomparable types
        try {
            frequency.addValue(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addValue for null
        try {
            frequency.addValue(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method addValue with existing value
     */
    @Test
    public void testAddValueWithExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test method addValue with different types
     */
    @Test
    public void testAddValueWithDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        assertEquals(1, frequency.getCount(1));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test method clear
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();

        assertEquals(0, frequency.getCount(1));
    }

    /**
     * Test method getSumFreq
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(2, frequency.getSumFreq());
    }

    /**
     * Test method getCount
     */
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);

        assertEquals(1, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    /**
     * Test method getCount with null value
     */
    @Test
    public void testGetCountWithNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);

        assertEquals(0, frequency.getCount(null));
    }

    /**
     * Test method getCount with different types
     */
    @Test
    public void testGetCountWithDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        assertEquals(1, frequency.getCount(1));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test method getPct
     */
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0.5, frequency.getPct(1), 0.01);
    }

    /**
     * Test method getPct with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPctWithNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);

        frequency.getPct(null);
    }

    /**
     * Test method getPct with uncomparable types
     */
    @Test
    public void testGetPctWithUncomparableTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");

        try {
            frequency.getPct('1');
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method getPct with different types
     */
    @Test
    public void testGetPctWithDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        assertEquals(1.0, frequency.getPct(1), 0.01);
        assertEquals(0.0, frequency.getPct('a'), 0.01);
    }

    /**
     * Test method getCumFreq
     */
    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    /**
     * Test method getCumFreq with null value
     */
    @Test
    public void testGetCumFreqWithNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);

        assertEquals(0, frequency.getCumFreq(null));
    }

    /**
     * Test method getCumFreq with uncomparable types
     */
    @Test
    public void testGetCumFreqWithUncomparableTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");

        assertEquals(0, frequency.getCumFreq('1'));
    }

    /**
     * Test method getCumFreq with different types
     */
    @Test
    public void testGetCumFreqWithDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1, frequency.getCumFreq(Character.valueOf('1')));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(2L));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('2')));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(3, frequency.getCumFreq(3L));
        assertEquals(3, frequency.getCumFreq(Character.valueOf('3')));

        assertEquals(1, frequency.getCumFreq(1.0));
        assertEquals(1, frequency.getCumFreq(1D));
        assertEquals(1, frequency.getCumFreq(1f));
        assertEquals(2, frequency.getCumFreq(2.0));
        assertEquals(2, frequency.getCumFreq(2D));
        assertEquals(2, frequency.getCumFreq(2f));
        assertEquals(3, frequency.getCumFreq(3.0));
        assertEquals(3, frequency.getCumFreq(3D));
        assertEquals(3, frequency.getCumFreq(3f));
    }

    /**
     * Test method getCumPct
     */
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0.5, frequency.getCumPct(1), 0.01);
        assertEquals(0.5, frequency.getCumPct(1L), 0.01);
        assertEquals(0.5, frequency.getCumPct(Character.valueOf('1')), 0.01);
        assertEquals(1.0, frequency.getCumPct(2), 0.01);
        assertEquals(1.0, frequency.getCumPct(2L), 0.01);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('2')), 0.01);
    }

    /**
     * Test method getCumPct with null value
     */
    @Test
    public void testGetCumPctWithNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);

        assertEquals(0.0, frequency.getCumPct(null), 0.01);
    }

    /**
     * Test method getCumPct with uncomparable types
     */
    @Test
    public void testGetCumPctWithUncomparableTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");

        assertEquals(0.0, frequency.getCumPct('1'), 0.01);
    }

    /**
     * Test method getCumPct with different types
     */
    @Test
    public void testGetCumPctWithDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        assertEquals(0.5, frequency.getCumPct(1), 0.01);
        assertEquals(0.5, frequency.getCumPct(1L), 0.01);
        assertEquals(0.5, frequency.getCumPct(Character.valueOf('1')), 0.01);
        assertEquals(1.0, frequency.getCumPct(2), 0.01);
        assertEquals(1.0, frequency.getCumPct(2L), 0.01);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('2')), 0.01);
        assertEquals(1.0, frequency.getCumPct(1.0), 0.01);
        assertEquals(1.0, frequency.getCumPct(1D), 0.01);
        assertEquals(1.0, frequency.getCumPct(1f), 0.01);
        assertEquals(2.0, frequency.getCumPct(3), 0.01);
        assertEquals(2.0, frequency.getCumPct(3L), 0.01);
        assertEquals(2.0, frequency.getCumPct(Character.valueOf('3')), 0.01);
        assertEquals(2.0, frequency.getCumPct(3.0), 0.01);
        assertEquals(2.0, frequency.getCumPct(3D), 0.01);
        assertEquals(2.0, frequency.getCumPct(3f), 0.01);
    }
}