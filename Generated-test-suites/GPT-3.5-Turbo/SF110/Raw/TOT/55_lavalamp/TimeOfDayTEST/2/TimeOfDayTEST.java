
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
        TimeOfDay time = new TimeOfDay("15:45");
        assertEquals(Integer.valueOf(15), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay("10:00");
        TimeOfDay time2 = new TimeOfDay("12:30");

        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time1.compareTo(time1));
        assertEquals(0, time2.compareTo(time2));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals(Integer.valueOf(8), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(6, 45);
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 0);
        TimeOfDay time2 = new TimeOfDay(9, 0);

        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(14, 20);
        TimeOfDay time2 = new TimeOfDay(14, 20);
        TimeOfDay time3 = new TimeOfDay(12, 30);

        assertTrue(time1.equals(time2));
        assertTrue(time2.equals(time1));
        assertFalse(time1.equals(time3));
    }
}

