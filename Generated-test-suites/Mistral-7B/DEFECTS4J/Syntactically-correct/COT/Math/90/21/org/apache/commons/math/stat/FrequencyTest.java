package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency freq = new Frequency();
    }

    /**
     * Test constructor with Comparator.
     */
    @Test
    public void testConstructorWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        String result = freq.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.3333333333333333 \t 0.6666666666666666\n"
                + "Value: 1, Frequency: 2, Cumulative Frequency: 2, Cumulative Percentage: 0.3333333333333333\n"
                + "Value: 2, Frequency: 1, Cumulative Frequency: 3, Cumulative Percentage: 0.6666666666666666\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue for Object.
     */
    @Test
    public void testAddValueForObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("A");
        Object key = "A";
        long expectedCount = 1;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test addValue for int.
     */
    @Test
    public void testAddValueForInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        int key = 1;
        long expectedCount = 2;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test addValue for long.
     */
    @Test
    public void testAddValueForLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long key = 2;
        long expectedCount = 1;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test addValue for char.
     */
    @Test
    public void testAddValueForChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        char key = 'A';
        long expectedCount = 1;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test addValue for non-comparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueForNonComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('A');
        freq.addValue(2);
        freq.addValue('B');
        freq.addValue(3);
        Object key = 3;
        freq.addValue(key);
    }

    /**
     * Test addValue for negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueForNegativeValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(-1);
    }

    /**
     * Test addValue for null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueForNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(null);
    }

    /**
     * Test getSumFreq.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long expected = 3;
        long actual = freq.getSumFreq();
        assertEquals(expected, actual);
    }

    /**
     * Test getCount for Object.
     */
    @Test
    public void testGetCountForObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("A");
        Object key = "A";
        long expectedCount = 1;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test getCount for int.
     */
    @Test
    public void testGetCountForInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        int key = 1;
        long expectedCount = 2;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test getCount for long.
     */
    @Test
    public void testGetCountForLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long key = 2;
        long expectedCount = 1;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test getCount for char.
     */
    @Test
    public void testGetCountForChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        char key = 'A';
        long expectedCount = 1;
        long actualCount = freq.getCount(key);
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Test getCount for non-existent value.
     */
    @Test
    public void testGetCountForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long actualCount = freq.getCount(2);
        assertNull(actualCount);
    }

    /**
     * Test getPct for Object.
     */
    @Test
    public void testGetPctForObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("A");
        Object key = "A";
        double expectedPct = 0.3333333333333333;
        double actualPct = freq.getPct(key);
        assertEquals(expectedPct, actualPct, 0.0000000000001);
    }

    /**
     * Test getPct for int.
     */
    @Test
    public void testGetPctForInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        int key = 1;
        double expectedPct = 0.3333333333333333;
        double actualPct = freq.getPct(key);
        assertEquals(expectedPct, actualPct, 0.0000000000001);
    }

    /**
     * Test getPct for long.
     */
    @Test
    public void testGetPctForLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long key = 2;
        double expectedPct = 0.3333333333333333;
        double actualPct = freq.getPct(key);
        assertEquals(expectedPct, actualPct, 0.0000000000001);
    }

    /**
     * Test getPct for char.
     */
    @Test
    public void testGetPctForChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        char key = 'A';
        double expectedPct = 0.3333333333333333;
        double actualPct = freq.getPct(key);
        assertEquals(expectedPct, actualPct, 0.0000000000001);
    }

    /**
     * Test getPct for non-existent value.
     */
    @Test
    public void testGetPctForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        double actualPct = freq.getPct(2);
        assertNull(actualPct);
    }

    /**
     * Test getCumFreq for Object.
     */
    @Test
    public void testGetCumFreqForObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("A");
        Object key = "A";
        long expectedCumFreq = 1;
        long actualCumFreq = freq.getCumFreq(key);
        assertEquals(expectedCumFreq, actualCumFreq);
    }

    /**
     * Test getCumFreq for int.
     */
    @Test
    public void testGetCumFreqForInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        int key = 1;
        long expectedCumFreq = 1;
        long actualCumFreq = freq.getCumFreq(key);
        assertEquals(expectedCumFreq, actualCumFreq);
    }

    /**
     * Test getCumFreq for long.
     */
    @Test
    public void testGetCumFreqForLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long key = 2;
        long expectedCumFreq = 1;
        long actualCumFreq = freq.getCumFreq(key);
        assertEquals(expectedCumFreq, actualCumFreq);
    }

    /**
     * Test getCumFreq for char.
     */
    @Test
    public void testGetCumFreqForChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        char key = 'A';
        long expectedCumFreq = 1;
        long actualCumFreq = freq.getCumFreq(key);
        assertEquals(expectedCumFreq, actualCumFreq);
    }

    /**
     * Test getCumFreq for non-existent value.
     */
    @Test
    public void testGetCumFreqForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        long actualCumFreq = freq.getCumFreq(2);
        assertNull(actualCumFreq);
    }

    /**
     * Test getCumPct for Object.
     */
    @Test
    public void testGetCumPctForObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("A");
        Object key = "A";
        double expectedCumPct = 0.3333333333333333;
        double actualCumPct = freq.getCumPct(key);
        assertEquals(expectedCumPct, actualCumPct, 0.0000000000001);
    }

    /**
     * Test getCumPct for int.
     */
    @Test
    public void testGetCumPctForInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        int key = 1;
        double expectedCumPct = 0.3333333333333333;
        double actualCumPct = freq.getCumPct(key);
        assertEquals(expectedCumPct, actualCumPct, 0.0000000000001);
    }

    /**
     * Test getCumPct for long.
     */
    @Test
    public void testGetCumPctForLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long key = 2;
        double expectedCumPct = 0.6666666666666666;
        double actualCumPct = freq.getCumPct(key);
        assertEquals(expectedCumPct, actualCumPct, 0.0000000000001);
    }

    /**
     * Test getCumPct for char.
     */
    @Test
    public void testGetCumPctForChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        char key = 'A';
        double expectedCumPct = 0.3333333333333333;
        double actualCumPct = freq.getCumPct(key);
        assertEquals(expectedCumPct, actualCumPct, 0.0000000000001);
    }

    /**
     * Test getCumPct for non-existent value.
     */
    @Test
    public void testGetCumPctForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        double actualCumPct = freq.getCumPct(2);
        assertNull(actualCumPct);
    }

    /**
     * Test clear.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertFalse(freq.valuesIterator().hasNext());
    }
}