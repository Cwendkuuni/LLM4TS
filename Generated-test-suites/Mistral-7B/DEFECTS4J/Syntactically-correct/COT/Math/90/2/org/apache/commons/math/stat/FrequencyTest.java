package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test cases for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor without comparator.
     */
    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testFrequencyWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n2 \t 1 \t 0.1666666666666667 \t 0.5 \n1 \t 1 \t 0.1666666666666667 \t 0.6666666666666667 \n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue(Object) method with valid arguments.
     */
    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Long> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        long expectedCount = 3;
        Long actualCount;
        while (iterator.hasNext()) {
            actualCount = iterator.next();
            assertEquals(expectedCount++, actualCount.longValue());
        }
    }

    /**
     * Test addValue(Object) method with invalid arguments.
     */
    @Test
    public void testAddValueObjectInvalid() {
        Frequency frequency = new Frequency();
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue("invalid"));
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object(){}));
        assertThrows(ClassCastException.class, () -> frequency.addValue(new Integer(1)).addValue('a'));
    }

    /**
     * Test addValue(int) method.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Long> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        long expectedCount = 3;
        Long actualCount;
        while (iterator.hasNext()) {
            actualCount = iterator.next();
            assertEquals(expectedCount++, actualCount.longValue());
        }
    }

    /**
     * Test addValue(char) method.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        Iterator<Long> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        long expectedCount = 3;
        Long actualCount;
        while (iterator.hasNext()) {
            actualCount = iterator.next();
            assertEquals(expectedCount++, actualCount.longValue());
        }
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long result = frequency.getSumFreq();
        long expected = 5;
        assertEquals(expected, result);
    }

    /**
     * Test getCount(Object) method with valid arguments.
     */
    @Test
    public void testGetCountObjectValid() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedCount = 3;
        long result = frequency.getCount(Long.valueOf(1));
        assertEquals(expectedCount, result);
    }

    /**
     * Test getCount(Object) method with invalid arguments.
     */
    @Test
    public void testGetCountObjectInvalid() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCount("invalid"));
        assertNull(frequency.getCount(new Object(){}));
        assertNull(frequency.getCount(new Integer(1)));
        assertNull(frequency.getCount('a'));
    }

    /**
     * Test getCount(int) method.
     */
    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedCount = 3;
        long result = frequency.getCount(1);
        assertEquals(expectedCount, result);
    }

    /**
     * Test getCount(char) method.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        long expectedCount = 3;
        long result = frequency.getCount('a');
        assertEquals(expectedCount, result);
    }

    /**
     * Test getPct(Object) method with valid arguments.
     */
    @Test
    public void testGetPctObjectValid() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        double expectedPct = 0.3333333333333333;
        double result = frequency.getPct(Long.valueOf(1));
        assertEquals(expectedPct, result, 0.0);
    }

    /**
     * Test getPct(Object) method with invalid arguments.
     */
    @Test
    public void testGetPctObjectInvalid() {
        Frequency frequency = new Frequency();
        assertDoubleNaNs(frequency.getPct("invalid"));
        assertDoubleNaNs(frequency.getPct(new Object(){}));
        assertDoubleNaNs(frequency.getPct(new Integer(1)));
        assertDoubleNaNs(frequency.getPct('a'));
    }

    /**
     * Test getPct(int) method.
     */
    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        double expectedPct = 0.3333333333333333;
        double result = frequency.getPct(1);
        assertEquals(expectedPct, result, 0.0);
    }

    /**
     * Test getPct(char) method.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        long sumFreq = frequency.getSumFreq();
        double expectedPct = 0.3333333333333333;
        double result = frequency.getPct('a');
        assertEquals(expectedPct, result, 0.0);
    }

    /**
     * Test getCumFreq(Object) method with valid arguments.
     */
    @Test
    public void testGetCumFreqObjectValid() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedCumFreq = 1;
        long result = frequency.getCumFreq(Long.valueOf(1));
        assertEquals(expectedCumFreq, result);
    }

    /**
     * Test getCumFreq(Object) method with invalid arguments.
     */
    @Test
    public void testGetCumFreqObjectInvalid() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumFreq("invalid"));
        assertNull(frequency.getCumFreq(new Object(){}));
        assertNull(frequency.getCumFreq(new Integer(1)));
        assertNull(frequency.getCumFreq('a'));
    }

    /**
     * Test getCumFreq(int) method.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedCumFreq = 1;
        long result = frequency.getCumFreq(1);
        assertEquals(expectedCumFreq, result);
    }

    /**
     * Test getCumFreq(char) method.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        long expectedCumFreq = 1;
        long result = frequency.getCumFreq('a');
        assertEquals(expectedCumFreq, result);
    }

    /**
     * Test getCumPct(Object) method with valid arguments.
     */
    @Test
    public void testGetCumPctObjectValid() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        double expectedCumPct = 0.3333333333333333;
        double result = frequency.getCumPct(Long.valueOf(1));
        assertEquals(expectedCumPct, result, 0.0);
    }

    /**
     * Test getCumPct(Object) method with invalid arguments.
     */
    @Test
    public void testGetCumPctObjectInvalid() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumPct("invalid"));
        assertNull(frequency.getCumPct(new Object(){}));
        assertNull(frequency.getCumPct(new Integer(1)));
        assertNull(frequency.getCumPct('a'));
    }

    /**
     * Test getCumPct(int) method.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        double expectedCumPct = 0.3333333333333333;
        double result = frequency.getCumPct(1);
        assertEquals(expectedCumPct, result, 0.0);
    }

    /**
     * Test getCumPct(char) method.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        long sumFreq = frequency.getSumFreq();
        double expectedCumPct = 0.3333333333333333;
        double result = frequency.getCumPct('a');
        assertEquals(expectedCumPct, result, 0.0);
    }

    /**
     * Helper method to check that the result is NaN.
     * @param obj the object to test
     */
    private void assertDoubleNaNs(Double obj) {
        assertTrue(Double.isNaN(obj));
    }
}