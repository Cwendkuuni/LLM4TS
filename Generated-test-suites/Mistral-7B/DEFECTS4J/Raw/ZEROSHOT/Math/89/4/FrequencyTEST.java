package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.Comparator;

import org.junit.Test;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor without Comparator
     */
    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency instanceof Frequency);
        assertNotNull(frequency.freqTable);
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    /**
     * Test constructor with Comparator
     */
    @Test
    public void testFrequencyWithComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency instanceof Frequency);
        assertNotNull(frequency.freqTable);
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    /**
     * Test adding values without Comparator
     */
    @Test
    public void testAddValuesDefaultComparator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequenciesAreEqual(frequency, new int[] {1, 1, 2, 2});
        assertEquals(6, frequency.getSumFreq());
        assertEquals(4, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test adding values with Comparator
     */
    @Test
    public void testAddValuesCustomComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequenciesAreEqual(frequency, new int[] {1, 1, 2, 2});
        assertEquals(6, frequency.getSumFreq());
        assertEquals(4, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test adding values with the same value
     */
    @Test
    public void testAddValuesSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequenciesAreEqual(frequency, new int[] {1, 1});
        assertEquals(2, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test adding values with different types
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValuesDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
    }

    /**
     * Test adding values with different types, char as key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValuesDifferentTypesCharKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    /**
     * Test getCount method
     */
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(4, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test getCount method with different types
     */
    @Test(expected = ClassCastException.class)
    public void testGetCountDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.getCount('a');
    }

    /**
     * Test getCumFreq method
     */
    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(3));
        assertEquals(6, frequency.getCumFreq(4));
        assertEquals(6, frequency.getCumFreq(Double.MAX_VALUE));
    }

    /**
     * Test getCumFreq method with different types
     */
    @Test(expected = ClassCastException.class)
    public void testGetCumFreqDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.getCumFreq('a');
    }

    /**
     * Test getCumPct method
     */
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.16666666666666663, frequency.getPct(1), 0.000000001);
        assertEquals(0.3333333333333333, frequency.getPct(2), 0.000000001);
        assertEquals(0.5, frequency.getPct(3), 0.000000001);
        assertEquals(1.0, frequency.getPct(Double.MAX_VALUE), 0.000000001);
    }

    /**
     * Test getCumPct method with different types
     */
    @Test(expected = ClassCastException.class)
    public void testGetCumPctDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.getCumPct('a');
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertFalse(frequency.valuesIterator().hasNext());
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.16666666666666663 \t 0.16666666666666663\n2 \t 2 \t 0.3333333333333333 \t 0.5\n";
        String result = frequency.toString();
        assertEquals(expected, result);
    }

    /**
     * Helper method to check frequencies array against Frequency object
     *
     * @param frequency the Frequency object
     * @param frequencies expected frequencies array
     */
    private void frequenciesAreEqual(Frequency frequency, int[] frequencies) {
        Iterator values = frequency.valuesIterator();
        int i = 0;
        while (values.hasNext()) {
            long value = ((Long) values.next()).longValue();
            assertEquals(frequencies[i], frequency.getCount(value));
            i++;
        }
    }
}