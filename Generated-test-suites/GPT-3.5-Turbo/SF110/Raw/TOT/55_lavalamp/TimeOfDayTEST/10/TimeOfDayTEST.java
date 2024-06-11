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
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(new Integer(9), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testCompareToEarlierTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareToLaterTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testCompareToSameTime() {
        TimeOfDay time1 = new TimeOfDay(5, 0);
        TimeOfDay time2 = new TimeOfDay(5, 0);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(15, 45);
        assertEquals("15:45", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(7, 30);
        TimeOfDay time2 = new TimeOfDay(7, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertEquals(time, time);
    }

    @Test
    public void testEqualsDifferentObject() {
        TimeOfDay time1 = new TimeOfDay(3, 30);
        TimeOfDay time2 = new TimeOfDay(3, 30);
        assertEquals(time1, time2);
    }

    @Test
    public void testEqualsDifferentType() {
        TimeOfDay time = new TimeOfDay(5, 45);
        assertFalse(time.equals("5:45"));
    }

    @Test
    public void testEqualsNull() {
        TimeOfDay time = new TimeOfDay(10, 0);
        assertFalse(time.equals(null));
    }

}