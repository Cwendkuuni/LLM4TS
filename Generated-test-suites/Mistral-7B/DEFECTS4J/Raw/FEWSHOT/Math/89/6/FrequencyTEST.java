package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.toString());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.toString());
    }

    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 100.0% \t 100.0%\n", freq.toString());
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        assertEquals(1, freq.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 100.0% \t 100.0%\n", freq.toString());
    }

    @Test
    public void testAddValueString() {
        Frequency freq = new Frequency();
        freq.addValue("1");
        assertEquals(1, freq.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 100.0% \t 100.0%\n", freq.toString());
    }

    @Test
    public void testAddValueCharacter() {
        Frequency freq = new Frequency();
        freq.addValue('1');
        assertEquals(1, freq.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 100.0% \t 100.0%\n", freq.toString());
    }

    @Test
    public void testAddValueInvalidType() {
        Frequency freq = new Frequency();
        try {
            freq.addValue(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddValueDuplicateValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 50.0% \t 50.0%\n", freq.toString());
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertEquals("", freq.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        Iterator values = freq.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals(1, values.next().longValue());
        assertFalse(values.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        freq.addValue(1);
        assertEquals(1, freq.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCount(1));
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCount(1L));
        freq.addValue(1L);
        assertEquals(1, freq.getCount(1L));
    }

    @Test
    public void testGetCountString() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCount("1"));
        freq.addValue("1");
        assertEquals(1, freq.getCount("1"));
    }

    @Test
    public void testGetCountCharacter() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCount('1'));
        freq.addValue('1');
        assertEquals(1, freq.getCount('1'));
    }

    @Test
    public void testGetCountInvalidType() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCount(new Object()));
    }

    @Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct(1));
        freq.addValue(1);
        assertEquals(1.0, freq.getPct(1), 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct(1L));
        freq.addValue(1L);
        assertEquals(1.0, freq.getPct(1L), 0.001);
    }

    @Test
    public void testGetPctString() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct("1"));
        freq.addValue("1");
        assertEquals(1.0, freq.getPct("1"), 0.001);
    }

    @Test
    public void testGetPctCharacter() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct('1'));
        freq.addValue('1');
        assertEquals(1.0, freq.getPct('1'), 0.001);
    }

    @Test
    public void testGetPctInvalidType() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct(new Object()));
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq(1));
        freq.addValue(1);
        assertEquals(1, freq.getCumFreq(1));
        freq.addValue(2);
        assertEquals(3, freq.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq(1L));
        freq.addValue(1L);
        assertEquals(1, freq.getCumFreq(1L));
        freq.addValue(2L);
        assertEquals(3, freq.getCumFreq(1L));
    }

    @Test
    public void testGetCumFreqString() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq("1"));
        freq.addValue("1");
        assertEquals(1, freq.getCumFreq("1"));
        freq.addValue("2");
        assertEquals(2, freq.getCumFreq("1"));
        freq.addValue("3");
        assertEquals(3, freq.getCumFreq("1"));
    }

    @Test
    public void testGetCumFreqCharacter() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq('1'));
        freq.addValue('1');
        assertEquals(1, freq.getCumFreq('1'));
        freq.addValue('2');
        assertEquals(2, freq.getCumFreq('1'));
        freq.addValue('3');
        assertEquals(3, freq.getCumFreq('1'));
    }

    @Test
    public void testGetCumFreqInvalidType() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getCumFreq(new Object()));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct(1));
        freq.addValue(1);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        freq.addValue(2);
        assertEquals(2.0, freq.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct(1L));
        freq.addValue(1L);
        assertEquals(1.0, freq.getCumPct(1L), 0.001);
        freq.addValue(2L);
        assertEquals(2.0, freq.getCumPct(1L), 0.001);
    }

    @Test
    public void testGetCumPctString() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct("1"));
        freq.addValue("1");
        assertEquals(1.0, freq.getCumPct("1"), 0.001);
        freq.addValue("2");
        assertEquals(2.0, freq.getCumPct("1"), 0.001);
        freq.addValue("3");
        assertEquals(3.0, freq.getCumPct("1"), 0.001);
    }

    @Test
    public void testGetCumPctCharacter() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct('1'));
        freq.addValue('1');
        assertEquals(1.0, freq.getCumPct('1'), 0.001);
        freq.addValue('2');
        assertEquals(2.0, freq.getCumPct('1'), 0.001);
        freq.addValue('3');
        assertEquals(3.0, freq.getCumPct('1'), 0.001);
    }

    @Test
    public void testGetCumPctInvalidType() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct(new Object()));
    }
}