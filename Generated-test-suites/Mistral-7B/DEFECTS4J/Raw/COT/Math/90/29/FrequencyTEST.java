package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().isEmpty());
    }

    /**
     * Test constructor with custom comparator.
     */
    @org.junit.Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().isEmpty());
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        String expected = "Value  \t Freq.  \t Pct.  \t Cum Pct.\n1 \t 2 \t 20.0% \t 20.0%\n2 \t 2 \t 20.0% \t 40.0%\n3 \t 2 \t 20.0% \t 60.0%\n";
        String actual = frequency.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test addValue method with int value.
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test addValue method with long value.
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1L));
        frequency.addValue(1L);
        assertEquals(2, frequency.getCount(1L));
    }

    /**
     * Test addValue method with char value.
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
        frequency.addValue('A');
        assertEquals(2, frequency.getCount('A'));
    }

    /**
     * Test addValue method with non-comparable value.
     * @expectedException java.lang.IllegalArgumentException
     */
    @org.junit.Test
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test addValue method with non-Comparable value.
     * @expectedException java.lang.ClassCastException
     */
    @org.junit.Test
    public void testAddValueNonComparableValueClassCastException() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue("notComparable");
    }

    /**
     * Test getSumFreq method.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long expected = 6;
        long actual = frequency.getSumFreq();
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with int value.
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long expected = 2;
        long actual = frequency.getCount(1);
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with long value.
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);

        long expected = 2;
        long actual = frequency.getCount(1L);
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with char value.
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        frequency.addValue('C');
        frequency.addValue('C');

        long expected = 2;
        long actual = frequency.getCount('A');
        assertEquals(expected, actual);
    }

    /**
     * Test getCount method with non-comparable value.
     */
    @org.junit.Test
    public void testGetCountNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long expected = 0;
        long actual = frequency.getCount(new Object());
        assertEquals(expected, actual);
    }

    /**
     * Test getPct method with int value.
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        double expected = 20.0;
        double actual = frequency.getPct(1);
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getPct method with long value.
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);

        double expected = 20.0;
        double actual = frequency.getPct(1L);
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getPct method with char value.
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        frequency.addValue('C');
        frequency.addValue('C');

        double expected = 20.0;
        double actual = frequency.getPct('A');
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getPct method with non-comparable value.
     */
    @org.junit.Test
    public void testGetPctNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        double expected = 0.0;
        double actual = frequency.getPct(new Object());
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getCumFreq method with int value.
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long expected = 1;
        long actual = frequency.getCumFreq(1);
        assertEquals(expected, actual);
    }

    /**
     * Test getCumFreq method with long value.
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);

        long expected = 1;
        long actual = frequency.getCumFreq(1L);
        assertEquals(expected, actual);
    }

    /**
     * Test getCumFreq method with char value.
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        frequency.addValue('C');
        frequency.addValue('C');

        long expected = 1;
        long actual = frequency.getCumFreq('A');
        assertEquals(expected, actual);
    }

    /**
     * Test getCumFreq method with non-comparable value.
     */
    @org.junit.Test
    public void testGetCumFreqNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long expected = 0;
        long actual = frequency.getCumFreq(new Object());
        assertEquals(expected, actual);
    }

    /**
     * Test getCumPct method with int value.
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        double expected = 20.0;
        double actual = frequency.getCumPct(1);
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getCumPct method with long value.
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);

        double expected = 20.0;
        double actual = frequency.getCumPct(1L);
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getCumPct method with char value.
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('B');
        frequency.addValue('C');
        frequency.addValue('C');

        double expected = 20.0;
        double actual = frequency.getCumPct('A');
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test getCumPct method with non-comparable value.
     */
    @org.junit.Test
    public void testGetCumPctNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        double expected = 0.0;
        double actual = frequency.getCumPct(new Object());
        assertEquals(expected, Precision.doubleEquals(actual, expected, 0.001d), 0.001d);
    }

    /**
     * Test clear method.
     */
    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        assertTrue(frequency.valuesIterator().hasNext());
        frequency.clear();
        assertFalse(frequency.valuesIterator().hasNext());
    }
}