package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency.java
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator
     */
    @Test
    public void testFrequencyDefaultComparator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator iterator = frequency.valuesIterator();
        int count = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            long frequencyValue = frequency.getCount(value);
            assertEquals("Incorrect frequency count for " + value, 2, frequencyValue);
            count++;
        }
        assertEquals("Incorrect number of values added", 3, count);

        long sumFreq = frequency.getSumFreq();
        assertEquals("Incorrect sum frequency", 3, sumFreq);

        long cumFreq = frequency.getCount("1");
        assertEquals("Incorrect cumulative frequency for 1", 2, cumFreq);

        double pct = frequency.getPct(1);
        assertEquals("Incorrect percentage of 1", 0.6666666666666666, pct, 0.000001);

        double cumPct = frequency.getCumPct(1);
        assertEquals("Incorrect cumulative percentage of 1", 0.6666666666666666, cumPct, 0.000001);

        cumPct = frequency.getCumPct(2);
        assertEquals("Incorrect cumulative percentage of 2", 1.0, cumPct, 0.000001);

        assertNull("toString should return null when no values have been added", frequency.toString());

        frequency.toString();
    }

    /**
     * Test constructor with custom comparator
     */
    @Test
    public void testFrequencyCustomComparator() {
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

        Iterator iterator = frequency.valuesIterator();
        int count = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            long frequencyValue = frequency.getCount(value);
            assertEquals("Incorrect frequency count for " + value, 2, frequencyValue);
            count++;
        }
        assertEquals("Incorrect number of values added", 3, count);

        long sumFreq = frequency.getSumFreq();
        assertEquals("Incorrect sum frequency", 3, sumFreq);

        long cumFreq = frequency.getCount("1");
        assertEquals("Incorrect cumulative frequency for 1", 2, cumFreq);

        double pct = frequency.getPct(1);
        assertEquals("Incorrect percentage of 1", 0.6666666666666666, pct, 0.000001);

        double cumPct = frequency.getCumPct(1);
        assertEquals("Incorrect cumulative percentage of 1", 0.6666666666666666, cumPct, 0.000001);

        cumPct = frequency.getCumPct(2);
        assertEquals("Incorrect cumulative percentage of 2", 1.0, cumPct, 0.000001);

        assertNull("toString should return null when no values have been added", frequency.toString());

        frequency.toString();
    }

    /**
     * Test exception when adding non-comparable values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('A');
    }

    /**
     * Test exception when adding null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(null);
    }

    /**
     * Test exception when adding non-integer and non-character values
     */
    @Test(expected = ClassCastException.class)
    public void testAddNonIntegerOrCharacterValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(new Object());
    }
}