package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }
    
    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(10));
        assertEquals(1, frequency.getCount(10));
    }
    
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(100L);
        assertEquals(1, frequency.getCount(100L));
    }
    
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
    }
    
    @Test
    public void testAddValueException() {
        Frequency frequency = new Frequency();
        try {
            frequency.addValue("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(2, frequency.getSumFreq());
    }
    
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(1, frequency.getCount(5));
        assertEquals(0, frequency.getCount(7));
    }
    
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(0.5, frequency.getPct(5), 0.001);
        assertEquals(Double.NaN, frequency.getPct(10), 0.001);
    }
    
    @Test
    public void testCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(1, frequency.getCumFreq(5));
        assertEquals(2, frequency.getCumFreq(10));
    }
    
    @Test
    public void testCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(0.5, frequency.getCumPct(5), 0.001);
        assertEquals(1.0, frequency.getCumPct(10), 0.001);
    }

}