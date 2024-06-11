package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.valuesIterator().isEmpty());

        freq.addValue(1);
        assertEquals(1, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        List<Object> values = new ArrayList<>(freq.valuesIterator());
        assertEquals(1, values.size());
        assertEquals(1, values.get(0).longValue());

        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));

        freq.addValue(2);
        assertEquals(3, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));

        List<Object> values2 = new ArrayList<>(freq.valuesIterator());
        assertEquals(2, values2.size());
        assertTrue(Arrays.asList(1L, 2L).containsAll(values2));

        Frequency emptyFreq = new Frequency();
        assertEquals(0, emptyFreq.getSumFreq());
        assertTrue(emptyFreq.valuesIterator().isEmpty());

        Frequency comparatorFreq = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });
        comparatorFreq.addValue(1);
        comparatorFreq.addValue(2);
        comparatorFreq.addValue(1);

        assertEquals(3, comparatorFreq.getSumFreq());
        assertEquals(2, comparatorFreq.getCount(1));
        assertEquals(1, comparatorFreq.getCount(2));

        List<Object> valuesComparator = new ArrayList<>(comparatorFreq.valuesIterator());
        assertEquals(2, valuesComparator.size());
        assertTrue(Arrays.asList(1L, 2L).containsAll(valuesComparator));

        comparatorFreq.addValue(1);
        List<Object> valuesComparator2 = new ArrayList<>(comparatorFreq.valuesIterator());
        assertEquals(3, valuesComparator2.size());
        assertTrue(Arrays.asList(1L, 1L, 2L).containsAll(valuesComparator2));

        comparatorFreq.addValue(3);
        List<Object> valuesComparator3 = new ArrayList<>(comparatorFreq.valuesIterator());
        assertEquals(4, valuesComparator3.size());
        assertTrue(Arrays.asList(1L, 1L, 2L, 3L).containsAll(valuesComparator3));

        comparatorFreq.clear();
        assertEquals(0, comparatorFreq.getSumFreq());
        assertTrue(comparatorFreq.valuesIterator().isEmpty());
    }

    @Test
    public void testFrequencyWithNullValues() {
        Frequency freq = new Frequency();
        assertNull(freq.getCount(null));
        freq.addValue(null);
        assertEquals(1, freq.getSumFreq());
        assertNull(freq.getCount(null));
        List<Object> values = new ArrayList<>(freq.valuesIterator());
        assertEquals(1, values.size());
        assertEquals(null, values.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Object() {
            @Override
            public int compareTo(Object o) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValueLong() {
        Frequency freq = new Frequency();
        freq.addValue("abc");
    }

    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 25.0% \t 25.0% \n2 \t 1 \t 12.5% \t 37.5% \n\nTotal: 3\n";
        String result = freq.toString();
        assertEquals(expected, result);
    }
}