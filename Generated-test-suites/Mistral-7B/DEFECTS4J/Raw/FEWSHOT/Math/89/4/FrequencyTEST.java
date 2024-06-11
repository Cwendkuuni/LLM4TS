package org.apache.commons.math.stat;

import static org.junit. Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 1.0 \t 1.0\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 1.0 \t 1.0\n2 \t 1 \t 1.0 \t 2.0\n", frequency.toString());
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1, frequency.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 1.0 \t 1.0\n2 \t 1 \t 1.0 \t 2.0\n", frequency.toString());
    }

    @Test
    public void testAddValueComparator() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(2, frequency.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 0.4444444444444444 \t 0.4444444444444444\n2 \t 1 \t 0.5555555555555556 \t 1.0\n", frequency.toString());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().longValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().longValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
    }

    @Test
    public void testGetCountInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCount('a'));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2L);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct('a'), 0.001);
    }

    @Test
    public void testGetPctInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNaN(frequency.getPct('a'));
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2L);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(2, frequency.getCumFreq('b'));
    }

    @Test
    public void testGetCumFreqInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumFreq('a'));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2L);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct('a'), 0.001);
        assertEquals(0.5, frequency.getCumPct('a'), 0.001);
    }

    @Test
    public void testGetCumPctInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNaN(frequency.getCumPct('a'));
    }
}