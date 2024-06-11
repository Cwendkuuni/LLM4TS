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
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.getCount(null));
        assertNull(freq.getCumFreq(null));
        assertNull(freq.getCumPct(null));

        freq.addValue(1);
        assertEquals(1, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getCumPct(1), 0.001);

        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCumFreq(1));
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);

        freq.addValue(2);
        assertEquals(3, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));
        assertEquals(3, freq.getCumFreq(1));
        assertEquals(2.0 / 3.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        assertEquals(2.0 / 3.0, freq.getCumPct(3), 0.001);
    }

    @Test
    public void testComparator() {
        Frequency freq = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2) * -1;
            }
        });

        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(3, freq.getSumFreq());
        assertEquals(2, freq.getCount(2));
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, freq.getCumPct(2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableWithExistingValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue("abc");
    }
}