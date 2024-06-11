package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    private TimeOfDay time1;
    private TimeOfDay time2;
    
    @Test
    public void testConstructor1() {
        time1 = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());
    }
    
    @Test
    public void testConstructor2() {
        time1 = new TimeOfDay("12:30");
        assertEquals(Integer.valueOf(12), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());
    }
    
    @Test
    public void testConstructor_invalidInput() {
        try{
            time1 = new TimeOfDay("12");
            fail("Expected IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e){
            assertEquals("Invalid time format", e.getMessage());
        }
    }
    
    @Test
    public void testCompareTo_equalHoursAndMinutes() {
        time1 = new TimeOfDay(12, 30);
        time2 = new TimeOfDay(12, 30);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareTo_equalHoursDifferentMinutes() {
        time1 = new TimeOfDay(12, 30);
        time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareTo_differentHours() {
        time1 = new TimeOfDay(12, 30);
        time2 = new TimeOfDay(18, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testGetHours() {
        time1 = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time1.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        time1 = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(30), time1.getMinutes());
    }
    
    @Test
    public void testToString() {
        time1 = new TimeOfDay(12, 30);
        assertEquals("12:30", time1.toString());
    }
    
    @Test
    public void testHashCode() {
        time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals_sameObject() {
        time1 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time1));
    }
    
    @Test
    public void testEquals_nullObject() {
        time1 = new TimeOfDay(12, 30);
        assertFalse(time1.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        time1 = new TimeOfDay(12, 30);
        assertFalse(time1.equals(new Object()));
    }
    
    @Test
    public void testEquals_sameHoursAndMinutes() {
        time1 = new TimeOfDay(12, 30);
        time2 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testEquals_differentHours() {
        time1 = new TimeOfDay(12, 30);
        time2 = new TimeOfDay(18, 30);
        assertFalse(time1.equals(time2));
    }
    
    @Test
    public void testEquals_differentMinutes() {
        time1 = new TimeOfDay(12, 30);
        time2 = new TimeOfDay(12, 45);
        assertFalse(time1.equals(time2));
    }
}