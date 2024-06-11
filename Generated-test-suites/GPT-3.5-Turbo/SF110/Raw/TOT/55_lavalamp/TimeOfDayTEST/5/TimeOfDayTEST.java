import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithInt() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("10:30");
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }

    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testCompareTo_SameHourDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_DifferentHourSameMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_DifferentHourDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testEquals_SameTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEquals_DifferentTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 45);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Integer expectedHashCode = new Integer(100 * 10 + 30);
        assertEquals(expectedHashCode.hashCode(), time.hashCode());
    }
}