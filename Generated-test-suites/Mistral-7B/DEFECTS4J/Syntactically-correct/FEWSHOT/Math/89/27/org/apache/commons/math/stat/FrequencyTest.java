package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(2, frequency.getCount(Integer.valueOf(1)));
        assertEquals(2, frequency.getSumFreq());
        assertEquals(1.0, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);

        frequency.addValue(Integer.valueOf(2));
        assertEquals(3, frequency.getCount(Integer.valueOf(1)));
        assertEquals(3, frequency.getCount(Integer.valueOf(2)));
        assertEquals(3, frequency.getSumFreq());
        assertEquals(0.5, frequency.getPct(Integer.valueOf(2)), 0.001);
        assertEquals(0.5, frequency.getCumPct(Integer.valueOf(2)), 0.001);
        assertEquals(0.5, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);

        frequency.addValue(Integer.valueOf(1));
        assertEquals(4, frequency.getCount(Integer.valueOf(1)));
        assertEquals(4, frequency.getCount(Integer.valueOf(2)));
        assertEquals(5, frequency.getSumFreq());
        assertEquals(0.4, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.4, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.6, frequency.getCumPct(Integer.valueOf(2)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue("not a number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueIncomparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Character.valueOf('a'));
    }

    @Test
    public void testFrequencyClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.clear();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));

        Iterator<Object> valuesIterator = frequency.valuesIterator();
        assertEquals(Integer.valueOf(1), valuesIterator.next());
        assertEquals(Integer.valueOf(1), valuesIterator.next());
        assertEquals(Integer.valueOf(2), valuesIterator.next());
    }

    @Test
    public void testFrequencyGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(2, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1, frequency.getCount(Integer.valueOf(2)));
    }

    @Test
    public void testFrequencyGetCountNull() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testFrequencyGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(0.5, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.333, frequency.getPct(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testFrequencyGetPctNull() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(null));
    }

    @Test
    public void testFrequencyGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(1, frequency.getCumFreq(Integer.valueOf(1)));
        assertEquals(2, frequency.getCumFreq(Integer.valueOf(2)));
        assertEquals(3, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(3, frequency.getCumFreq(null));
    }

    @Test
    public void testFrequencyGetCumFreqNull() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCumFreq(null));
    }

    @Test
    public void testFrequencyGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(0.333, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.667, frequency.getCumPct(Integer.valueOf(2)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        assertEquals(1.0, frequency.getCumPct(null), 0.001);
    }

    @Test
    public void testFrequencyGetCumPctNull() {
        Frequency frequency = new Frequency();
        assertEquals(0.0, frequency.getCumPct(null), 0.001);
    }
}