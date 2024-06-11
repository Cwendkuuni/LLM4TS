package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructor() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        assertEquals(new Integer(1), time1.getHours());
        assertEquals(new Integer(30), time1.getMinutes());
        
        TimeOfDay time2 = new TimeOfDay("01:30");
        assertEquals(new Integer(1), time2.getHours());
        assertEquals(new Integer(30), time2.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(2, 30);
        TimeOfDay time3 = new TimeOfDay(1, 45);
        
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time1));
        assertEquals(-1, time1.compareTo(time3));
        assertEquals(1, time3.compareTo(time1));
        assertEquals(0, time3.compareTo(time3));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        assertEquals(new Integer(1), time1.getHours());
        
        TimeOfDay time2 = new TimeOfDay("01:30");
        assertEquals(new Integer(1), time2.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        assertEquals(new Integer(30), time1.getMinutes());
        
        TimeOfDay time2 = new TimeOfDay("01:30");
        assertEquals(new Integer(30), time2.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        assertEquals("1:30", time1.toString());
        
        TimeOfDay time2 = new TimeOfDay("01:30");
        assertEquals("1:30", time2.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay("01:30");
        TimeOfDay time3 = new TimeOfDay(2, 30);
        
        assertEquals(time1.hashCode(), time2.hashCode());
        assertFalse(time1.hashCode() == time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay("01:30");
        TimeOfDay time3 = new TimeOfDay(2, 30);
        
        assertEquals(time1, time2);
        assertFalse(time1.equals(time3));
    }
}