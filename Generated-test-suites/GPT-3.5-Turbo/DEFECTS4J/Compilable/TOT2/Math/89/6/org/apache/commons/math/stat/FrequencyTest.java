package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testAddValue() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValueChar() {
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testClear() {
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testGetCount() {
        frequency.addValue(3);
        assertEquals(1, frequency.getCount(3));
    }

    @Test
    public void testGetPct() {
        frequency.addValue(4);
        assertEquals(1.0, frequency.getPct(4), 0.0);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(3);
        assertEquals(1, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(6);
        assertEquals(1.0, frequency.getCumPct(6), 0.0);
    }
}