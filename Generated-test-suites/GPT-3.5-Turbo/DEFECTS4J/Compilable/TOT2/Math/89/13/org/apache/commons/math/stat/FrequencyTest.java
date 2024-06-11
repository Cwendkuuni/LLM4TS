package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2L);
        freq.addValue(3);

        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2L));
        assertEquals(1, freq.getCount(3));
    }

    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(2, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1000000000L);
        freq.addValue(2000000000L);
        freq.addValue(1000000000L);

        assertEquals(2, freq.getCount(1000000000L));
        assertEquals(1, freq.getCount(2000000000L));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('a');

        assertEquals(2, freq.getCount('a'));
        assertEquals(1, freq.getCount('b'));
    }

    @Test
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        try {
            freq.addValue("string");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();

        assertEquals(0, freq.getCount(1));
        assertEquals(0, freq.getCount(2));
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
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.6666666666666666, freq.getPct(1), 0.0001);
        assertEquals(0.3333333333333333, freq.getPct(2), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(2, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);

        assertEquals(0.6666666666666666, freq.getCumPct(1), 0.0001);
        assertEquals(1, freq.getCumPct(2), 0.0001);
    }

    @Test
    public void testComparator() {
        Comparator<Integer> descComparator = (a, b) -> b.compareTo(a);
        Frequency freq = new Frequency(descComparator);
        freq.addValue(3);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(3, freq.valuesIterator().next());
    }
}