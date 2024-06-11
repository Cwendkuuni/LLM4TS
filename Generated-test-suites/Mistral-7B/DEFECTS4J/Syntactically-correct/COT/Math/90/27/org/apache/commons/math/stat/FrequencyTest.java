package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {
    
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testConstructorWithValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertNotNull(frequency);
        assertEquals(1, frequency.getSumFreq());
        assertFalse(frequency.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("not a number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithValidValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
        assertFalse(frequency.isEmpty());
    }

    @Test
    public void testAddValueWithSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.4 \t 0.4 \n2 \t 1 \t 0.3 \t 0.7 \n\nTotal: 3\n";
        assertEquals(expected, result);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Iterator values = frequency.valuesIterator();
        assertEquals(1, values.next().longValue());
        assertEquals(2, values.next().longValue());
        assertEquals(1, values.next().longValue());
        assertFalse(values.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(0, frequency.getCount("not a number"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.getCount("not a number");
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double result = frequency.getPct(1);
        double expected = 0.4;
        assertEquals(expected, result, 0.001);
        result = frequency.getPct(2);
        expected = 0.3;
        assertEquals(expected, result, 0.001);
        result = frequency.getPct(3);
        assertEquals(0.0, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPctWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.getPct("not a number");
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long result = frequency.getCumFreq(1);
        long expected = 1;
        assertEquals(expected, result);
        result = frequency.getCumFreq(2);
        expected = 2;
        assertEquals(expected, result);
        result = frequency.getCumFreq(3);
        assertEquals(3, result);
        result = frequency.getCumFreq("not a number");
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.getCumFreq("not a number");
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double result = frequency.getCumPct(1);
        double expected = 0.4;
        assertEquals(expected, result, 0.001);
        result = frequency.getCumPct(2);
        expected = 0.7;
        assertEquals(expected, result, 0.001);
        result = frequency.getCumPct(3);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testGetCumPctWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double result = frequency.getCumPct("not a number");
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }
}