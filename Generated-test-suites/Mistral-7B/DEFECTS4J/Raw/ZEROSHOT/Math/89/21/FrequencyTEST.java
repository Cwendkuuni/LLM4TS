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

        Frequency frequencyComparator = new Frequency(new NaturalComparator());
        assertNotNull(frequencyComparator);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        String expected = "Value     Freq.     Pct.     Cum Pct.\n1         2       0.3333   0.3333\n2         2       0.3333   0.6667\n";
        String actual = frequency.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test addValue method with int values.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        long actual = frequency.getCount(1);
        long expected = 4;
        assertEquals(expected, actual);

        long sumFreq = frequency.getSumFreq();
        long expectedSumFreq = 4;
        assertEquals(expectedSumFreq, sumFreq);

        long actualCumFreq = frequency.getCumFreq(1);
        long expectedCumFreq = 4;
        assertEquals(expectedCumFreq, actualCumFreq);

        double actualPct = frequency.getPct(1);
        double expectedPct = 1.0;
        assertEquals(expectedPct, actualPct, 0.001);

        double actualCumPct = frequency.getCumPct(1);
        double expectedCumPct = 1.0;
        assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    /**
     * Test addValue method with long values.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();

        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        long actual = frequency.getCount(1L);
        long expected = 4;
        assertEquals(expected, actual);

        long sumFreq = frequency.getSumFreq();
        long expectedSumFreq = 4;
        assertEquals(expectedSumFreq, sumFreq);

        long actualCumFreq = frequency.getCumFreq(1L);
        long expectedCumFreq = 4;
        assertEquals(expectedCumFreq, actualCumFreq);

        double actualPct = frequency.getPct(1L);
        double expectedPct = 1.0;
        assertEquals(expectedPct, actualPct, 0.001);

        double actualCumPct = frequency.getCumPct(1L);
        double expectedCumPct = 1.0;
        assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    /**
     * Test addValue method with char values.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();

        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');

        long actual = frequency.getCount('A');
        long expected = 4;
        assertEquals(expected, actual);

        long sumFreq = frequency.getSumFreq();
        long expectedSumFreq = 4;
        assertEquals(expectedSumFreq, sumFreq);

        long actualCumFreq = frequency.getCumFreq('A');
        long expectedCumFreq = 4;
        assertEquals(expectedCumFreq, actualCumFreq);

        double actualPct = frequency.getPct('A');
        double expectedPct = 1.0;
        assertEquals(expectedPct, actualPct, 0.001);

        double actualCumPct = frequency.getCumPct('A');
        double expectedCumPct = 1.0;
        assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    /**
     * Test addValue method with Object values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueObjectFailedComparison() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue('A');

        try {
            frequency.addValue(1);
        } catch (IllegalArgumentException e) {
            // expected
            assertEquals("Value not comparable to existing values.", e.getMessage());
            throw e;
        }
    }

    /**
     * Test addValue method with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Frequency frequency = new Frequency();

        try {
            frequency.addValue(null);
        } catch (IllegalArgumentException e) {
            // expected
            assertEquals("Value must not be null", e.getMessage());
            throw e;
        }
    }

    /**
     * Test getCount method with int values.
     */
    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        long actual = frequency.getCount(1);
        long expected = 2;
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with long values.
     */
    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);

        long actual = frequency.getCount(1L);
        long expected = 2;
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with char values.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');

        long actual = frequency.getCount('A');
        long expected = 2;
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with null value.
     */
    @Test
    public void testGetCountNullValue() {
        Frequency frequency = new Frequency();

        long actual = frequency.getCount(null);
        long expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test getPct method with int values.
     */
    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        double actual = frequency.getPct(1);
        double expected = 0.3333;
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test getPct method with long values.
     */
    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        double actual = frequency.getPct(1L);
        double expected = 0.3333;
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test getPct method with char values.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');

        double actual = frequency.getPct('A');
        double expected = 0.5;
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test getPct method with null value.
     */
    @Test
    public void testGetPctNullValue() {
        Frequency frequency = new Frequency();

        double actual = frequency.getPct(null);
        double expected = Double.NaN;
        assertEquals(expected, actual);
    }

    /**
     * Test getCumFreq method with int values.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        long actual = frequency.getCumFreq(1);
        long expected = 2;
        assertEquals(expected, actual);

        long actual2 = frequency.getCumFreq(2);
        long expected2 = 4;
        assertEquals(expected2, actual2);
    }

    /**
     * Test getCumFreq method with long values.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        long actual = frequency.getCumFreq(1L);
        long expected = 2;
        assertEquals(expected, actual);

        long actual2 = frequency.getCumFreq(2L);
        long expected2 = 4;
        assertEquals(expected2, actual2);
    }

    /**
     * Test getCumFreq method with char values.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');

        long actual = frequency.getCumFreq('A');
        long expected = 2;
        assertEquals(expected, actual);

        long actual2 = frequency.getCumFreq('B');
        long expected2 = 4;
        assertEquals(expected2, actual2);
    }

    /**
     * Test getCumFreq method with null value.
     */
    @Test
    public void testGetCumFreqNullValue() {
        Frequency frequency = new Frequency();

        long actual = frequency.getCumFreq(null);
        long expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test getCumPct method with int values.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        double actual = frequency.getCumPct(1);
        double expected = 0.3333;
        assertEquals(expected, actual, 0.001);

        double actual2 = frequency.getCumPct(2);
        double expected2 = 1.0;
        assertEquals(expected2, actual2, 0.001);
    }

    /**
     * Test getCumPct method with long values.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        double actual = frequency.getCumPct(1L);
        double expected = 0.3333;
        assertEquals(expected, actual, 0.001);

        double actual2 = frequency.getCumPct(2L);
        double expected2 = 1.0;
        assertEquals(expected2, actual2, 0.001);
    }

    /**
     * Test getCumPct method with char values.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');

        double actual = frequency.getCumPct('A');
        double expected = 0.5;
        assertEquals(expected, actual, 0.001);

        double actual2 = frequency.getCumPct('B');
        double expected2 = 1.0;
        assertEquals(expected2, actual2, 0.001);
    }

    /**
     * Test getCumPct method with null value.
     */
    @Test
    public void testGetCumPctNullValue() {
        Frequency frequency = new Frequency();

        double actual = frequency.getCumPct(null);
        double expected = Double.NaN;
        assertEquals(expected, actual);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();

        long actualCount = frequency.getCount(1);
        long expectedCount = 0;
        assertEquals(expectedCount, actualCount);

        long actualSumFreq = frequency.getSumFreq();
        long expectedSumFreq = 0;
        assertEquals(expectedSumFreq, actualSumFreq);

        long actualCumFreq = frequency.getCumFreq(1);
        long expectedCumFreq = 0;
        assertEquals(expectedCumFreq, actualCumFreq);

        double actualPct = frequency.getPct(1);
        double expectedPct = Double.NaN;
        assertEquals(expectedPct, actualPct);

        double actualCumPct = frequency.getCumPct(1);
        double expectedCumPct = Double.NaN;
        assertEquals(expectedCumPct, actualCumPct);
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Iterator<Comparable<?>> actual = frequency.valuesIterator();
        Object[] expectedValues = {1, 1, 2, 2};
        int i = 0;
        while (actual.hasNext()) {
            Comparable<?> actualValue = actual.next();
            assertEquals(expectedValues[i++], actualValue);
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
        frequency.addValue(2);

        long actual = frequency.getSumFreq();
        long expected = 6;
        assertEquals(expected, actual);
    }
}