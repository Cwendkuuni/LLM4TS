import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntParams() {
        TimeOfDay time = new TimeOfDay(12, 34);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(34), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringParam() {
        TimeOfDay time = new TimeOfDay("12:34");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(34), time.getMinutes());
    }
    
    @Test
    public void testCompareToSameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 34);
        TimeOfDay time2 = new TimeOfDay(12, 34);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareToEarlierTime() {
        TimeOfDay time1 = new TimeOfDay(10, 45);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareToLaterTime() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 34);
        assertEquals("12:34", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 34);
        TimeOfDay time2 = new TimeOfDay(12, 34);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEqualsSameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 34);
        TimeOfDay time2 = new TimeOfDay(12, 34);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        TimeOfDay time1 = new TimeOfDay(12, 34);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertFalse(time1.equals(time2));
    }
}