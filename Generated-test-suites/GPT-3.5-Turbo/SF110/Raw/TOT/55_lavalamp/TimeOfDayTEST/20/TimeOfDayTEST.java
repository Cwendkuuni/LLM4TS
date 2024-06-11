
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareTo_SameHourDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareTo_DifferentHourSameMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testCompareTo_DifferentHourDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals_SameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testEquals_DifferentTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        assertFalse(time1.equals(time2));
    }
}

