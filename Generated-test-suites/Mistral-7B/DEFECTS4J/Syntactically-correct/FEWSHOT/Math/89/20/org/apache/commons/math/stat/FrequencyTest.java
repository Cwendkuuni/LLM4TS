package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.TreeMap;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency.freqTable);
        assertTrue(frequency.freqTable instanceof TreeMap);
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        assertNotNull(frequency.freqTable);
        assertTrue(frequency.freqTable instanceof TreeMap);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n2 \t 1 \t 0.2 \t 0.5333333333333333 \n";
        assertEquals(expected, frequency.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long expected = 4;
        assertEquals(expected, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long expected = 2;
        assertEquals(expected, frequency.getCount(1));
        assertEquals(expected, frequency.getCount(Long.valueOf(1)));
        assertEquals(expected, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(Long.valueOf(2)));
        assertEquals(1, frequency.getCount(Character.valueOf('2')));
        assertEquals(0, frequency.getCount('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue2() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object[]{});
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double expected = 0.3333333333333333;
        assertEquals(expected, frequency.getPct(1), 0.00001);
        double expected2 = 0.2;
        assertEquals(expected2, frequency.getPct(2), 0.00001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long expected = 1;
        assertEquals(expected, frequency.getCumFreq(1));
        long expected2 = 2;
        assertEquals(expected2, frequency.getCumFreq(1));
        long expected3 = 2;
        assertEquals(expected3, frequency.getCumFreq(2));
        long expected4 = 3;
        assertEquals(expected4, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double expected = 0.25;
        assertEquals(expected, frequency.getCumPct(1), 0.00001);
        double expected2 = 0.5;
        assertEquals(expected2, frequency.getCumPct(1), 0.00001);
        double expected3 = 0.75;
        assertEquals(expected3, frequency.getCumPct(2), 0.00001);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }
}