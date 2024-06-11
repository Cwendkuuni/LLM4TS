package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator<Integer> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        Iterator<Long> iterator = frequency.valuesIterator();
        assertEquals(5, iterator.next().longValue());
        assertEquals(3, iterator.next().longValue());
        assertTrue(iterator.hasNext());
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testFrequencyWithDefaultComparator() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        Iterator<Long> iterator = frequency.valuesIterator();
        assertEquals(5, iterator.next().longValue());
        assertEquals(3, iterator.next().longValue());
        assertTrue(iterator.hasNext());
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    @Test
    public void testAddValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(13, frequency.getSumFreq());
        Iterator<Long> iterator = frequency.valuesIterator();
        assertEquals(5, iterator.next().longValue());
        assertEquals(3, iterator.next().longValue());
        assertEquals(5, iterator.next().longValue());
        assertFalse(iterator.hasNext());
        frequency.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValuesWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalStateException.class)
    public void testToStringWithEmptyIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(13, frequency.getSumFreq());
        frequency.clear();
        String toString = frequency.toString();
        assertTrue(toString.contains("Value \t Freq. \t Pct. \t Cum Pct."));
        assertTrue(toString.contains("Value \t Freq. \t Pct. \t Cum Pct.\n"));
        assertTrue(toString.contains("Value\tFreq.\tPct.\tCum Pct.\n"));
        assertTrue(toString.contains("Value\t 0\t NaN\t NaN\n"));
        assertTrue(toString.length() > 1000);
    }

    @Test
    public void testToStringWithValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(13, frequency.getSumFreq());
        String toString = frequency.toString();
        assertTrue(toString.contains("Value \t Freq. \t Pct. \t Cum Pct."));
        assertTrue(toString.contains("Value \t 5\t 0.3846153846153846\t 0.3846153846153846\n"));
        assertTrue(toString.contains("Value \t 3\t 0.2307692307692308\t 0.6152746152746153\n"));
        assertTrue(toString.contains("Value \t 5\t 0.3846153846153846\t 1.0\n"));
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(2, frequency.getCount(5));
        assertEquals(1, frequency.getCount(3));
        assertEquals(0, frequency.getCount(4));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(0.3846153846153846, frequency.getPct(5), 0.001);
        assertEquals(0.2307692307692308, frequency.getPct(3), 0.001);
        assertEquals(0.0, frequency.getPct(4), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(5, frequency.getCumFreq(5));
        assertEquals(8, frequency.getCumFreq(3));
        assertEquals(13, frequency.getCumFreq(5));
        assertEquals(13, frequency.getCumFreq(Long.MAX_VALUE));
        assertEquals(0, frequency.getCumFreq(4));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);
        assertEquals(0.3846153846153846, frequency.getPct(5), 0.001);
        assertEquals(0.2307692307692308, frequency.getPct(3), 0.001);
        assertEquals(1.0, frequency.getPct(5), 0.001);
        assertEquals(1.0, frequency.getPct(Long.MAX_VALUE), 0.001);
        assertEquals(0.0, frequency.getPct(4), 0.001);
        assertEquals(0.3846153846153846, frequency.getPct(5), 0.001);
        assertEquals(0.6152746152746153, frequency.getPct(3), 0.001);
    }

    private void assertEmpty(Iterator<Long> iterator) {
        assertFalse(iterator.hasNext());
    }
}