package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency instanceof Frequency);
        assertTrue(frequency instanceof Serializable);
        assertNull(frequency.freqTable);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> o1.toString().compareTo(o2.toString());
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency instanceof Frequency);
        assertTrue(frequency instanceof Serializable);
        assertTrue(frequency.freqTable instanceof TreeMap);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        String expected = "Value   Freq.   Pct.   Cum Pct.\n1   2   0.5    0.5\n1   2   0.5    1.0\n2   2   0.5    1.5\n2   2   0.5    2.0\n";
        String result = frequency.toString();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithIntegerValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueWithLongValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueWithCharacterValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(1, count);
    }

    @Test
    public void testAddValueWithExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        assertEquals(6, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        assertEquals(2, count1);
        assertEquals(2, count2);
    }

    @Test
    public void testGetCountWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long count = frequency.getCount(new Object());
        assertEquals(0, count);
    }

    @Test
    public void testGetCountWithNegativeValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(-1);
        assertEquals(0, count);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double pct1 = frequency.getPct(1);
        double pct2 = frequency.getPct(2);
        assertEquals(0.3333333333333333, pct1, 0.00001);
        assertEquals(0.3333333333333333, pct2, 0.00001);
    }

    @Test
    public void testGetPctWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double pct = frequency.getPct(new Object());
        assertEquals(Double.NaN, pct, 0.0);
    }

    @Test
    public void testGetPctWithNoValuesAdded() {
        Frequency frequency = new Frequency();
        double pct = frequency.getPct(1);
        assertEquals(Double.NaN, pct, 0.0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long cumFreq1 = frequency.getCumFreq(1);
        long cumFreq2 = frequency.getCumFreq(2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(1, cumFreq1);
        assertEquals(2, cumFreq2);
        assertEquals(4, cumFreq3);
    }

    @Test
    public void testGetCumFreqWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(new Object());
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumFreqWithValueLessThanFirstValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(0);
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumFreqWithValueGreaterThanLastValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(3);
        assertEquals(4, cumFreq);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double cumPct1 = frequency.getCumPct(1);
        double cumPct2 = frequency.getCumPct(2);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(0.3333333333333333, cumPct1, 0.00001);
        assertEquals(0.6666666666666667, cumPct2, 0.00001);
        assertEquals(1.0, cumPct3, 0.00001);
    }

    @Test
    public void testGetCumPctWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(new Object());
        assertEquals(Double.NaN, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctWithNoValuesAdded() {
        Frequency frequency = new Frequency();
        double cumPct = frequency.getCumPct(1);
        assertEquals(Double.NaN, cumPct, 0.0);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        Object value = iterator.next();
        assertEquals(1, Precision.compareTo(value.longValue(), 1, 0.00001));
        value = iterator.next();
        assertEquals(1, Precision.compareTo(value.longValue(), 1, 0.00001));
        value = iterator.next();
        assertEquals(2, Precision.compareTo(value.longValue(), 2, 0.00001));
        value = iterator.next();
        assertEquals(2, Precision.compareTo(value.longValue(), 2, 0.00001));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.clear();
        Iterator<Object> iterator = frequency.valuesIterator();
        assertFalse(iterator.hasNext());
    }

    
}