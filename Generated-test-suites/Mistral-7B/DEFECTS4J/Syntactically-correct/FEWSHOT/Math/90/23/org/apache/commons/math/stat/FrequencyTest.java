package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FrequencyTest {

    private Frequency freq;
    private Comparator comparator;

    public FrequencyTest(Comparator comparator) {
        this.comparator = comparator;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { null },
                { Collections.reverseOrder() },
                { new NaturalComparator() }
        });
    }

    @Test
    public void testConstructor() {
        freq = new Frequency();
        assertEquals(Frequency.class, freq.getClass());
        assertNull(comparator, freq.getComparator());
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.isEmpty());

        freq = new Frequency(comparator);
        assertEquals(Frequency.class, freq.getClass());
        assertSame(comparator, freq.getComparator());
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.isEmpty());
    }

    @Test
    public void testAddValue() {
        freq = new Frequency();
        freq.addValue('A');
        assertEquals(Long.valueOf(1), freq.get(Character.valueOf('A')));
        assertEquals(1, freq.getSumFreq());

        freq.addValue('B');
        assertEquals(Long.valueOf(1), freq.get(Character.valueOf('B')));
        assertEquals(2, freq.getSumFreq());

        freq.addValue('A');
        assertEquals(Long.valueOf(2), freq.get(Character.valueOf('A')));

        try {
            freq.addValue("foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        freq.addValue(1);
        assertEquals(Long.valueOf(2), freq.get(Long.valueOf(1)));

        try {
            freq.addValue(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        freq.addValue(null);
        assertEquals(Long.valueOf(0), freq.get(null));

        freq.addValue(-1);
        assertEquals(Long.valueOf(0), freq.get(Long.valueOf(-1)));

        freq.addValue('Z');
        assertEquals(Long.valueOf(1), freq.get(Character.valueOf('Z')));

        freq.addValue('A');
        assertEquals(Long.valueOf(3), freq.get(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Long.valueOf(4), freq.get(Character.valueOf('A')));

        freq.clear();
        assertTrue(freq.isEmpty());
    }

    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('C');
        freq.addValue('A');
        freq.addValue('A');

        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n"
                + "A       \t 4       \t 40.0% \t 40.0%\n"
                + "B       \t 1       \t 10.0% \t 50.0%\n"
                + "C       \t 1       \t 10.0% \t 60.0%\n";

        String result;
        if (comparator == null) {
            result = freq.toString();
        } else {
            Frequency freq2 = new Frequency(comparator);
            freq2.addValue('C');
            freq2.addValue('B');
            freq2.addValue('A');
            freq2.addValue('A');
            freq2.addValue('A');
            result = freq2.toString();
        }

        assertEquals(expected, result);
    }

    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('C');
        freq.addValue('A');
        freq.addValue('A');

        Iterator<Comparable> it = freq.valuesIterator();
        assertEquals(Character.valueOf('A'), it.next());
        assertEquals(Character.valueOf('B'), it.next());
        assertEquals(Character.valueOf('C'), it.next());
        assertEquals(Character.valueOf('A'), it.next());
        assertEquals(Character.valueOf('A'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());

        freq.addValue('A');
        assertEquals(1, freq.getSumFreq());

        freq.addValue('B');
        assertEquals(2, freq.getSumFreq());

        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        assertEquals(Long.valueOf(0), freq.get(null));
        assertEquals(Long.valueOf(0), freq.get(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Long.valueOf(1), freq.get(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Long.valueOf(2), freq.get(Character.valueOf('A')));

        freq.addValue('B');
        assertEquals(Long.valueOf(1), freq.get(Character.valueOf('B')));

        freq.clear();
        assertEquals(Long.valueOf(0), freq.get(Character.valueOf('A')));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct(null));
        assertEquals(Double.NaN, freq.getPct(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Double.valueOf(0.0), freq.getPct(null));
        assertEquals(Double.valueOf(100.0), freq.getPct(Character.valueOf('A')));

        freq.addValue('B');
        assertEquals(Double.valueOf(0.0), freq.getPct(null));
        assertEquals(Double.valueOf(50.0), freq.getPct(Character.valueOf('B')));

        freq.addValue('A');
        assertEquals(Double.valueOf(40.0), freq.getPct(Character.valueOf('A')));

        freq.clear();
        assertEquals(Double.valueOf(0.0), freq.getPct(null));
        assertEquals(Double.valueOf(0.0), freq.getPct(Character.valueOf('A')));
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        assertEquals(Long.valueOf(0), freq.getCumFreq(null));
        assertEquals(Long.valueOf(0), freq.getCumFreq(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Long.valueOf(1), freq.getCumFreq(Character.valueOf('A')));

        freq.addValue('B');
        assertEquals(Long.valueOf(1), freq.getCumFreq(Character.valueOf('B')));
        assertEquals(Long.valueOf(2), freq.getCumFreq(Character.valueOf('A')));

        freq.addValue('C');
        assertEquals(Long.valueOf(2), freq.getCumFreq(Character.valueOf('C')));
        assertEquals(Long.valueOf(3), freq.getCumFreq(Character.valueOf('A')));

        freq.clear();
        assertEquals(Long.valueOf(0), freq.getCumFreq(null));
        assertEquals(Long.valueOf(0), freq.getCumFreq(Character.valueOf('A')));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct(null));
        assertEquals(Double.NaN, freq.getCumPct(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Double.valueOf(0.0), freq.getCumPct(null));
        assertEquals(Double.valueOf(100.0), freq.getCumPct(Character.valueOf('A')));

        freq.addValue('B');
        assertEquals(Double.valueOf(50.0), freq.getCumPct(Character.valueOf('B')));
        assertEquals(Double.valueOf(100.0), freq.getCumPct(Character.valueOf('A')));

        freq.addValue('A');
        assertEquals(Double.valueOf(70.0), freq.getCumPct(Character.valueOf('A')));

        freq.clear();
        assertEquals(Double.valueOf(0.0), freq.getCumPct(null));
        assertEquals(Double.valueOf(0.0), freq.getCumPct(Character.valueOf('A')));
    }
}