package org.apache.commons.math.stat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testAddValue_Integer() {
        frequency.addValue(5);
        frequency.addValue(10);
        frequency.addValue(5);

        assertEquals(2, frequency.getCount(5));
        assertEquals(1, frequency.getCount(10));
    }

    @Test
    public void testAddValue_Long() {
        frequency.addValue(100L);
        frequency.addValue(200L);
        frequency.addValue(100L);

        assertEquals(2, frequency.getCount(100L));
        assertEquals(1, frequency.getCount(200L));
    }

    @Test
    public void testAddValue_Char() {
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
    }

    @Test
    public void testClear() {
        frequency.addValue(5);
        frequency.addValue(10);
        frequency.clear();

        assertEquals(0, frequency.getCount(5));
        assertEquals(0, frequency.getCount(10));
    }

    @Test
    public void testGetSumFreq() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);

        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);

        assertEquals(2, frequency.getCount(5));
        assertEquals(1, frequency.getCount(10));
    }

    @Test
    public void testGetPct() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);

        assertEquals(0.6666666666666666, frequency.getPct(5), 0.0001);
        assertEquals(0.3333333333333333, frequency.getPct(10), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);

        assertEquals(2, frequency.getCumFreq(5));
        assertEquals(3, frequency.getCumFreq(10));
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);

        assertEquals(0.6666666666666666, frequency.getCumPct(5), 0.0001);
        assertEquals(1.0, frequency.getCumPct(10), 0.0001);
    }
}