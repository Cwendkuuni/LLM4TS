package org.apache.commons.math.stat.test;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertTrue;
import org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(Collections.emptyMap(), frequency.freqTable);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> customComparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(customComparator);
        assertEquals(0, frequency.getSumFreq());
        assertEquals(Collections.emptyMap(), frequency.freqTable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
    }

    @Test
    public void testAddValueWithComparableIntValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 5.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 5.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testAddValueWithComparableLongValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1L));
        assertEquals(1.0 / 5.0, frequency.getPct(1L), 0.001);
        assertEquals(1.0 / 5.0, frequency.getCumPct(1L), 0.001);
    }

    @Test
    public void testAddValueWithComparableIntegerValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(5, frequency.getSumFreq());
        Map<Long, Long> expectedMap = new HashMap<>();
        expectedMap.put(1L, 5L);
        assertEquals(expectedMap, frequency.freqTable);
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(5), 0.001);
        assertEquals(0.0, frequency.getCumPct(2), 0.001);
    }

    @Test
    public void testAddValueWithComparableCharacterValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('A');
        assertEquals(5, frequency.getSumFreq());
        Map<Character, Long> expectedMap = new HashMap<>();
        expectedMap.put('A', 5L);
        assertEquals(expectedMap, frequency.freqTable);
        assertEquals(1.0 / 5.0, frequency.getPct('A'), 0.001);
        assertEquals(1.0 / 5.0, frequency.getCumPct('A'), 0.001);
        assertEquals(1.0 / 5.0, frequency.getCumPct('E'), 0.001);
        assertEquals(0.0, frequency.getCumPct('B'), 0.001);
    }

    @Test
    public void testAddValueWithComparableNegativeLongValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(-1L);
        assertEquals(-1, frequency.getCount(-1L));
        assertEquals(-1.0 / 5.0, frequency.getPct
                (-1), 0.001);
        assertEquals(-1.0 / 5.0, frequency.getCumPct(-1), 0.001);
        assertEquals(-1.0 / 5.0, frequency.getCumPct(0), 0.001);
        assertEquals(0.0, frequency.getCumPct(1), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddValueWithLongValueGreaterThanLongMax() {
        Frequency frequency = new Frequency();
        long maxLongValue = Long.MAX_VALUE;
        frequency.addValue(maxLongValue + 1);
    }

    @Test
    public void testClearMethod() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(Collections.emptyMap(), frequency.freqTable);
    }
}