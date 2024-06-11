package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.statistics.NumStatistics;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyWithDefaultComparator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(3, freq.getCount(1L));
        assertEquals(2, freq.getCount(2L));
        assertEquals(5, freq.getSumFreq());

        Iterator it = freq.valuesIterator();
        assertEquals(1L, it.next());
        assertEquals(2L, it.next());

        assertEquals(0.4, freq.getPct(1L), 0.01);
        assertEquals(0.4, freq.getPct(1), 0.01); // Integer value is automatically converted to Long
        assertEquals(0.2, freq.getPct(2L), 0.01);
        assertEquals(0.4, freq.getPct(2), 0.01); // Integer value is automatically converted to Long

        assertEquals(0.2, freq.getCumPct(1L), 0.01);
        assertEquals(0.2, freq.getCumPct(1), 0.01); // Integer value is automatically converted to Long
        assertEquals(0.4, freq.getCumPct(2L), 0.01);
        assertEquals(0.6, freq.getCumPct(3L), 0.01);
        assertEquals(1.0, freq.getCumPct(5L), 0.01);

        assertEquals(1.0, freq.getCumFreq(5L), 5);
        assertEquals(3.0, freq.getCumFreq(3L), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue('a');

        // The following call should throw an IllegalArgumentException as 'a' is not comparable to integers
        freq.addValue(1);
    }

    @Test
    public void testFrequencyWithCustomComparator() {
        Frequency freq = new Frequency(new Comparator<Comparable<?>>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(Comparable<? extends Comparable<?>> o1, Comparable<? extends Comparable<?>> o2) {
                return ((Comparable<Comparable<?>>) o1).compareTo((Comparable<Comparable<?>>) o2);
            }
        });
        freq.addValue("a");
        freq.addValue("z");
        freq.addValue("z");
        freq.addValue("a");

        assertEquals(2, freq.getCount("a"));
        assertEquals(2, freq.getCount("z"));
        assertEquals(4, freq.getSumFreq());

        Iterator it = freq.valuesIterator();
        assertEquals("a", it.next());
        assertEquals("z", it.next());

        assertEquals(0.5, freq.getPct("a"), 0.01);
        assertEquals(0.5, freq.getPct("z"), 0.01);

        assertEquals(0.25, freq.getCumPct("a"), 0.01);
        assertEquals(0.5, freq.getCumPct("z"), 0.01);
        assertEquals(1.0, freq.getCumPct("z"), 0.01);

        assertEquals(2.0, freq.getCumFreq("a"));
        assertEquals(4.0, freq.getCumFreq("z"));
    }

    @Test
    public void testStatisticalMethods() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(1);

        NumStatistics stats = new NumStatistics();
        stats.push(1.0); stats.push(1.0);
        stats.push(2.0); stats.push(1.0);
        stats.push(1.0); stats.push(1.0);

        assertEquals(stats.getMean(), freq.getMean(), 0.01);
        assertEquals(stats.getStandardDeviation(), freq.getStdDev(), 0.01);
        assertEquals(stats.getMin(), freq.getMin(), 0.01);
        assertEquals(stats.getMax(), freq.getMax(), 0.01);
    }
}