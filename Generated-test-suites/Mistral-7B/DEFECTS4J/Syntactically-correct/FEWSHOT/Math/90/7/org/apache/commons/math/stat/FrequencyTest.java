package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyWithDefaultComparator() {
        Frequency frequency = new Frequency();

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        // Test getCount
        assertEquals(4, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount(3));

        // Test getCumFreq
        assertEquals(4, frequency.getCumFreq(1));
        assertEquals(5, frequency.getCumFreq(2));
        assertEquals(5, frequency.getCumFreq(3));

        // Test getPct
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.25, frequency.getPct(2), 0.001);
        assertNull(frequency.getPct(3));

        // Test getCumPct
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);

        // Test clear
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @Test
    public void testFrequencyWithCustomComparator() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };

        Frequency frequency = new Frequency(comparator);

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        // Test getCount
        assertEquals(4, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount(3));

        // Test getCumFreq
        assertEquals(4, frequency.getCumFreq(1));
        assertEquals(5, frequency.getCumFreq(2));
        assertEquals(5, frequency.getCumFreq(3));

        // Test getPct
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.25, frequency.getPct(2), 0.001);
        assertNull(frequency.getPct(3));

        // Test getCumPct
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);

        // Test clear
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }
}