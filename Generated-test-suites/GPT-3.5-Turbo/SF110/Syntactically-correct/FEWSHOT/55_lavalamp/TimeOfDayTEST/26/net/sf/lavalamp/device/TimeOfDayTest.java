package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertNotNull(time);
        assertEquals(new Integer(5), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertNotNull(time);
        assertEquals(new Integer(9), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 0);
        
        assertTrue(time1.compareTo(time2) < 0);
        assertEquals(0, time2.compareTo(time2));
        assertTrue(time3.compareTo(time2) > 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertEquals(new Integer(10), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertEquals(new Integer(15), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(14, 20);
        assertEquals("14:20", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(10, 0);
        TimeOfDay time3 = new TimeOfDay(10, 30);
        
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(10, 0);
        TimeOfDay time3 = new TimeOfDay(10, 30);
        
        assertTrue(time1.equals(time2));
        assertTrue(!time1.equals(time3));
    }
}