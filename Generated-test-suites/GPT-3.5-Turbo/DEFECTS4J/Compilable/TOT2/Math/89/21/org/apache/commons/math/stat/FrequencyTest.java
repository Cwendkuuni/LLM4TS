package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValueInteger() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(Long.valueOf(10));
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue((Comparable) "test");
        assertEquals(1, freq.getCount("test"));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(1, freq.getCount(5));
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.5, freq.getPct(5), 0.001);
    }

    @Test
    public void testCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        freq.addValue(5);
        assertEquals(2, freq.getCumFreq(5));
        assertEquals(0, freq.getCumFreq(3));
    }

    @Test
    public void testCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.5, freq.getCumPct(5), 0.001);
    }

    @Test
    public void testComparatorFrequency() {
        Comparator<Double> reverseComparator = Comparator.reverseOrder();
        Frequency freq = new Frequency(reverseComparator);
        freq.addValue(4.0);
        freq.addValue(6.0);
        assertEquals(2, freq.getSumFreq());
    }

}