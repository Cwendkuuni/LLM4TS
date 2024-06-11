package org.apache.commons.math.stat;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @After
    public void tearDown() {
        frequency = null;
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
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testClear() {
        frequency.addValue(5);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
    }

    @Test
    public void testGetSumFreq() {
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(6);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(2, frequency.getCount(5));
    }

    @Test
    public void testGetPct() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0.5, frequency.getPct(10), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(1, frequency.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(15);
        frequency.addValue(30);
        assertEquals(0.5, frequency.getCumPct(15), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_NonComparable() {
        frequency.addValue(new Object());
    }

}