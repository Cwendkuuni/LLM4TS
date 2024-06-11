package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    private Frequency frequency;

    @Test
    public void testAddValue() {
        frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
        frequency.addValue(5);
        assertEquals(2, frequency.getCount(5));
    }

    @Test
    public void testAddValueObject() {
        frequency = new Frequency();
        frequency.addValue(10L);
        assertEquals(1, frequency.getCount(10L));
        frequency.addValue(10);
        assertEquals(2, frequency.getCount(10));
    }

    @Test
    public void testAddValueChar() {
        frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
        frequency.addValue('A');
        assertEquals(2, frequency.getCount('A'));
    }

    @Test
    public void testAddValueException() {
        frequency = new Frequency();
        try {
            frequency.addValue("invalid");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testClear() {
        frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testGetPct() {
        frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(1), 0.0);
    }

    @Test
    public void testGetCumPct() {
        frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getCumPct(1), 0.0);
    }
}