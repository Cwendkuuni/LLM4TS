import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertNotNull(time1);
        assertEquals(Integer.valueOf(10), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time1 = new TimeOfDay("10:30");
        assertNotNull(time1);
        assertEquals(Integer.valueOf(10), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());
    }
    
    @Test
    public void testCompareToWhenEqual() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareToWhenLessThan() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareToWhenGreaterThan() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time1.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(30), time1.getMinutes());
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
    public void testEqualsWhenEqual() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEqualsWhenNotEqual() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        assertFalse(time1.equals(time2));
    }
}