import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
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
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertEquals("12:0", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(new Integer(915).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(8, 30);
        assertEquals(time1, time2);
    }
}