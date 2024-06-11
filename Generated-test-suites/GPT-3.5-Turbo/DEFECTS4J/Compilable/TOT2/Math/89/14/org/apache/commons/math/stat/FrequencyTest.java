package org.apache.commons.math.stat;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

    private Frequency frequency;

    @Test
    public void testAddValue_Integer() {
        frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
        assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testAddValue_Long() {
        frequency = new Frequency();
        frequency.addValue(Long.valueOf(10));
        assertEquals(1, frequency.getCount(10));
        assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testAddValue_Char() {
        frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testAddValue_Exception() {
        frequency = new Frequency();
        try {
            frequency.addValue("invalid");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddValue_Comparator() {
        Comparator comparator = Comparator.reverseOrder();
        frequency = new Frequency(comparator);
        frequency.addValue(3);
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(3));
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetPct() {
        frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(1), 0.01);
        assertEquals(0.33, frequency.getPct(2), 0.01);
    }

    @Test
    public void testGetCumPct() {
        frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getCumPct(1), 0.01);
        assertEquals(1.0, frequency.getCumPct(2), 0.01);
    }
}