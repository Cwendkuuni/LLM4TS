package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testFrequencyWithComparatorConstructor() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(10L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('X');
    }

    @Test
    public void testAddValueSameValueMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(2, frequency.getCount(3));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testGetCountUncomparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertNull(frequency.getCount(new Object()));
    }

    @Test
    public void testGetCountUncomparableInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertNull(frequency.getCount(10));
    }

    @Test
    public void testGetCountUncomparableLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertNull(frequency.getCount(10L));
    }

    @Test
    public void testGetCountUncomparableChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertNull(frequency.getCount('X'));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.33333333333333336, frequency.getPct(1), 0.0001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        frequency.addValue(1L);
        assertEquals(0.66666666666666668, frequency.getPct(2), 0.0001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getPct('a'), 0.0001);
    }

    @Test
    public void testGetPctUncomparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getPct(new Object()));
    }

    @Test
    public void testGetPctUncomparableInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getPct(10));
    }

    @Test
    public void testGetPctUncomparableLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getPct(10L));
    }

    @Test
    public void testGetPctUncomparableChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertNull(frequency.getPct('X'));
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(3)); // out of bounds
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1, frequency.getCumFreq(2L));
        assertEquals(3, frequency.getCumFreq(3L)); // out of bounds
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(1, frequency.getCumFreq('b'));
        assertEquals(2, frequency.getCumFreq('c')); // out of bounds
    }

    @Test
    public void testGetCumFreqUncomparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumFreq(new Object()));
    }

    @Test
    public void testGetCumFreqUncomparableInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumFreq(10));
    }

    @Test
    public void testGetCumFreqUncomparableLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCumFreq(10L));
    }

    @Test
    public void testGetCumFreqUncomparableChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertNull(frequency.getCumFreq('X'));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.33333333333333336, frequency.getCumPct(1), 0.0001);
        assertEquals(0.66666666666666668, frequency.getCumPct(2), 0.0001);
        assertEquals(1, frequency.getCumPct(3)); // out of bounds
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(0.33333333333333336, frequency.getCumPct(1L), 0.0001);
        assertEquals(0.66666666666666668, frequency.getCumPct(2L), 0.0001);
        assertEquals(1, frequency.getCumPct(3L)); // out of bounds
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.5, frequency.getCumPct('a'), 0.0001);
        assertEquals(0.5, frequency.getCumPct('b'), 0.0001);
        assertEquals(1, frequency.getCumPct('c')); // out of bounds
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().hashCode());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().hashCode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 1 \t 0.33333333333333336 \t 0.33333333333333336\n2 \t 1 \t 0.66666666666666668 \t 1.0\n";
        assertEquals(expected, frequency.toString());
    }
}