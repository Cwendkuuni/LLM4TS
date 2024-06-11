package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1.0, frequency.getPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('a'), 0.001);

        frequency.addValue('a');
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2.0 / 3.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.001);

        frequency.addValue('b');
        assertEquals(3, frequency.getCount('a'));
        assertEquals(2.0 / 3.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(1, frequency.getCount('b'));
        assertEquals(1.0 / 3.0, frequency.getPct('b'), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct('b'), 0.001);

        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('c');

        assertEquals(5, frequency.getSumFreq());
        assertEquals(4, frequency.getCount('a'));
        assertEquals(0.8, frequency.getPct('a'), 0.001);
        assertEquals(0.8, frequency.getCumPct('a'), 0.001);

        assertEquals(1, frequency.getCount('b'));
        assertEquals(0.2, frequency.getPct('b'), 0.001);
        assertEquals(0.2, frequency.getCumPct('b'), 0.001);

        assertEquals(0, frequency.getCount('c'));
        assertEquals(0.0, frequency.getPct('c'), 0.001);
        assertEquals(0.0, frequency.getCumPct('c'), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentExceptionChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentExceptionComparator() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                throw new ClassCastException();
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue('a');
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('c');
        assertEquals(5, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('c');

        assertEquals(4, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
        assertEquals(0, frequency.getCount('c'));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('c');

        assertEquals(0.8, frequency.getPct('a'), 0.001);
        assertEquals(0.2, frequency.getPct('b'), 0.001);
        assertEquals(0.0, frequency.getPct('c'), 0.001);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('c');

        assertEquals(0.8, frequency.getPct('a'), 0.001);
        assertEquals(0.8, frequency.getCumPct('a'), 0.001);

        assertEquals(0.8, frequency.getPct('a'), 0.001);
        assertEquals(0.88, frequency.getCumPct('a'), 0.001);

        assertEquals(0.2, frequency.getPct('b'), 0.001);
        assertEquals(0.2, frequency.getCumPct('b'), 0.001);

        assertEquals(0.2, frequency.getPct('b'), 0.001);
        assertEquals(0.28, frequency.getCumPct('b'), 0.001);

        assertEquals(0.0, frequency.getPct('c'), 0.001);
        assertEquals(0.0, frequency.getCumPct('c'), 0.001);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('c');

        Iterator<Object> iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals('a', iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals('b', iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals('a', iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals('b', iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals('c', iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        frequency.clear();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }
}