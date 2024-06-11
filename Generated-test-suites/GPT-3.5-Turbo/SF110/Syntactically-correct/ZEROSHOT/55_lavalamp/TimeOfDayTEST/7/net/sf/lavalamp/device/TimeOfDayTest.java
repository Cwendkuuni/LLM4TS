import org.junit.*;
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
        TimeOfDay time = new TimeOfDay("08:45");
        assertEquals(8, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals("9:15", time.toString());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        
        TimeOfDay time3 = new TimeOfDay(10, 30);
        TimeOfDay time4 = new TimeOfDay(10, 30);
        assertEquals(0, time3.compareTo(time4));
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 45);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(7, 30);
        TimeOfDay time2 = new TimeOfDay(7, 30);
        assertEquals(true, time1.equals(time2));
        
        TimeOfDay time3 = new TimeOfDay(7, 30);
        TimeOfDay time4 = new TimeOfDay(7, 45);
        assertEquals(false, time3.equals(time4));
        
        assertEquals(false, time1.equals(null));
        assertEquals(false, time1.equals("7:30"));
    }
}