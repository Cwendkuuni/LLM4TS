import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
        int hours = 10;
        int minutes = 30;
        TimeOfDay time = new TimeOfDay(hours, minutes);
        
        assertEquals(hours, time.getHours().intValue());
        assertEquals(minutes, time.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        String timeString = "10:30";
        TimeOfDay time = new TimeOfDay(timeString);
        
        assertEquals(10, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        
        assertTrue(time1.compareTo(time2) == 0);
        assertTrue(time1.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time1) > 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Integer hours = time.getHours();
        
        assertEquals(10, hours.intValue());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Integer minutes = time.getMinutes();
        
        assertEquals(30, minutes.intValue());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        String timeString = time.toString();
        
        assertEquals("10:30", timeString);
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 30);
        int hashCode = time.hashCode();
        
        assertEquals(1030, hashCode);
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("10:30"));
    }
}

