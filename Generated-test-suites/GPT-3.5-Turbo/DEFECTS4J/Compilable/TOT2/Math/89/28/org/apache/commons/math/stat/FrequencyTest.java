package org.apache.commons.math.stat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Comparator;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        assertNotNull(frequency.valuesIterator());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testAddValueComparator() {
        Comparator<Integer> comparator = (a, b) -> Integer.compare(b, a);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(10);
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(10));
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(4);
        assertEquals(1, frequency.getCount(4));
        assertNotEquals(1, frequency.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(1.0, frequency.getPct(3), 0.001);
        assertNotEquals(0.5, frequency.getPct(3), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCumFreq(2));
        assertNotEquals(1, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        assertNotEquals(0.5, frequency.getCumPct(3), 0.001);
    }
}