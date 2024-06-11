package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.333333 \t 0.333333\n2 \t 2 \t 0.333333 \t 0.666667\n", frequency.toString());
    }

    @Test
    public void testDeprecatedAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        expectIllegalArgumentException(frequency);
        frequency.addValue('a');
        expectIllegalArgumentException(frequency);
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        frequency.addValue(2);
        assertEquals(3, frequency.getCount(2));
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(2));
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(2));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1L);
        assertEquals(2, frequency.getCount(1));
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
        frequency.addValue(2L);
        assertEquals(2, frequency.getCount(2));
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        frequency.addValue('a');
        assertEquals(2, frequency.getCount('a'));
        frequency.addValue('b');
        assertEquals(1, frequency.getCount('b'));
        frequency.addValue('b');
        assertEquals(2, frequency.getCount('b'));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(6, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount(3));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount((long) 1));
        assertEquals(2, frequency.getCount((long) 2));
        assertEquals(0, frequency.getCount((long) 3));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('b'));
        assertEquals(0, frequency.getCount('c'));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.333333, frequency.getPct(1), 0.0001);
        assertEquals(0.333333, frequency.getPct(2), 0.0001);
        assertEquals(NaN, frequency.getPct(3), 0.0001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.333333, frequency.getPct((long) 1), 0.0001);
        assertEquals(0.333333, frequency.getPct((long) 2), 0.0001);
        assertEquals(NaN, frequency.getPct((long) 3), 0.0001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        assertEquals(0.333333, frequency.getPct('a'), 0.0001);
        assertEquals(0.333333, frequency.getPct('b'), 0.0001);
        assertEquals(NaN, frequency.getPct('c'), 0.0001);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(0));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(5, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(0L));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(3, frequency.getCumFreq(2L));
        assertEquals(5, frequency.getCumFreq(3L));
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        assertEquals(1, frequency.getCumFreq('a'.toLowerCase()));
        assertEquals(1, frequency.getCumFreq('a'.toUpperCase()));
        assertEquals(3, frequency.getCumFreq('b'.toLowerCase()));
        assertEquals(5, frequency.getCumFreq('b'.toUpperCase()));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.333333, frequency.getPct(1), 0.0001);
        assertEquals(0.333333, frequency.getPct(2), 0.0001);
        assertEquals(0.5, frequency.getCumPct(2), 0.0001);
        assertEquals(1, frequency.getCumPct(3), 0.0001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.333333, frequency.getPct(1), 0.0001);
        assertEquals(0.333333, frequency.getPct(2), 0.0001);
        assertEquals(0.5, frequency.getCumPct(2), 0.0001);
        assertEquals(1, frequency.getCumPct(3), 0.0001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        assertEquals(0.333333, frequency.getPct('a'), 0.0001);
        assertEquals(0.333333, frequency.getPct('b'), 0.0001);
        assertEquals(0.5, frequency.getCumPct('b'), 0.0001);
        assertEquals(1, frequency.getCumPct('c'), 0.0001);
    }

    private void expectIllegalArgumentException(Frequency frequency) {
        try {
            frequency.addValue(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}