package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.valuesIterator().next());
    }

    @Test
    public void testFrequencyWithValues() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);

        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('b');

        assertEquals(4, freq.getSumFreq());
        assertEquals('a', freq.valuesIterator().next());
        assertEquals(2, freq.getCount('a'));
        assertEquals('b', freq.valuesIterator().next());
        assertEquals(2, freq.getCount('b'));

        Iterator it = freq.valuesIterator();
        assertEquals('a', it.next());
        assertEquals('a', it.next());
        assertEquals('b', it.next());
        assertEquals('b', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testFrequencyWithNullValue() {
        Frequency freq = new Frequency();
        assertNull(freq.getCount(null));
        freq.addValue(null);
        assertEquals(1, freq.getSumFreq());
        assertEquals(1, freq.getCount(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithNonComparableValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithNonComparableValues() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        freq.addValue('a');
        freq.addValue(1);
    }

    @Test
    public void testFrequencyClear() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);

        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('b');

        assertEquals(4, freq.getSumFreq());
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.valuesIterator().next());
    }

    @Test
    public void testFrequencyToString() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);

        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('b');

        String toString = freq.toString();
        String expected = "Value   Freq.   Pct.   Cum Pct.\n" +
                "a       2       50.0    50.0\n" +
                "b       2       50.0    100.0\n";
        assertTrue(toString.startsWith(expected));
    }
}