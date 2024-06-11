package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(10);
        assertEquals(1, frequency.getCount(10));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(15L);
        assertEquals(1, frequency.getCount(15L));
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(7);
        assertEquals(1, frequency.getCount(7));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(8);
        assertEquals(1.0, frequency.getPct(8), 0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(4);
        frequency.addValue(5);
        assertEquals(2, frequency.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getCumPct(2), 0);
    }

}