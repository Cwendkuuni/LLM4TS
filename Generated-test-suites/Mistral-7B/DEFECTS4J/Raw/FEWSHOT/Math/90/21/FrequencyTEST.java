package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testEmptyConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(Collections.emptyMap(), frequency.freqTable);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertEquals(Collections.emptyMap(), frequency.freqTable);
    }

    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, (int) frequency.getCount(1));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, (int) frequency.getCount(2));
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, (int) frequency.getCount('a'));
    }

    @Test
    public void testAddValueSameValueMultipleTimes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, (int) frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparableToPrevious() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Integer(2));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(Collections.emptyMap(), frequency.freqTable);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, (int) frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, (int) frequency.getCount(1));
        assertEquals(1, (int) frequency.getCount(2));
        assertNull(frequency.getCount(new Object()));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.3333333333333333, frequency.getPct(2), 0.001);
        assertNull(frequency.getPct(new Object()));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, (int) frequency.getCumFreq(1));
        assertEquals(2, (int) frequency.getCumFreq(1
                + Frequency.NaturalComparator.COMPARATOR.compare(1, 2)));
        assertEquals(3, (int) frequency.getCumFreq(2));
        assertNull(frequency.getCumFreq(new Object()));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.001);
        assertEquals(0.6666666666666667, frequency.getCumPct(1
                + Frequency.NaturalComparator.COMPARATOR.compare(1, 2)), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        assertNull(frequency.getCumPct(new Object()));
    }

    @Test
    public void testAddValueAndGetFreqsWithComparator() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Number) o1).intValue() - ((Number) o2).intValue();
            }
        };

        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(1, (int) frequency.getCount(1));
        assertEquals(1, (int) frequency.getCount(2));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        String expectedResult = "Value \t Freq. \t Pct. \t Cum Pct.\n"
                + "1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n"
                + "2 \t 1 \t 0.3333333333333333 \t 0.6666666666666667\n";
        assertEquals(expectedResult, frequency.toString());
    }
}