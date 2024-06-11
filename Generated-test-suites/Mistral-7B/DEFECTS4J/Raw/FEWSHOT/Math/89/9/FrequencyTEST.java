package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

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
        assertEquals(0, frequency.getCount(3));
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(1.0, frequency.getPct(3), 0.001);

        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);

        Comparator comparator = new NaturalComparator();
        Frequency frequencyComparator = new Frequency(comparator);
        frequencyComparator.addValue(2);
        frequencyComparator.addValue(1);
        assertEquals(3, frequencyComparator.getSumFreq());
        assertEquals(2, frequencyComparator.getCount(2));
        assertEquals(1, frequencyComparator.getCount(1));
        assertEquals(0, frequencyComparator.getCount(3));
        assertEquals(0.67, frequencyComparator.getPct(2), 0.001);
        assertEquals(0.33, frequencyComparator.getPct(1), 0.001);
        assertEquals(1.0, frequencyComparator.getPct(3), 0.001);

        assertEquals(0.67, frequencyComparator.getCumPct(2), 0.001);
        assertEquals(0.67, frequencyComparator.getCumPct(1), 0.001);
        assertEquals(1.0, frequencyComparator.getCumPct(3), 0.001);
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };

        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(1));
        assertEquals(0, frequency.getCount(3));
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getPct(3), 0.001);

        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableWithComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                throw new UnsupportedOperationException();
            }
        };

        Frequency frequency = new Frequency(comparator);
        frequency.addValue(new Object());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }
}