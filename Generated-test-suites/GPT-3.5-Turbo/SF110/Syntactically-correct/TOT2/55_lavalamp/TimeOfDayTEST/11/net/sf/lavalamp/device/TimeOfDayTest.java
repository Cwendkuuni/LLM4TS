package net.sf.lavalamp.device;
import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals("9:15", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(8, 0);
        assertEquals(new Integer(800).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        assertTrue(time1.equals(time2));
    }
    
    @Test(expected = NumberFormatException.class)
    public void testConstructorWithStringParameterInvalidFormat() {
        TimeOfDay time = new TimeOfDay("abc:def");
    }

}