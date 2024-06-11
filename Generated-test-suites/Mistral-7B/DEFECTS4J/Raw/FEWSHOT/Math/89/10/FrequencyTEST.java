package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.valuesIterator().next());
    }

    @Test
    public void testFrequencyInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(6, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));

        Iterator iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().longValue());
        assertEquals(1, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(3, iterator.next().longValue());
    }

    @Test
    public void testFrequencyLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);
        frequency.addValue(4L);

        assertEquals(5, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1L));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(2, frequency.getCount(3L));
        assertEquals(0, frequency.getCount(4L));

        Iterator iterator = frequency.valuesIterator();
        assertEquals(1L, iterator.next().longValue());
        assertEquals(2L, iterator.next().longValue());
        assertEquals(3L, iterator.next().longValue());
        assertEquals(3L, iterator.next().longValue());
    }

    @Test
    public void testFrequencyChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('c');
        frequency.addValue('c');
        frequency.addValue('d');

        assertEquals(5, frequency.getSumFreq());
        assertEquals(1, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('c'));
        assertEquals(1, frequency.getCount('b'));
        assertEquals(1, frequency.getCount('d'));

        Iterator iterator = frequency.valuesIterator();
        assertEquals(97, iterator.next().longValue());
        assertEquals(99, iterator.next().longValue());
        assertEquals(99, iterator.next().longValue());
        assertEquals(99, iterator.next().longValue());
        assertEquals(100, iterator.next().longValue());
    }

    @Test
    public void testFrequencyComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Long.compare((Long) o1, (Long) o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(3L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1L));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(1, frequency.getCount(3L));

        Iterator iterator = frequency.valuesIterator();
        assertEquals(1L, iterator.next().longValue());
        assertEquals(1L, iterator.next().longValue());
        assertEquals(2L, iterator.next().longValue());
        assertEquals(3L, iterator.next().longValue());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 3 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 3 \t 0.3333333333333333 \t 0.6666666666666666\n3 \t 1 \t 0.1666666666666667 \t 0.8333333333333333\n";
        assertEquals(expected, frequency.toString());
    }
}