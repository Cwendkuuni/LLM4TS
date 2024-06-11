import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(9, 45);
        assertEquals(9, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        TimeOfDay time3 = new TimeOfDay(9, 30);
        TimeOfDay time4 = new TimeOfDay(10, 30);

        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time1.compareTo(time3) > 0);
        assertTrue(time1.compareTo(time4) == 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals("8:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        TimeOfDay time3 = new TimeOfDay(10, 45);

        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        TimeOfDay time3 = new TimeOfDay(10, 45);
        TimeOfDay time4 = null;
        Object nonTimeOfDayObject = new Object();

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(time4));
        assertFalse(time1.equals(nonTimeOfDayObject));
    }
}