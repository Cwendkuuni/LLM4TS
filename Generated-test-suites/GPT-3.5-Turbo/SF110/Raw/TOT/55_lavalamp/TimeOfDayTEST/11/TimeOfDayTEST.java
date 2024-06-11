import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        
        assertEquals(9, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithInvalidString() {
        try {
            TimeOfDay time = new TimeOfDay("24:00");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid time format", e.getMessage());
        }
    }
    
    @Test
    public void testCompareToEqualTime() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareToEarlierHour() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(9, 0);
        
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareToLaterHour() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testCompareToSameHourEarlierMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareToSameHourLaterMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(7, 45);
        
        assertEquals("7:45", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(15, 30);
        TimeOfDay time2 = new TimeOfDay(15, 30);
        TimeOfDay time3 = new TimeOfDay(18, 0);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}