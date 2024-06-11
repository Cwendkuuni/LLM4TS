package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        assertEquals(1, freq.getCount(3));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.clear();
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(1, freq.getCount(3));
        assertEquals(0, freq.getCount(7));
    }

    @Test
    public void testPct() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(2.0 / 3.0, freq.getPct(3), 0.0001);
    }

    @Test
    public void testCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(2, freq.getCumFreq(3));
    }

    @Test
    public void testCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(2.0 / 3.0, freq.getCumPct(3), 0.0001);
    }

    @Test
    public void testComparator() {
        Comparator<Integer> comparator = Integer::compareTo;
        Frequency freq = new Frequency(comparator);
        freq.addValue(3);
        freq.addValue(5);
        assertTrue(freq.valuesIterator().hasNext());
    }
}