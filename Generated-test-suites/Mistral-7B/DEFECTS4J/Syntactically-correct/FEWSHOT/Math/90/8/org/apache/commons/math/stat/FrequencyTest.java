package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test cases for the {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test Frequency with default comparator.
     */
    @Test
    public void testDefaultFrequency() {
        Frequency frequency = new Frequency();
        // Add values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        // Test methods
        assertEquals(6, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(1L));
        assertEquals(3, frequency.getCount(1.0));
        assertEquals(3, frequency.getCount(1.0f));
        assertEquals(3, frequency.getCount(Character.valueOf('1')));
        assertEquals(3, frequency.getCount('1'));
        assertEquals(3, frequency.getCount('1', new NaturalComparator()));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(2L));
        assertEquals(1, frequency.getCount(2.0));
        assertEquals(1, frequency.getCount(2.0f));
        assertEquals(1, frequency.getCount(Character.valueOf('2')));
        assertEquals(1, frequency.getCount('2'));
        assertEquals(1, frequency.getCount('2', new NaturalComparator()));
        assertEquals(2, frequency.getCount(3));
        assertEquals(2, frequency.getCount(3L));
        assertEquals(2, frequency.getCount(3.0));
        assertEquals(2, frequency.getCount(3.0f));
        assertEquals(2, frequency.getCount(Character.valueOf('3')));
        assertEquals(2, frequency.getCount('3'));
        assertEquals(2, frequency.getCount('3', new NaturalComparator()));
        // Test getCumFreq and getCumPct
        assertEquals(0, frequency.getCumFreq(0));
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(6, frequency.getCumFreq(2));
        assertEquals(6, frequency.getCumFreq(3));
        assertEquals(3.0 / 6.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(2), 0.001);
        assertEquals(2.0 / 6.0, frequency.getPct(3), 0.001);
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(0.8333333333333333, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        // Test clear
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        // Test iterator
        assertEquals(Collections.emptyList(), Arrays.asList(frequency.valuesIterator().toArray()));
    }

    /**
     * Test Frequency with custom comparator.
     */
    @Test
    public void testCustomFrequency() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);
        // Add values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        // Test methods
        assertEquals(6, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(1L));
        assertEquals(3, frequency.getCount(1.0));
        assertEquals(3, frequency.getCount(1.0f));
        assertEquals(3, frequency.getCount(Character.valueOf('1')));
        assertEquals(3, frequency.getCount('1'));
        assertEquals(3, frequency.getCount('1', new NaturalComparator()));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(2L));
        assertEquals(1, frequency.getCount(2.0));
        assertEquals(1, frequency.getCount(2.0f));
        assertEquals(1, frequency.getCount(Character.valueOf('2')));
        assertEquals(1, frequency.getCount('2'));
        assertEquals(1, frequency.getCount('2', new NaturalComparator()));
        assertEquals(2, frequency.getCount(3));
        assertEquals(2, frequency.getCount(3L));
        assertEquals(2, frequency.getCount(3.0));
        assertEquals(2, frequency.getCount(3.0f));
        assertEquals(2, frequency.getCount(Character.valueOf('3')));
        assertEquals(2, frequency.getCount('3'));
        assertEquals(2, frequency.getCount('3', new NaturalComparator()));
        // Test getCumFreq and getCumPct
        assertEquals(0, frequency.getCumFreq(0));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(6, frequency.getCumFreq(3));
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        // Test clear
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        // Test iterator
        assertEquals(Collections.emptyList(), Arrays.asList(frequency.valuesIterator().toArray()));
    }

    /**
     * Test Frequency with mixed types.
     */
    @Test
    public void testMixedFrequency() {
        Frequency frequency = new Frequency();
        // Add values
        frequency.addValue(1);
        frequency.addValue('1');
        frequency.addValue(1.0);
        frequency.addValue('1.0');
        // Test methods
        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(Character.valueOf('1')));
        assertEquals(1, frequency.getCount(1.0));
        assertEquals(1, frequency.getCount(Character.valueOf('1.0')));
        assertEquals(1, frequency.getCount(1L));
        assertEquals(1, frequency.getCount('1'));
        assertEquals(1, frequency.getCount('1.0'));
        assertEquals(1, frequency.getCount(1.0f));
        assertEquals(1, frequency.getCount(Character.valueOf('1.0')));
        assertEquals(0, frequency.getCumFreq(0));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq('1'));
        assertEquals(1, frequency.getCumFreq(1.0));
        assertEquals(1, frequency.getCumFreq(Character.valueOf('1.0')));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq('1'));
        assertEquals(2, frequency.getCumFreq(1.0));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('1.0')));
        assertEquals(2, frequency.getCumFreq(1L));
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq('1'));
        assertEquals(3, frequency.getCumFreq(1.0));
        assertEquals(3, frequency.getCumFreq(Character.valueOf('1.0')));
        assertEquals(3, frequency.getCumFreq(1L));
    }

}