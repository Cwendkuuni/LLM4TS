package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(10, 30);
        
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        
        assertEquals(new Integer(9), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        TimeOfDay time3 = new TimeOfDay(12, 30);
        
        assertTrue(time1.compareTo(time2) > 0);
        assertTrue(time2.compareTo(time1) < 0);
        assertTrue(time1.compareTo(time3) == 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(8, 15);
        
        assertEquals(new Integer(8), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(8, 15);
        
        assertEquals(new Integer(15), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 20);
        
        assertEquals("9:20", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(11, 45);
        
        assertEquals(new Integer(1145).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 45);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("10:30"));
    }
}