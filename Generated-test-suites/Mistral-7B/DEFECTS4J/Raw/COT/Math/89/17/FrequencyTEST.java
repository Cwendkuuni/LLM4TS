package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
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
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 33.33% \t 33.33% \n2 \t 1 \t 16.67% \t 50.00% \n";
        assertEquals(expected, result);
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1L));
        frequency.addValue(1L);
        assertEquals(2, frequency.getCount(1L));
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        frequency.addValue('a');
        assertEquals(2, frequency.getCount('a'));
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCount("3"));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueSameObject() {
        Frequency frequency = new Frequency();
        Object obj = new Object();
        frequency.addValue(obj);
        frequency.addValue(obj);
        assertEquals(2, frequency.getCount(obj));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(4, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(2, frequency.getCount(1L));
    }

    @Test
    public void testGetCountCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(2, frequency.getCount('a'));
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCount("3"));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(33.33, frequency.getPct(1), 0.01);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(33.33, frequency.getPct(1L), 0.01);
    }

    @Test
    public void testGetPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(33.33, frequency.getPct('a'), 0.01);
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getPct("3"));
        frequency.addValue(1);
        assertEquals(33.33, frequency.getPct(1), 0.01);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(4, frequency.getCumFreq(4));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(2, frequency.getCumFreq(2L));
        assertEquals(3, frequency.getCumFreq(3L));
        assertEquals(4, frequency.getCumFreq(4L));
    }

    @Test
    public void testGetCumFreqCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(2, frequency.getCumFreq('b'));
        assertEquals(3, frequency.getCumFreq('c'));
        assertEquals(4, frequency.getCumFreq('d'));
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumFreq("3"));
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(33.33, frequency.getPct(1), 0.01);
        assertEquals(66.67, frequency.getCumPct(2), 0.01);
        assertEquals(100.0, frequency.getCumPct(3), 0.01);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(33.33, frequency.getPct(1L), 0.01);
        assertEquals(66.67, frequency.getCumPct(2L), 0.01);
        assertEquals(100.0, frequency.getCumPct(3L), 0.01);
    }

    @Test
    public void testGetCumPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(33.33, frequency.getPct('a'), 0.01);
        assertEquals(66.67, frequency.getCumPct('b'), 0.01);
        assertEquals(100.0, frequency.getCumPct('c'), 0.01);
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumPct("3"));
        frequency.addValue(1);
        assertEquals(33.33, frequency.getPct(1), 0.01);
        assertEquals(66.67, frequency.getCumPct(1), 0.01);
        assertEquals(100.0, frequency.getCumPct(2), 0.01);
        assertEquals(100.0, frequency.getCumPct(2), 0.01);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, ((Comparable<Long>) iterator.next()).compareTo(Long.valueOf(1)));
        assertEquals(1, ((Comparable<Long>) iterator.next()).compareTo(Long.valueOf(1)));
        assertEquals(2, ((Comparable<Long>) iterator.next()).compareTo(Long.valueOf(2)));
        assertFalse(iterator.hasNext());
    }

    @Test(expected = ClassCastException.class)
    public void testGetCountNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(1);
        assertThrows(ClassCastException.class, () -> frequency.getCount(new Object()));
    }

    @Test(expected = ClassCastException.class)
    public void testGetPctNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(1);
        assertThrows(ClassCastException.class, () -> frequency.getPct(new Object()));
    }

    @Test(expected = ClassCastException.class)
    public void testGetCumFreqNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(1);
        assertThrows(ClassCastException.class, () -> frequency.getCumFreq(new Object()));
    }

    @Test(expected = ClassCastException.class)
    public void testGetCumPctNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(1);
        assertThrows(ClassCastException.class, () -> frequency.getCumPct(new Object()));
    }

    @Test
    public void testGetSumFreqEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testGetCountEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testGetPctEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(null), 0.0);
    }

    @Test
    public void testGetCumFreqEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCumFreq(null));
    }

    @Test
    public void testGetCumPctEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getCumPct(null), 0.0);
    }
}