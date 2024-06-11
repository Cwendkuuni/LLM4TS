package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {
    // Test cases for constructor
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    // Test cases for addValue methods
    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        long count = frequency.getCount("test");
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        long count = frequency.getCount(5);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(10L);
        long count = frequency.getCount(10L);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(1, count);
    }

    @Test
    public void testAddValueMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        assertEquals(2, count1);
        assertEquals(1, count2);
    }

    // Test cases for getSumFreq method
    @Test
    public void testGetSumFreqEmptyFrequency() {
        Frequency frequency = new Frequency();
        long sumFreq = frequency.getSumFreq();
        assertEquals(0, sumFreq);
    }

    @Test
    public void testGetSumFreqFrequencyWithValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);
    }

    // Test cases for getCount methods
    @Test
    public void testGetCountEmptyFrequency() {
        Frequency frequency = new Frequency();
        long count = frequency.getCount(1);
        assertEquals(0, count);
    }

    @Test
    public void testGetCountValuePresentOnce() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test
    public void testGetCountValuePresentMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountValueNotPresent() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        long count = frequency.getCount(1);
        assertEquals(0, count);
    }

    // Test cases for getPct methods
    @Test
    public void testGetPctEmptyFrequency() {
        Frequency frequency = new Frequency();
        double pct = frequency.getPct(1);
        assertEquals(Double.NaN, pct, 0.0);
    }

    @Test
    public void testGetPctValuePresentOnce() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertEquals(1.0 / 1.0, pct, 0.0);
    }

    @Test
    public void testGetPctValuePresentMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertEquals(2.0 / 3.0, pct, 0.0);
    }

    @Test
    public void testGetPctValueNotPresent() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        double pct = frequency.getPct(1);
        assertEquals(0.0, pct, 0.0);
    }

    // Test cases for getCumFreq methods
    @Test
    public void testGetCumFreqEmptyFrequency() {
        Frequency frequency = new Frequency();
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumFreqValueLessThanFirstValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumFreqValueGreaterThanLastValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(3);
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumFreqValuePresentOnce() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
    }

    @Test
    public void testGetCumFreqValuePresentMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(2, cumFreq);
    }

    // Test cases for getCumPct methods
    @Test
    public void testGetCumPctEmptyFrequency() {
        Frequency frequency = new Frequency();
        double cumPct = frequency.getCumPct(1);
        assertEquals(Double.NaN, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctValueLessThanFirstValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.0, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctValueGreaterThanLastValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(3);
        assertEquals(0.0, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctValuePresentOnce() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(1);
        assertEquals(1.0 / 1.0, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctValuePresentMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(3.0 / 3.0, cumPct, 0.0);
    }

    // Test clearing the frequency table
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        long sumFreq = frequency.getSumFreq();
        assertEquals(0, sumFreq);
    }
}