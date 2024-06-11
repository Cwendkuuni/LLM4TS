package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testAddValue_Object() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount("test"));
        assertNull(frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_NonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValue_ComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 100.0% \t 100.0%\n", frequency.toString());
    }

    @Test
    public void testAddValue_DifferentTypes_Integer() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(1L);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1, frequency.getCount(1L));
    }

    @Test
    public void testAddValue_DifferentTypes_Long() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1L);
        frequency.addValue(Long.valueOf(1));
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(1L));
        assertEquals(1, frequency.getCount(Long.valueOf(1)));
    }

    @Test
    public void testAddValue_DifferentTypes_Character() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue('A');
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1, frequency.getCount(Character.valueOf('a')));
        assertEquals(1, frequency.getCount('A'));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertFalse(frequency.toString().contains("100.0%"));
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Iterator<Object> valuesIterator = frequency.valuesIterator();
        assertEquals(1, valuesIterator.next().intValue());
        assertEquals(2, valuesIterator.next().intValue());
        assertEquals(1, valuesIterator.next().intValue());
        assertFalse(valuesIterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(4, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(0.42857142857142854, frequency.getPct(1), Precision.tolerableDelta(0.0));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(0.25, frequency.getCumPct(1), Precision.tolerableDelta(0.0));
        assertEquals(0.5, frequency.getCumPct(2), Precision.tolerableDelta(0.0));
        assertEquals(1.0, frequency.getCumPct(3), Precision.tolerableDelta(0.0));
    }
}