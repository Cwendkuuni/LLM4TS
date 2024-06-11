package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        Object value = "test";
        frequency.addValue(value);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(value));
        assertEquals(1.0, frequency.getPct(value), 0.01);
        assertEquals(1.0, frequency.getCumPct(value), 0.01);
        assertEquals(value, frequency.freqTable.firstKey());
        assertEquals(value, frequency.freqTable.lastKey());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" + value + "\t1\t100.0%\t100.0%\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        Object value1 = "test1";
        frequency.addValue(value1);
        Object value2 = 1;
        frequency.addValue(value2);
        throw new AssertionError("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableChar() {
        Frequency frequency = new Frequency();
        Object value1 = 'a';
        frequency.addValue(value1);
        char value2 = 'b';
        frequency.addValue(value2);
        throw new AssertionError("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(value));
        assertEquals(1.0, frequency.getPct(value), 0.01);
        assertEquals(1.0, frequency.getCumPct(value), 0.01);
        assertEquals(Long.valueOf(value), frequency.freqTable.firstKey());
        assertEquals(Long.valueOf(value), frequency.freqTable.lastKey());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" + value + "\t1\t100.0%\t100.0%\n", frequency.toString());
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(value));
        assertEquals(1.0, frequency.getPct(value), 0.01);
        assertEquals(1.0, frequency.getCumPct(value), 0.01);
        assertEquals(Long.valueOf(value), frequency.freqTable.firstKey());
        assertEquals(Long.valueOf(value), frequency.freqTable.lastKey());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" + value + "\t1\t100.0%\t100.0%\n", frequency.toString());
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(value));
        assertEquals(1.0, frequency.getPct(value), 0.01);
        assertEquals(1.0, frequency.getCumPct(value), 0.01);
        assertEquals(Character.valueOf(value), frequency.freqTable.firstKey());
        assertEquals(Character.valueOf(value), frequency.freqTable.lastKey());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" + value + "\t1\t100.0%\t100.0%\n", frequency.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        int value1 = 1;
        frequency.addValue(value1);
        int value2 = 2;
        frequency.addValue(value2);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        Object value = "test";
        frequency.addValue(value);
        frequency.addValue(value);
        assertEquals(2, frequency.getCount(value));
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        assertEquals(2, frequency.getCount(value));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        assertEquals(2, frequency.getCount(value));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        frequency.addValue(value);
        assertEquals(2, frequency.getCount(value));
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        Object value = "test";
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue("test2");
        assertEquals(0.5, frequency.getPct(value), 0.01);
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(value), 0.01);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(value), 0.01);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct(value), 0.01);
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        Object value = "test";
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue("test2");
        assertEquals(2, frequency.getCumFreq(value));
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(value));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(value));
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue('b');
        assertEquals(1, frequency.getCumFreq(value));
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        Object value = "test";
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue("test2");
        assertEquals(0.6666666666666666, frequency.getCumPct(value), 0.0000001);
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        int value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getCumPct(value), 0.0000001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        long value = 1;
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getCumPct(value), 0.0000001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        char value = 'a';
        frequency.addValue(value);
        frequency.addValue(value);
        frequency.addValue('b');
        assertEquals(0.3333333333333333, frequency.getCumPct(value), 0.0000001);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue(1.2);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1\t1\t33.33%\t33.33%\ntest\t1\t33.33%\t66.67%\n1.2\t1\t33.33%\t100.0%\n", frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue(1.2);
        Iterator<Object> values = frequency.valuesIterator();
        assertEquals(1, values.next().intValue());
        assertEquals("test", values.next());
        assertEquals(1.2, values.next().doubleValue());
        assertFalse(values.hasNext());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertNull(frequency.toString());
    }
}