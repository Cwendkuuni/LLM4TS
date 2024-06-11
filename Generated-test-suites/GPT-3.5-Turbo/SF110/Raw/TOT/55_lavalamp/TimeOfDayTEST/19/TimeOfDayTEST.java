import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testCompareTo_EarlierTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_LaterTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
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
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertFalse(time1.equals(time2));
    }
}