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
    public void testAddValueWithInteger() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValueWithIntegerMultipleTimes() {
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(2, frequency.getCount(5));
    }

    @Test
    public void testAddValueWithLong() {
        frequency.addValue(100L);
        assertEquals(1, frequency.getCount(100L));
    }

    @Test
    public void testAddValueWithChar() {
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
    public void testGetPct() {
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(2, frequency.getCount(5));
        assertEquals(1.0, frequency.getPct(5), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        frequency.addValue(new Object()); // Adding a non-comparable object should throw an exception
    }

    @Test
    public void testGetCountNonComparable() {
        assertEquals(0, frequency.getCount(new Object())); // Attempting to get count for a non-comparable object should return 0
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(2, frequency.getSumFreq());
        assertEquals(0.5, frequency.getCumPct(5), 0.001);
    }

}