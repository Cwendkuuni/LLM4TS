package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(1.0 / 4.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(0.75, frequency.getCumPct(2), 0.001);
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }
}