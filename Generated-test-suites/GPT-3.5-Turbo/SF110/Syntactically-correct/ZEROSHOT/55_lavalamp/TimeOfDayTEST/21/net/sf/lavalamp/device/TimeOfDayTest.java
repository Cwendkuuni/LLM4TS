import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(12, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time1.compareTo(time1));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals(10, time1.getHours().intValue());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals(30, time1.getMinutes().intValue());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals("10:30", time1.toString());
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
        TimeOfDay time3 = new TimeOfDay(12, 45);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }

}