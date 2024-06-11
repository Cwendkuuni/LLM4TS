package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        TimeOfDay time3 = new TimeOfDay(10, 30);
        
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time1.compareTo(time3) == 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 20);
        assertEquals(Integer.valueOf(9), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 20);
        assertEquals(Integer.valueOf(20), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertEquals("10:15", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(10, 45);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}