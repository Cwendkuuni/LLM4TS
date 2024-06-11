package org.apache.commons.math.stat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testAddValue_Integer() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValue_Long() {
        frequency.addValue(10L);
        assertEquals(1, frequency.getCount(10L));
    }

    @Test
    public void testAddValue_Char() {
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
    }

    @Test
    public void testClear() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
    }

    @Test
    public void testGetCount() {
        frequency.addValue(2);
        frequency.addValue((long)3);
        assertEquals(2, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));
    }

    @Test
    public void testGetPct() {
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
    }
}