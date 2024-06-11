package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

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

        Frequency frequencyComparator = new Frequency(new NaturalComparator());
        assertNotNull(frequencyComparator);
    }

    /**
     * Test method addValue
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        long expectedCount = 2;
        long actualCount = frequency.getCount(1);
        assertEquals(expectedCount, actualCount);

        expectedCount = 2;
        actualCount = frequency.getCount(2);
        assertEquals(expectedCount, actualCount);

        expectedCount = 4;
        long sumFreq = frequency.getSumFreq();
        assertEquals(expectedCount, sumFreq);

        double expectedPct = 0.5;
        double actualPct = frequency.getPct(1);
        assertEquals(expectedPct, actualPct, 0.001);

        expectedPct = 0.5;
        actualPct = frequency.getPct(2);
        assertEquals(expectedPct, actualPct, 0.001);

        long expectedCumFreq = 2;
        long actualCumFreq = frequency.getCumFreq(1);
        assertEquals(expectedCumFreq, actualCumFreq);

        expectedCumFreq = 4;
        actualCumFreq = frequency.getCumFreq(2);
        assertEquals(expectedCumFreq, actualCumFreq);

        double expectedCumPct = 0.5;
        double actualCumPct = frequency.getCumPct(1);
        assertEquals(expectedCumPct, actualCumPct, 0.001);

        expectedCumPct = 1.0;
        actualCumPct = frequency.getCumPct(2);
        assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    /**
     * Test method clear
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        frequency.clear();

        long expectedCount = 0;
        long actualCount = frequency.getCount(1);
        assertEquals(expectedCount, actualCount);

        expectedCount = 0;
        actualCount = frequency.getCount(2);
        assertEquals(expectedCount, actualCount);

        long expectedSumFreq = 0;
        long sumFreq = frequency.getSumFreq();
        assertEquals(expectedSumFreq, sumFreq);

        double expectedPct = 0.0;
        double pct = frequency.getPct(1);
        assertEquals(expectedPct, pct, 0.001);

        double expectedCumPct = 0.0;
        double cumPct = frequency.getCumPct(1);
        assertEquals(expectedCumPct, cumPct, 0.001);
    }

    /**
     * Test method valuesIterator
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Iterator<Long> valuesIterator = frequency.valuesIterator();

        Long value = valuesIterator.next();
        assertEquals(Long.valueOf(1), value);

        value = valuesIterator.next();
        assertEquals(Long.valueOf(1), value);

        value = valuesIterator.next();
        assertEquals(Long.valueOf(2), value);

        value = valuesIterator.next();
        assertEquals(Long.valueOf(2), value);

        assertFalse(valuesIterator.hasNext());
    }
}