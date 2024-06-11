package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(Integer.valueOf(1)));
        assertEquals(0, frequency.getCount(Integer.valueOf(2)));
        assertEquals(0, frequency.getCount(Character.valueOf('a')));
        assertEquals(0, frequency.getCount(Character.valueOf('b')));
        assertEquals(0, frequency.getPct(Integer.valueOf(1)));
        assertEquals(0, frequency.getPct(Integer.valueOf(2)));
        assertEquals(0, frequency.getPct(Character.valueOf('a')));
        assertEquals(0, frequency.getPct(Character.valueOf('b')));
        assertEquals(0, frequency.getCumFreq(Integer.valueOf(1)));
        assertEquals(0, frequency.getCumFreq(Integer.valueOf(2)));
        assertEquals(0, frequency.getCumFreq(Character.valueOf('a')));
        assertEquals(0, frequency.getCumFreq(Character.valueOf('b')));
        assertEquals(0, frequency.getCumPct(Integer.valueOf(1)));
        assertEquals(0, frequency.getCumPct(Integer.valueOf(2)));
        assertEquals(0, frequency.getCumPct(Character.valueOf('a')));
        assertEquals(0, frequency.getCumPct(Character.valueOf('b')));
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1.0, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(0, frequency.getCount(Integer.valueOf(2)));
        assertEquals(0.0, frequency.getPct(Integer.valueOf(2)), 0.001);
        assertEquals(0.0, frequency.getCumPct(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(2, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1, frequency.getCount(2L));
        assertEquals(1.5, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.5, frequency.getPct(Integer.valueOf(2)), 0.001);
        assertEquals(1.5, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.5, frequency.getCumPct(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(Character.valueOf('a')));
        assertEquals(1.0, frequency.getPct(Character.valueOf('a')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('a')), 0.001);
        assertEquals(0, frequency.getCount(Character.valueOf('b')));
        assertEquals(0.0, frequency.getPct(Character.valueOf('b')), 0.001);
        assertEquals(0.0, frequency.getCumPct(Character.valueOf('b')), 0.001);
    }

    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1, frequency.getCount(Integer.valueOf(2)));
        assertEquals(2.0 / 3.0, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0 / 3.0, frequency.getPct(Integer.valueOf(2)), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(3.0 / 3.0, frequency.getCumPct(Integer.valueOf(2)), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue("notComparable");
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(Integer.valueOf(1)));
        assertEquals(0, frequency.getCount(Integer.valueOf(2)));
        assertEquals(0, frequency.getCount(Character.valueOf('a')));
        assertEquals(0, frequency.getCount(Character.valueOf('b')));
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator it = frequency.valuesIterator();
        assertTrue(it.hasNext());
        assertEquals(1, it.next().longValue());
        assertTrue(it.hasNext());
        assertEquals(1, it.next().longValue());
        assertTrue(it.hasNext());
        assertEquals(2, it.next().longValue());
        assertFalse(it.hasNext());
    }

    @Test
    public void testComparator() {
        Frequency frequency = new Frequency(new Comparator<Comparable<?>>() {
            @Override
            public int compare(Comparable<? super Comparable<?>> o1, Comparable<? super Comparable<?>> o2) {
                return o1.compareTo(o2) * -1;
            }
        });
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator it = frequency.valuesIterator();
        assertTrue(it.hasNext());
        assertEquals(2, it.next().longValue());
        assertTrue(it.hasNext());
        assertEquals(1, it.next().longValue());
        assertTrue(it.hasNext());
        assertEquals(1, it.next().longValue());
        assertFalse(it.hasNext());
    }
}