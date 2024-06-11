
package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorIntHours() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(0), time.getMinutes());
    }
    
    @Test
    public void testConstructorIntMinutes() {
        TimeOfDay time = new TimeOfDay(0, 30);
        assertEquals(Integer.valueOf(0), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorStringMidnight() {
        TimeOfDay time = new TimeOfDay("00:00");
        assertEquals(Integer.valueOf(0), time.getHours());
        assertEquals(Integer.valueOf(0), time.getMinutes());
    }
    
    @Test
    public void testConstructorStringAfternoon() {
        TimeOfDay time = new TimeOfDay("15:45");
        assertEquals(Integer.valueOf(15), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareToSameTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareToDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testCompareToDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 45);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
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
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEqualsSameObject() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertTrue(time.equals(time));
    }
    
    @Test
    public void testEqualsDifferentObject() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testEqualsNullObject() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertFalse(time.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertFalse(time.equals("10:30"));
    }
    
    @Test
    public void testEqualsDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertFalse(time1.equals(time2));
    }
    
    @Test
    public void testEqualsDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertFalse(time1.equals(time2));
    }
}

