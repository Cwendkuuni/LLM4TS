package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testAddValue_Object() {
        Frequency frequency = new Frequency();
        Object value = new Integer(1);
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_NotComparable() {
        Frequency frequency = new Frequency();
        String value = "test";
        frequency.addValue(value);
    }

    @Test
    public void testAddValue_Integer() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testAddValue_Long() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testAddValue_Char() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testAddValue_SameValue() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        assertEquals(2, frequency.getCount(value));
    }

    @Test
    public void testAddValue_DifferentValues() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        int value2 = 2;
        frequency.addValue(value1);
        frequency.addValue(value2);
        assertEquals(2, frequency.getCount(value1));
        assertEquals(1, frequency.getCount(value2));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        int value2 = 2;
        frequency.addValue(value1);
        frequency.addValue(value2);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount_Object() {
        Frequency frequency = new Frequency();
        Object value = new Integer(1);
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testGetCount_Integer() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testGetCount_Long() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testGetCount_Char() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
    }

    @Test
    public void testGetCount_ValueNotPresent() {
        Frequency frequency = new Frequency();
        int value = 1;
        assertEquals(0, frequency.getCount(value));
    }

    @Test
    public void testGetPct_Object() {
        Frequency frequency = new Frequency();
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        frequency.addValue(value1);
        frequency.addValue(value2);
        double expected = (double) 1 / 3.0;
        assertEquals(expected, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(1 - expected, frequency.getPct(value2), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetPct_Integer() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        int value2 = 2;
        frequency.addValue(value1);
        frequency.addValue(value2);
        double expected = (double) 1 / 3.0;
        assertEquals(expected, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(1 - expected, frequency.getPct(value2), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetPct_Long() {
        Frequency frequency = new Frequency();
        long value1 = 1;
        long value2 = 2;
        frequency.addValue(value1);
        frequency.addValue(value2);
        double expected = (double) 1 / 3.0;
        assertEquals(expected, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(1 - expected, frequency.getPct(value2), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetPct_Char() {
        Frequency frequency = new Frequency();
        char value1 = 'a';
        char value2 = 'b';
        frequency.addValue(value1);
        frequency.addValue(value2);
        double expected = (double) 1 / 3.0;
        assertEquals(expected, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(1 - expected, frequency.getPct(value2), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetPct_EmptyFrequency() {
        Frequency frequency = new Frequency();
        double expected = 0.0;
        assertEquals(expected, frequency.getPct(new Integer(1)), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetCumFreq_Object() {
        Frequency frequency = new Frequency();
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        Object value3 = new Integer(3);
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        assertEquals(1, frequency.getCumFreq(value1));
        assertEquals(2, frequency.getCumFreq(value2));
        assertEquals(3, frequency.getCumFreq(value3));
    }

    @Test
    public void testGetCumFreq_Integer() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        int value2 = 2;
        int value3 = 3;
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        assertEquals(1, frequency.getCumFreq(value1));
        assertEquals(2, frequency.getCumFreq(value2));
        assertEquals(3, frequency.getCumFreq(value3));
    }

    @Test
    public void testGetCumFreq_Long() {
        Frequency frequency = new Frequency();
        long value1 = 1;
        long value2 = 2;
        long value3 = 3;
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        assertEquals(1, frequency.getCumFreq(value1));
        assertEquals(2, frequency.getCumFreq(value2));
        assertEquals(3, frequency.getCumFreq(value3));
    }

    @Test
    public void testGetCumFreq_Char() {
        Frequency frequency = new Frequency();
        char value1 = 'a';
        char value2 = 'b';
        char value3 = 'c';
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        assertEquals(1, frequency.getCumFreq(value1));
        assertEquals(2, frequency.getCumFreq(value2));
        assertEquals(3, frequency.getCumFreq(value3));
    }

    @Test
    public void testGetCumFreq_ValueNotPresent() {
        Frequency frequency = new Frequency();
        int value = 1;
        assertEquals(0, frequency.getCumFreq(value));
    }

    @Test
    public void testGetCumPct_Object() {
        Frequency frequency = new Frequency();
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        Object value3 = new Integer(3);
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        double expected1 = (double) 1 / 3.0;
        double expected2 = (double) 2 / 3.0;
        double expected3 = 1.0;
        assertEquals(expected1, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(expected2, frequency.getPct(value2), Precision.SAFE_EQUALS);
        assertEquals(expected3, frequency.getCumPct(value3), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetCumPct_Integer() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        int value2 = 2;
        int value3 = 3;
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        double expected1 = (double) 1 / 3.0;
        double expected2 = (double) 2 / 3.0;
        double expected3 = 1.0;
        assertEquals(expected1, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(expected2, frequency.getPct(value2), Precision.SAFE_EQUALS);
        assertEquals(expected3, frequency.getCumPct(value3), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetCumPct_Long() {
        Frequency frequency = new Frequency();
        long value1 = 1;
        long value2 = 2;
        long value3 = 3;
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        double expected1 = (double) 1 / 3.0;
        double expected2 = (double) 2 / 3.0;
        double expected3 = 1.0;
        assertEquals(expected1, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(expected2, frequency.getPct(value2), Precision.SAFE_EQUALS);
        assertEquals(expected3, frequency.getCumPct(value3), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetCumPct_Char() {
        Frequency frequency = new Frequency();
        char value1 = 'a';
        char value2 = 'b';
        char value3 = 'c';
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        double expected1 = (double) 1 / 3.0;
        double expected2 = (double) 2 / 3.0;
        double expected3 = 1.0;
        assertEquals(expected1, frequency.getPct(value1), Precision.SAFE_EQUALS);
        assertEquals(expected2, frequency.getPct(value2), Precision.SAFE_EQUALS);
        assertEquals(expected3, frequency.getCumPct(value3), Precision.SAFE_EQUALS);
    }

    @Test
    public void testGetCumPct_EmptyFrequency() {
        Frequency frequency = new Frequency();
        double expected = 0.0;
        assertEquals(expected, frequency.getCumPct(new Integer(1)), Precision.SAFE_EQUALS);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        int value2 = 2;
        frequency.addValue(value1);
        frequency.addValue(value2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(value1, iterator.next());
        assertEquals(value2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}