package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.TreeMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency instanceof Frequency);
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency instanceof Frequency);
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        assertEquals(1, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals("test", frequency.valuesIterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
    }

    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals('a', frequency.valuesIterator().next());
    }

    @Test
    public void testAddValueMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
    }

    @Test
    public void testAddValueSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
    }

    @Test
    public void testAddValueExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next().longValue());
    }

    @Test(expected = ClassCastException.class)
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue("test1");
        frequency.addValue("test2");
        Iterator iterator = frequency.valuesIterator();
        assertEquals("test1", iterator.next());
        assertEquals("test2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        assertEquals(1, frequency.getCount("test"));
    }

    @Test
    public void testGetCountComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testGetCountNonExistentValue() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testGetCountSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
    }

    @Test
    public void testGetCountNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(null);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        assertEquals(Double.NaN, frequency.getPct("test"), 0.001);
    }

    @Test
    public void testGetPctComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0.0, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0.0, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(0.0, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(0.0, frequency.getPct('a'), 0.001);
    }

    @Test
    public void testGetPctValueWithManyValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('a');
        assertEquals(0.333, frequency.getPct(1), 0.001);
        assertEquals(0.333, frequency.getPct(1L), 0.001);
        assertEquals(0.333, frequency.getPct((byte) 1), 0.001);
        assertEquals(0.2, frequency.getPct("test"), 0.001);
        assertEquals(0.1, frequency.getPct('a'), 0.001);
    }

    @Test
    public void testGetPctZeroValues() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctValueGreaterThanAll() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(Double.NaN, frequency.getPct(2), 0.001);
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        assertEquals(0, frequency.getCumFreq("test"));
    }

    @Test
    public void testGetCumFreqComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCumFreq('a'));
    }

    @Test
    public void testGetCumFreqValueLessThanFirst() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0, frequency.getCumFreq(0));
    }

    @Test
    public void testGetCumFreqValueGreaterThanLast() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumFreqValueInMiddle() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(6, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumFreqValueWithManyValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('a');
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1, frequency.getCumFreq((byte) 1));
        assertEquals(3, frequency.getCumFreq("test"));
        assertEquals(4, frequency.getCumFreq('a'));
        assertEquals(7, frequency.getCumFreq(5)); // arbitrary value greater than all existing values
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        assertEquals(Double.NaN, frequency.getCumPct("test"), 0.001);
    }

    @Test
    public void testGetCumPctComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(0.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(0.0, frequency.getCumPct('a'), 0.001);
    }

    @Test
    public void testGetCumPctValueLessThanFirst() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(0.0, frequency.getCumPct(0), 0.001);
    }

    @Test
    public void testGetCumPctValueGreaterThanLast() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        assertEquals(1.0, frequency.getCumPct(4), 0.001);
    }

    @Test
    public void testGetCumPctValueInMiddle() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.333, frequency.getCumPct(1), 0.001);
        assertEquals(0.666, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    @Test
    public void testGetCumPctValueWithManyValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('a');
        assertEquals(0.333, frequency.getCumPct(1), 0.001);
        assertEquals(0.333, frequency.getCumPct(1L), 0.001);
        assertEquals(0.333, frequency.getCumPct((byte) 1), 0.001);
        assertEquals(0.6, frequency.getCumPct("test"), 0.001);
        assertEquals(0.7, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct(Double.POSITIVE_INFINITY), 0.001);
    }
}