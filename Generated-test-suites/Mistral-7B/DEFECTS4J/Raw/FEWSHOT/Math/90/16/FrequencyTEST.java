package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        Iterator values = frequency.valuesIterator();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(values.hasNext());
    }

    @Test
    public void testFrequencyWithDefaultComparator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        Iterator values = frequency.valuesIterator();
        assertSame(Integer.valueOf(1), values.next());
        assertEquals(2, frequency.getCount(1));
        assertEquals(3, frequency.getSumFreq());

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.4 \t 0.4\n2 \t 1 \t 0.2 \t 0.6\n", frequency.toString());
    }

    @Test
    public void testFrequencyWithComparator() {
        Frequency frequency = new Frequency(new NaturalComparator());
        frequency.addValue(2);
        frequency.addValue(1);

        Iterator values = frequency.valuesIterator();
        assertSame(Integer.valueOf(1), values.next());
        assertEquals(1, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(3, frequency.getSumFreq());

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 2 \t 0.6666666666666667 \t 0.6666666666666667\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("foo");
    }

    @Test
    public void testFrequencyAddValueToExistingEntry() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        Iterator values = frequency.valuesIterator();
        assertSame(Integer.valueOf(1), values.next());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getSumFreq());

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.5 \t 0.5\n", frequency.toString());
    }

    @Test
    public void testFrequencyAddValueToNonExistingEntry() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);

        Iterator values = frequency.valuesIterator();
        assertSame(Integer.valueOf(3), values.next());
        assertEquals(1, frequency.getCount(3));
        assertEquals(1, frequency.getSumFreq());

        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n3 \t 1 \t 1.0 \t 1.0\n", frequency.toString());
    }

    @Test
    public void testFrequencyClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();

        Iterator values = frequency.valuesIterator();
        assertFalse(values.hasNext());
        assertEquals(0, frequency.getSumFreq());
    }
}