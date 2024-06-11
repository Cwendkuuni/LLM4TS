package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyWithDefaultComparator() {
        Frequency frequency = new Frequency();

        List<Comparable<?>> values = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5, 5, 5);
        for (Comparable<?> value : values) {
            frequency.addValue(value);
        }
        assertEquals(5, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(5, frequency.getCount(3));
        assertEquals(4, frequency.getCount(4));
        assertEquals(1, frequency.getCount(5));
        assertEquals(15, frequency.getSumFreq());

        assertEquals(0.3333333333333333, frequency.getPct(1), 0.00001);
        assertEquals(0.2, frequency.getPct(2), 0.00001);
        assertEquals(0.3333333333333333, frequency.getPct(3), 0.00001);
        assertEquals(0.26666666666666664, frequency.getPct(4), 0.00001);
        assertEquals(0.033333333333333327, frequency.getPct(5), 0.00001);

        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.00001);
        assertEquals(0.3333333333333333, frequency.getCumPct(2), 0.00001);
        assertEquals(0.6666666666666667, frequency.getCumPct(3), 0.00001);
        assertEquals(0.9333333333333333, frequency.getCumPct(4), 0.00001);
        assertEquals(1.0, frequency.getCumPct(5), 0.00001);

        assertEquals("Value  \t Freq. \t Pct. \t Cum Pct.\n1        \t 5        \t 0.3333333333333333\t 0.3333333333333333\n2        \t 3        \t 0.2       \t 0.6666666666666667\n3        \t 5        \t 0.3333333333333333\t 1.0\n4        \t 4        \t 0.26666666666666664\t 1.0\n5        \t 1        \t 0.033333333333333327\t 1.0\n", frequency.toString());
    }

    @Test
    public void testFrequencyWithCustomComparator() {
        Frequency frequency = new Frequency(new NaturalComparator());

        List<Comparable<?>> values = Arrays.asList(5, 2, 2, 1, 3, 3, 4, 5, 5, 5, 5);
        for (Comparable<?> value : values) {
            frequency.addValue(value);
        }
        assertEquals(5, frequency.getCount(5));
        assertEquals(3, frequency.getCount(3));
        assertEquals(3, frequency.getCount(2));
        assertEquals(1, frequency.getCount(1));
        assertEquals(4, frequency.getCount(4));
        assertEquals(15, frequency.getSumFreq());

        assertEquals(0.3333333333333333, frequency.getPct(5), 0.00001);
        assertEquals(0.2, frequency.getPct(2), 0.00001);
        assertEquals(0.2, frequency.getPct(3), 0.00001);
        assertEquals(0.26666666666666664, frequency.getPct(1), 0.00001);
        assertEquals(0.3333333333333333, frequency.getPct(4), 0.00001);

        assertEquals(0.3333333333333333, frequency.getCumPct(5), 0.00001);
        assertEquals(0.6666666666666667, frequency.getCumPct(3), 0.00001);
        assertEquals(1.0, frequency.getCumPct(2), 0.00001);
        assertEquals(1.0, frequency.getCumPct(1), 0.00001);
        assertEquals(1.0, frequency.getCumPct(4), 0.00001);

        assertEquals("Value  \t Freq. \t Pct. \t Cum Pct.\n5        \t 5        \t 0.3333333333333333\t 0.3333333333333333\n4        \t 1        \t 0.06666666666666667\t 0.3333333333333333\n3        \t 3        \t 0.2        \t 0.6666666666666667\n2        \t 3        \t 0.2\t \t 1.0\n1        \t 1        \t 0.033333333333333327\t 1.0\n", frequency.toString());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();

        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(5);

        assertEquals(5, frequency.getCount(5));
        assertEquals(5, frequency.getCount(Integer.valueOf(5)));
        assertEquals(5, frequency.getCount((Comparable<Number>)Long.valueOf(5)));
        assertEquals(5, frequency.getCount(Long.valueOf(5)));
        assertEquals(5, frequency.getCount(Character.valueOf((char)53))); // ASCII code for '5'
        assertEquals(5, frequency.getCount('5'));

        assertEquals(25, frequency.getSumFreq());

        assertEquals(0.2, frequency.getPct(5), 0.00001);
        assertEquals(0.2, frequency.getPct(Integer.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getPct((Comparable<Number>)Long.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getPct(Long.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getPct(Character.valueOf((char)53)), 0.00001);
        assertEquals(0.2, frequency.getPct('5'), 0.00001);

        assertEquals(0.2, frequency.getCumPct(5), 0.00001);
        assertEquals(0.2, frequency.getCumPct(Integer.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getCumPct((Comparable<Number>)Long.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getCumPct(Long.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getCumPct(Character.valueOf((char)53)), 0.00001);
        assertEquals(0.2, frequency.getCumPct('5'), 0.00001);

        assertEquals("Value  \t Freq. \t Pct. \t Cum Pct.\n5        \t 5        \t 0.2       \t 0.2\n", frequency.toString());
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();

        frequency.addValue(5L);
        frequency.addValue(5L);
        frequency.addValue(5L);
        frequency.addValue(5L);
        frequency.addValue(5L);

        assertEquals(5, frequency.getCount(5));
        assertEquals(5, frequency.getCount(Long.valueOf(5)));
        assertEquals(5, frequency.getCount(Long.TYPE));

        assertEquals(25, frequency.getSumFreq());

        assertEquals(0.2, frequency.getPct(5), 0.00001);
        assertEquals(0.2, frequency.getPct(Long.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getPct(Long.TYPE), 0.00001);

        assertEquals(0.2, frequency.getCumPct(5), 0.00001);
        assertEquals(0.2, frequency.getCumPct(Long.valueOf(5)), 0.00001);
        assertEquals(0.2, frequency.getCumPct(Long.TYPE), 0.00001);

        assertEquals("Value  \t Freq. \t Pct. \t Cum Pct.\n5        \t 5        \t 0.2       \t 0.2\n", frequency.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueCharIllegalArgumentException() {
        Frequency frequency = new Frequency();

        frequency.addValue('a');
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();

        frequency.addValue('5');
        frequency.addValue('5');
        frequency.addValue('5');
        frequency.addValue('5');
        frequency.addValue('5');

        assertEquals(5, frequency.getCount('5'));
        assertEquals(5, frequency.getCount(Character.valueOf('5')));

        assertEquals(25, frequency.getSumFreq());

        assertEquals(0.2, frequency.getPct('5'), 0.00001);
        assertEquals(0.2, frequency.getPct(Character.valueOf('5')), 0.00001);

        assertEquals(0.2, frequency.getCumPct('5'), 0.00001);
        assertEquals(0.2, frequency.getCumPct(Character.valueOf('5')), 0.00001);

        assertEquals("Value  \t Freq. \t Pct. \t Cum Pct.\n5        \t 5        \t 0.2       \t 0.2\n", frequency.toString());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();

        List<Comparable<?>> values = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5, 5, 5);
        for (Comparable<?> value : values) {
            frequency.addValue(value);
        }

        frequency.clear();

        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
        assertEquals(0, frequency.getCount(3));
        assertEquals(0, frequency.getCount(4));
        assertEquals(0, frequency.getCount(5));

        assertEquals("Value  \t Freq. \t Pct. \t Cum Pct.\n", frequency.toString());
    }
}