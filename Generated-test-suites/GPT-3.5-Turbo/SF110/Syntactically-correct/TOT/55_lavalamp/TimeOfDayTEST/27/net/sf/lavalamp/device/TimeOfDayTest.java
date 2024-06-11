import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo_LessThan() {
        TimeOfDay time1 = new TimeOfDay(8, 20);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_GreaterThan() {
        TimeOfDay time1 = new TimeOfDay(15, 45);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testCompareTo_Equals() {
        TimeOfDay time1 = new TimeOfDay(6, 0);
        TimeOfDay time2 = new TimeOfDay(6, 0);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(Integer.valueOf(9), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(13, 55);
        assertEquals(Integer.valueOf(55), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(7, 35);
        assertEquals("7:35", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals_EqualObjects() {
        TimeOfDay time1 = new TimeOfDay(4, 25);
        TimeOfDay time2 = new TimeOfDay(4, 25);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        TimeOfDay time1 = new TimeOfDay(11, 10);
        Object time2 = new Object();
        assertFalse(time1.equals(time2));
    }
}