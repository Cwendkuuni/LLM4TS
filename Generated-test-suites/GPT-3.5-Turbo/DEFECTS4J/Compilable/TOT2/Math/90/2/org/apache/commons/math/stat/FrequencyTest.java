package org.apache.commons.math.stat;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {
    
    @Test
    public void testAddValue() {
        Frequency f = new Frequency();
        f.addValue(5);
        assertEquals(1, f.getCount(5));
    }
    
    @Test
    public void testClear() {
        Frequency f = new Frequency();
        f.addValue(5);
        f.clear();
        assertEquals(0, f.getCount(5));
    }
    
    @Test
    public void testGetSumFreq() {
        Frequency f = new Frequency();
        f.addValue(5);
        f.addValue(5);
        assertEquals(2, f.getSumFreq());
    }
    
    @Test
    public void testGetCount() {
        Frequency f = new Frequency();
        f.addValue(5);
        assertEquals(1, f.getCount(5));
    }
}