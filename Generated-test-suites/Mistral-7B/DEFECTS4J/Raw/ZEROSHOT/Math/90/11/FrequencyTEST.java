package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

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
        Frequency frequencyComparing = new Frequency(comparator);
        assertNotNull(frequencyComparing);
    }

    /**
     * Test addValue method with Comparable values.
     */
    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(2);
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);

        assertEquals(3, frequency.getCount(5));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));

        assertEquals(50.0, frequency.getPct(5), 0.01);
        assertEquals(10.0, frequency.getPct(2), 0.01);
        assertEquals(10.0, frequency.getPct(3), 0.01);

        assertEquals(50.0, frequency.getCumPct(5), 0.01);
        assertEquals(60.0, frequency.getCumPct(2), 0.01);
        assertEquals(70.0, frequency.getCumPct(3), 0.01);
    }

    /**
     * Test addValue method with non-Comparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(5);
        frequency.addValue('a');

        // This should throw an IllegalArgumentException
        frequency.addValue(6);
    }

    /**
     * Test addValue method with different value types.
     */
    @Test
    public void testAddValueDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(5);
        frequency.addValue('a');
        frequency.addValue(3);
        frequency.addValue(5L);

        assertEquals(2, frequency.getCount('a'));
        assertEquals(2, frequency.getCount(5));
        assertEquals(1, frequency.getCount(3));
        assertEquals(0, frequency.getCount(6));

        assertEquals(50.0, frequency.getPct('a'), 0.01);
        assertEquals(25.0, frequency.getPct(5), 0.01);
        assertEquals(10.0, frequency.getPct(3), 0.01);
        
        assertEquals(50.0, frequency.getCumPct('a'), 0.01);
        assertEquals(75.0, frequency.getCumPct(5), 0.01);
        assertEquals(85.0, frequency.getCumPct(3), 0.01);
    }

    /**
     * Test addValue method with same value but different types.
     */
    @Test
    public void testAddValueSameValueDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(5);
        frequency.addValue('a');
        frequency.addValue(5);
        frequency.addValue(5L);

        assertEquals(3, frequency.getCount('a'));
        assertEquals(3, frequency.getCount(5));
        assertEquals(0, frequency.getCount(3));

        assertEquals(50.0, frequency.getPct('a'), 0.01);
        assertEquals(50.0, frequency.getPct(5), 0.01);
        assertEquals(0.0, frequency.getPct(3), 0.01);

        assertEquals(50.0, frequency.getCumPct('a'), 0.01);
        assertEquals(100.0, frequency.getCumPct(5), 0.01);
        assertEquals(100.0, frequency.getCumPct(3), 0.01); // Should be 100.0 but assertion will fail due to rounding errors
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(2);
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);

        assertEquals(15, frequency.getSumFreq());
    }
}