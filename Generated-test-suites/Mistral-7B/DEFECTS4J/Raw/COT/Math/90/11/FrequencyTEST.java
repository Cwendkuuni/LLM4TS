package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency.freqTable);
    }

    /**
     * Test constructor with custom comparator.
     */
    @org.junit.Test
    public void testCustomComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency.freqTable);
    }

    /**
     * Test toString method with empty frequency distribution.
     */
    @org.junit.Test
    public void testToStringEmptyFrequencyDistribution() {
        Frequency frequency = new Frequency();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n";
        String actual = frequency.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test addValue method with different data types.
     */
    @org.junit.Test
    public void testAddValueDifferentDataTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(3.14);
        frequency.addValue(1L);
        frequency.addValue(10);
        Iterator<Object> iterator = frequency.valuesIterator();
        long countA = 0;
        long countOne = 0;
        long countThreePointOneFour = 0;
        long countOneLong = 0;
        long countTen = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            if (value.equals('a')) {
                countA++;
            } else if (value.equals(1)) {
                countOne++;
            } else if (value.equals(3.14)) {
                countThreePointOneFour++;
            } else if (value instanceof Long && Long.parseLong(value.toString()) == 1L) {
                countOneLong++;
            } else if (value.equals(10)) {
                countTen++;
            }
        }
        assertEquals(5, frequency.getSumFreq());
        assertEquals(1, countA);
        assertEquals(2, countOne);
        assertEquals(1, countThreePointOneFour);
        assertEquals(1, countOneLong);
        assertEquals(1, countTen);
    }

    /**
     * Test getCount method.
     */
    @org.junit.Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        long countA = frequency.getCount('a');
        long countB = frequency.getCount('b');
        assertEquals(2, countA);
        assertEquals(1, countB);
    }

    /**
     * Test getCount method with invalid data types.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCountInvalidDataType() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue('b');
        frequency.getCount(10);
    }

    /**
     * Test getPct method.
     */
    @org.junit.Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        double pctA = frequency.getPct('a');
        double pctB = frequency.getPct('b');
        assertEquals(0.4, pctA, 0.001);
        assertEquals(0.6, pctB, 0.001);
    }

    /**
     * Test getPct method with empty frequency distribution.
     */
    @org.junit.Test
    public void testGetPctEmptyFrequencyDistribution() {
        Frequency frequency = new Frequency();
        double pct = frequency.getPct('a');
        assertEquals(Double.NaN, pct, 0.0);
    }

    /**
     * Test getCumFreq method.
     */
    @org.junit.Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        long cumFreqA = frequency.getCumFreq('a');
        long cumFreqB = frequency.getCumFreq('b');
        assertEquals(1, cumFreqA);
        assertEquals(2, cumFreqB);
    }

    /**
     * Test getCumFreq method with invalid data types.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqInvalidDataType() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue('b');
        frequency.getCumFreq(10);
    }

    /**
     * Test getCumPct method.
     */
    @org.junit.Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        double cumPctA = frequency.getCumPct('a');
        double cumPctB = frequency.getCumPct('b');
        assertEquals(0.4, cumPctA, 0.001);
        assertEquals(0.6, cumPctB, 0.001);
    }

    /**
     * Test getCumPct method with empty frequency distribution.
     */
    @org.junit.Test
    public void testGetCumPctEmptyFrequencyDistribution() {
        Frequency frequency = new Frequency();
        double cumPct = frequency.getCumPct('a');
        assertEquals(Double.NaN, cumPct, 0.0);
    }
}