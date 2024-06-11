package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultComparator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(0);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(1.0, frequency.getPct(1.0), 0.001);
        assertEquals(0.25, frequency.getCumPct(0.0), 0.001);
        assertEquals(0.5, frequency.getCumPct(1.0), 0.001);

        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(0, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(1, iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void testFrequencyCustomComparator() {
        Comparator<Number> customComparator = new Comparator<Number>() {
            @Override
            public int compare(Number n1, Number n2) {
                return n1.longValue() < n2.longValue() ? -1 : n1.longValue() > n2.longValue() ? 1 : 0;
            }
        };

        Frequency frequency = new Frequency(customComparator);
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(0));
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));

        assertEquals(4, frequency.getSumFreq());
        assertEquals(1.0, frequency.getPct(Long.valueOf(1)), 0.001);
        assertEquals(0.25, frequency.getCumPct(Long.valueOf(0)), 0.001);
        assertEquals(0.5, frequency.getCumPct(Long.valueOf(1)), 0.001);

        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(Long.valueOf(0), iterator.next());
        assertEquals(Long.valueOf(1), iterator.next());
        assertEquals(Long.valueOf(1), iterator.next());
        assertEquals(Long.valueOf(1), iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void testFrequencyAddValue() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());

        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());

        frequency.addValue(1);
        assertEquals(4, frequency.getSumFreq());

        frequency.addValue(1);
        assertEquals(5, frequency.getSumFreq());

        frequency.addValue(new Object()); // should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueDifferentTypes() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test
    public void testFrequencyValuesIterator() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().hashCode());
        assertEquals(1, iterator.next().hashCode());
        assertEquals(1, iterator.next().hashCode());
        assertNull(iterator.next());
    }

    @Test
    public void testFrequencyGetCount() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(3, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testFrequencyGetCountChar() {
        Frequency frequency = new Frequency();

        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
    }

    @Test
    public void testFrequencyGetCountNull() {
        Frequency frequency = new Frequency();

        assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testFrequencyGetPct() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(0.6, frequency.getPct(1), 0.001);
        assertEquals(0.0, frequency.getPct(2), 0.001);
    }

    @Test
    public void testFrequencyGetPctChar() {
        Frequency frequency = new Frequency();

        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(0.6, frequency.getPct('a'), 0.001);
        assertEquals(0.0, frequency.getPct('b'), 0.001);
    }

    @Test
    public void testFrequencyGetPctNull() {
        Frequency frequency = new Frequency();

        assertEquals(0.0, frequency.getPct(null), 0.001);
    }

    @Test
    public void testFrequencyGetCumFreq() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(3));
        assertEquals(0, frequency.getCumFreq(0));
    }

    @Test
    public void testFrequencyGetCumFreqChar() {
        Frequency frequency = new Frequency();

        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(2, frequency.getCumFreq('a'));
        assertEquals(3, frequency.getCumFreq('b'));
        assertEquals(0, frequency.getCumFreq('c'));
    }

    @Test
    public void testFrequencyGetCumFreqNull() {
        Frequency frequency = new Frequency();

        assertEquals(0, frequency.getCumFreq(null));
    }

    @Test
    public void testFrequencyGetCumPct() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(0.3, frequency.getCumPct(1), 0.001);
        assertEquals(0.75, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        assertEquals(0.0, frequency.getCumPct(0), 0.001);
    }

    @Test
    public void testFrequencyGetCumPctChar() {
        Frequency frequency = new Frequency();

        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(0.3, frequency.getCumPct('a'), 0.001);
        assertEquals(0.6, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('b'), 0.001);
        assertEquals(0.0, frequency.getCumPct('c'), 0.001);
    }

    @Test
    public void testFrequencyGetCumPctNull() {
        Frequency frequency = new Frequency();

        assertEquals(0.0, frequency.getCumPct(null), 0.001);
    }

    @Test
    public void testFrequencyToString() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 3 \t 0.6 \t 0.3 \n1 \t 3 \t 0.6 \t 0.75 \n1 \t 3 \t 0.6 \t 1.0 \n";
        assertEquals(expected, frequency.toString());
    }
}