package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Frequency freq = new Frequency(new NaturalComparator());
        assertNotNull(freq);
    }

    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        Iterator iterator = freq.valuesIterator();
        assertEquals(Long.valueOf(1), iterator.next());
        assertEquals(Long.valueOf(2), iterator.next());
        assertEquals(Long.valueOf(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(1, freq.getCount(Long.valueOf(1)));
        assertEquals(2, freq.getCount(Long.valueOf(2)));
        assertEquals(0, freq.getCount(Long.valueOf(3)));
    }

    @Test
    public void testGetCount_int() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetCount_char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
        assertEquals(1, freq.getCount('b'));
        assertEquals(0, freq.getCount('c'));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(0.3333333333333333, freq.getPct(Long.valueOf(1)), 0.00001);
        assertEquals(0.6666666666666666, freq.getPct(Long.valueOf(2)), 0.00001);
        assertEquals(NaN.NaN, freq.getPct(Long.valueOf(3)), 0.0);
    }

    @Test
    public void testGetPct_int() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(0.3333333333333333, freq.getPct(1), 0.00001);
        assertEquals(0.6666666666666666, freq.getPct(2), 0.00001);
        assertEquals(NaN.NaN, freq.getPct(3), 0.0);
    }

    @Test
    public void testGetPct_char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');
        assertEquals(0.3333333333333333, freq.getPct((char) 97), 0.00001);
        assertEquals(0.6666666666666666, freq.getPct((char) 98), 0.00001);
        assertEquals(NaN.NaN, freq.getPct((char) 99), 0.0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(1, freq.getCumFreq(Long.valueOf(1)));
        assertEquals(2, freq.getCumFreq(Long.valueOf(1)));
        assertEquals(2, freq.getCumFreq(Long.valueOf(2)));
        assertEquals(3, freq.getCumFreq(Long.valueOf(2)));
        assertEquals(3, freq.getCumFreq(Long.valueOf(3)));
    }

    @Test
    public void testGetCumFreq_int() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(3));
    }

    @Test
    public void testGetCumFreq_char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        assertEquals(1, freq.getCumFreq((char) 97));
        assertEquals(2, freq.getCumFreq((char) 97));
        assertEquals(2, freq.getCumFreq((char) 98));
        assertEquals(3, freq.getCumFreq((char) 98));
        assertEquals(3, freq.getCumFreq((char) 99));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.3333333333333333, freq.getCumPct(Long.valueOf(1)), 0.00001);
        assertEquals(0.6666666666666666, freq.getCumPct(Long.valueOf(2)), 0.00001);
        assertEquals(1.0, freq.getCumPct(Long.valueOf(3)), 0.00001);
    }

    @Test
    public void testGetCumPct_int() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.3333333333333333, freq.getCumPct(1), 0.00001);
        assertEquals(0.6666666666666666, freq.getCumPct(2), 0.00001);
        assertEquals(1.0, freq.getCumPct(3), 0.00001);
    }

    @Test
    public void testGetCumPct_char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');
        assertEquals(0.3333333333333333, freq.getCumPct((char) 97), 0.00001);
        assertEquals(0.6666666666666666, freq.getCumPct((char) 98), 0.00001);
        assertEquals(1.0, freq.getCumPct((char) 99), 0.00001);
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }
}