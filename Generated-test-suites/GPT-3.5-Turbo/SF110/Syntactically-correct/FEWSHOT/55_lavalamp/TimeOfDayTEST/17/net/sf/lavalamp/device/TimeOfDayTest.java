import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructor_ints() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructor_string() {
        TimeOfDay time = new TimeOfDay("09:30");
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) < 0);
        assertEquals(0, time1.compareTo(time1));
        assertTrue(time2.compareTo(time1) > 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 45);
        assertEquals(Integer.valueOf(12), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 45);
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals("9:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        TimeOfDay time3 = new TimeOfDay(10, 15);
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}