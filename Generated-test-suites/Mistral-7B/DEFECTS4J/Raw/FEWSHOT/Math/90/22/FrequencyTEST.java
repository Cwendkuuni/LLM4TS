package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        // Test empty frequency distribution
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        // Test empty frequency distribution with custom comparator
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        // Test adding an int value
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        // Test adding a long value
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        // Test adding a char value
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        // Test adding a Comparable value
        Comparable value = 3;
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
        // Test adding a value that is not comparable
        try {
            frequency.addValue(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        fail();
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().hashCode());
        assertEquals(2, iterator.next().hashCode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount(new Object()));
    }

    @Test
    public void testGetCountWhenValueIsNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertNull(frequency.getCount(new Object()));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
        assertEquals(0.6666666666666667, frequency.getPct(2), 0.0001);
        assertEquals(Double.NaN, frequency.getPct(new Object()));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3)); // should return 0
    }

    @Test
    public void testGetCumFreqWhenValueIsNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumFreq(new Object()));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.0001);
        assertEquals(0.6666666666666667, frequency.getCumPct(2), 0.0001);
        assertEquals(1.0, frequency.getCumPct(3), 0.0001); // should return 0
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 1 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.6666666666666667 \t 1.0\n";
        assertEquals(expected, frequency.toString());
    }

    private void assertEmpty(Iterator<Object> iterator) {
        assertFalse(iterator.hasNext());
    }
}