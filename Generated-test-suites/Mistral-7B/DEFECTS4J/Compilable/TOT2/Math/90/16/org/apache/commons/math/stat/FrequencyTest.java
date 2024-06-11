package org.apache.commons.math.stat;
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {
    @Test
    public void testAddValue() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        f.addValue(2);
        f.addValue(2);
        assertEquals(4, f.getSumFreq());
        assertEquals(2, f.getCount(1));
        assertEquals(2, f.getCount(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueThrowsException() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue('a');
    }

    @Test
    public void testGetPct() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(2);
        f.addValue(1);
        assertEquals(0.3333, f.getPct(1), 0.01);
        assertEquals(0.6667, f.getPct(2), 0.01);
    }

    @Test
    public void testGetCumPct() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        f.addValue(2);
        f.addValue(2);
        assertEquals(0.3333, f.getCumPct(1), 0.01);
        assertEquals(0.5, f.getCumPct(2), 0.01);
        assertEquals(1, f.getCumPct(3), 0.01);
    }
}