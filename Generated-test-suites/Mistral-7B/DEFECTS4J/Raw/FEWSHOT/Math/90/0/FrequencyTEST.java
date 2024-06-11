package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testFrequencyWithComparatorConstructor() {
        Frequency frequency = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
        });
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n" +
                "2 \t 2 \t 0.3333333333333333 \t 0.6666666666666667\n";

        assertEquals(expected, frequency.toString());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(4, frequency.getCount(1));
        assertEquals(4, frequency.getCount(2));
        assertEquals(0, frequency.getCount(3));
        assertEquals(4, frequency.getSumFreq());

        Iterator<Object> values = frequency.valuesIterator();
        assertEquals(1, values.next());
        assertEquals(2, values.next());
        assertNull(values.next());
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        assertEquals(4, frequency.getCount(1L));
        assertEquals(4, frequency.getCount(2L));
        assertEquals(0, frequency.getCount(3L));
        assertEquals(4, frequency.getSumFreq());

        Iterator<Object> values = frequency.valuesIterator();
        assertEquals(1L, values.next());
        assertEquals(2L, values.next());
        assertNull(values.next());
    }

    @Test
    public void testAddValueDouble() {
        Frequency frequency = new Frequency();
        frequency.addValue(1.0);
        frequency.addValue(1.0);
        frequency.addValue(2.0);
        frequency.addValue(2.0);

        assertEquals(4, frequency.getCount(1.0));
        assertEquals(4, frequency.getCount(2.0));
        assertEquals(0, frequency.getCount(3.0));
        assertEquals(4, frequency.getSumFreq());

        Iterator<Object> values = frequency.valuesIterator();
        assertEquals(1.0, values.next().doubleValue());
        assertEquals(2.0, values.next().doubleValue());
        assertNull(values.next());
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');

        assertEquals(4, frequency.getCount('a'));
        assertEquals(4, frequency.getCount('b'));
        assertEquals(0, frequency.getCount('c'));
        assertEquals(4, frequency.getSumFreq());

        Iterator<Object> values = frequency.valuesIterator();
        assertEquals('a', values.next().charValue());
        assertEquals('b', values.next().charValue());
        assertNull(values.next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("foo");
        frequency.addValue("bar");
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();

        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(4, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(3));
        assertEquals(4, frequency.getCumFreq(Integer.MAX_VALUE));
        assertEquals(0, frequency.getCumFreq(Integer.MIN_VALUE));

        Iterator<Object> values = frequency.valuesIterator();
        assertEquals(1, values.next().intValue());
        assertEquals(2, values.next().intValue());
        assertNull(values.next());
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        double expected = 0.25;
        assertEquals(expected, frequency.getPct(1), 0.001);
        assertEquals(expected, frequency.getPct(2), 0.001);
        assertEquals(0, frequency.getPct(3), 0.001);

        expected = 0.5;
        assertEquals(expected, frequency.getCumPct(1), 0.001);
        assertEquals(expected, frequency.getCumPct(2), 0.001);
        assertEquals(1, frequency.getCumPct(Integer.MAX_VALUE), 0.001);
        assertEquals(0, frequency.getCumPct(Integer.MIN_VALUE), 0.001);

        Iterator<Object> values = frequency.valuesIterator();
        assertEquals(1, values.next().intValue());
        assertEquals(2, values.next().intValue());
        assertNull(values.next());
    }

    private void assertEmpty(Iterator<Object> iterator) {
        assertFalse(iterator.hasNext());
    }
}