
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Integer expected = 10;
        Integer actual = time.getHours();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Integer expected = 30;
        Integer actual = time.getMinutes();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        String expected = "10:30";
        String actual = time.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        int expected = time1.hashCode();
        int actual = time2.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        int expected = -1;
        int actual = time1.compareTo(time2);
        assertEquals(expected, actual);
    }
}
