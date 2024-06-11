package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator<Comparable<?>> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testFrequencyToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333\t 0.3333333333333333\n2 \t 1 \t 0.25\t 0.5833333333333333\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFrequencyEmptyMethods() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(null));
        assertNull(frequency.getCount('a'));
        assertNull(frequency.getCount(1));
        assertNull(frequency.getCount(1.0));
        assertNull(frequency.getPct(null));
        assertNull(frequency.getPct('a'));
        assertNull(frequency.getPct(1));
        assertNull(frequency.getPct(1.0));
        assertNull(frequency.getCumFreq(null));
        assertNull(frequency.getCumFreq('a'));
        assertNull(frequency.getCumFreq(1));
        assertNull(frequency.getCumFreq(1.0));
        assertNull(frequency.getCumPct(null));
        assertNull(frequency.getCumPct('a'));
        assertNull(frequency.getCumPct(1));
        assertNull(frequency.getCumPct(1.0));
    }

    @Test
    public void testFrequencyAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long sumFreq = frequency.getSumFreq();
        assertEquals(10, sumFreq);
        long count1 = frequency.getCount(1);
        assertEquals(3, count1);
        long count2 = frequency.getCount(2);
        assertEquals(3, count2);
        long count3 = frequency.getCount(3);
        assertEquals(4, count3);
        double pct1 = frequency.getPct(1);
        assertEquals(0.3, pct1, 0.001);
        double pct2 = frequency.getPct(2);
        assertEquals(0.3, pct2, 0.001);
        double pct3 = frequency.getPct(3);
        assertEquals(0.4, pct3, 0.001);
        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(3, cumFreq1);
        long cumFreq2 = frequency.getCumFreq(2);
        assertEquals(6, cumFreq2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(10, cumFreq3);
        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.3, cumPct1, 0.001);
        double cumPct2 = frequency.getCumPct(2);
        assertEquals(0.6, cumPct2, 0.001);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(1, cumPct3, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueIllegalArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue("abc");
    }

    @Test
    public void testFrequencyClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        Iterator<Comparable<?>> valuesIterator = frequency.valuesIterator();
        int count = 0;
        while (valuesIterator.hasNext()) {
            Comparable<?> value = valuesIterator.next();
            long countValue = frequency.getCount(value);
            assertEquals(frequency.getCount(value), countValue);
            count++;
        }
        assertEquals(10, count);
    }
}