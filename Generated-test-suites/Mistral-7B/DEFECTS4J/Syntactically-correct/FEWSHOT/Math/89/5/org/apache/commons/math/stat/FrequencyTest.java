package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Frequency frequency = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        });
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertTrue(frequency.getSumFreq() == 5);
        assertEquals(2, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));

        try {
            frequency.addValue("not an integer");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            frequency.addValue(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(5, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(0, frequency.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        double total = frequency.getSumFreq();
        assertEquals(0.4, frequency.getPct(1), 0.01);
        assertEquals(0.6, frequency.getPct(2), 0.01);
        assertEquals(0.0, frequency.getPct(3), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = 0;
        for (Object value : frequency) {
            cumFreq += frequency.getCount((Comparable<?>) value);
            assertEquals(cumFreq, frequency.getCumFreq(value));
        }
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = 0.0;
        for (Object value : frequency) {
            cumPct += frequency.getPct((Comparable<?>) value);
            assertEquals(cumPct, frequency.getCumPct(value), 0.01);
        }
    }
}