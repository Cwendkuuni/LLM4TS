package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringTime() {
        TimeOfDay time = new TimeOfDay("10:30");
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        TimeOfDay time3 = new TimeOfDay(10, 45);
        
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time1));
        assertEquals(-1, time1.compareTo(time3));
        assertEquals(1, time3.compareTo(time1));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        
        assertEquals(time1.hashCode(), time2.hashCode());
        assertEquals(time2.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        TimeOfDay time4 = new TimeOfDay(10, 45);
        
        assertEquals(true, time1.equals(time1));
        assertEquals(true, time1.equals(time2));
        assertEquals(true, time2.equals(time1));
        assertEquals(false, time1.equals(null));
        assertEquals(false, time1.equals(new Object()));
        assertEquals(false, time1.equals(time3));
        assertEquals(false, time3.equals(time1));
        assertEquals(false, time1.equals(time4));
    }
}