package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount((short) 1));
        assertEquals(0, frequency.getCount(Character.valueOf('a')));
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount((short) 1));
        assertEquals(0, frequency.getCount(Character.valueOf('a')));

        Frequency frequency1 = new Frequency();
        frequency1.addValue(1);
        frequency1.addValue(2);
        frequency1.addValue(1);
        frequency1.addValue(2);

        Frequency frequency2 = new Frequency(comparator);
        frequency2.addValue(2);
        frequency2.addValue(1);
        frequency2.addValue(1);
        frequency2.addValue(2);

        assertEquals(frequency1, frequency2);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "0 \t 0 \t 0.0% \t 0.0%\n" +
                "1 \t 0 \t 0.0% \t 0.0%\n" +
                "2 \t 0 \t 0.0% \t 0.0%\n" +
                "-- \t 0 \t 0.0% \t 0.0%\n", frequency.toString());

        Frequency frequency1 = new Frequency();
        frequency1.addValue(1);
        frequency1.addValue(2);
        frequency1.addValue(1);
        frequency1.addValue(2);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 25.0% \t 25.0%\n" +
                "2 \t 2 \t 25.0% \t 50.0%\n" +
                "-- \t 2 \t 25.0% \t 100.0%\n", frequency1.toString());

        frequency.clear();
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(3);
        frequency.addValue(1);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 10.5263157894737% \t 10.5263157894737%\n" +
                "3 \t 4 \t 21.0526315789474% \t 31.5789473684211%\n" +
                "-- \t 2 \t 10.5263157894737% \t 42.1157894736842%\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableType() {
        Frequency frequency = new Frequency();
        frequency.addValue(2.5);
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        frequency.clear();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        frequency.addValue(3);
        assertEquals(3, frequency.getCount(3));
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        frequency.addValue(new Character('a'));
        assertEquals(1, frequency.getCount(new Character('a')));
        frequency.addValue(new Character('b'));
        assertEquals(0, frequency.getCount(new Character('b')));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertNull(iterator.next());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());
        frequency.addValue(1);
        assertEquals(4, frequency.getSumFreq());
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(2));
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
        frequency.clear();
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        frequency.addValue(new Character('a'));
        assertEquals(1, frequency.getCount(new Character('a')));
        frequency.addValue(new Character('b'));
        assertEquals(0, frequency.getCount(new Character('b')));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(1));
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.25, frequency.getPct(1), 0.001);
        assertEquals(0.25, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getPct(3), 0.001);
        frequency.clear();
        assertEquals(Double.NaN, frequency.getPct(1));
        assertEquals(Double.NaN, frequency.getPct(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        assertEquals(0.25, frequency.getPct(1), 0.001);
        assertEquals(0.25, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getPct(3), 0.001);
        frequency.addValue(new Character('a'));
        assertEquals(0.1, frequency.getPct(new Character('a')), 0.001);
        assertEquals(0, frequency.getPct(new Character('b')), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumFreq(2));
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
        frequency.addValue(2);
        assertEquals(3, frequency.getCumFreq(2));
        frequency.addValue(1);
        assertEquals(4, frequency.getCumFreq(1));
        frequency.clear();
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumFreq(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(3));
        frequency.addValue(new Character('a'));
        assertEquals(1, frequency.getCumFreq(new Character('a')));
        assertEquals(1, frequency.getCumFreq(new Character('b')));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getCumPct(1));
        assertEquals(Double.NaN, frequency.getCumPct(2));
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        frequency.clear();
        assertEquals(Double.NaN, frequency.getCumPct(1));
        assertEquals(Double.NaN, frequency.getCumPct(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        frequency.addValue(new Character('a'));
        assertEquals(0.2, frequency.getCumPct(new Character('a')), 0.001);
        assertEquals(0.2, frequency.getCumPct(new Character('b')), 0.001);
    }
}