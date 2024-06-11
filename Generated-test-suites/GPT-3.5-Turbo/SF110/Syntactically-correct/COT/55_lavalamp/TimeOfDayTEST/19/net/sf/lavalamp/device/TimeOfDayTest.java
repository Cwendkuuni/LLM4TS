import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeOfDayTest {

    private TimeOfDay timeOfDay;

    @Before
    public void setUp() {
        timeOfDay = new TimeOfDay(10, 30);
    }

    @Test
    public void testGetHours() {
        Integer expected = 10;
        Integer actual = timeOfDay.getHours();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinutes() {
        Integer expected = 30;
        Integer actual = timeOfDay.getMinutes();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "10:30";
        String actual = timeOfDay.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = 1030;
        int actual = timeOfDay.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        TimeOfDay other = new TimeOfDay(10, 30);
        assertTrue(timeOfDay.equals(other));
    }

    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay other = new TimeOfDay(10, 30);
        int expected = 0;
        int actual = timeOfDay.compareTo(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo_EarlierTime() {
        TimeOfDay other = new TimeOfDay(9, 0);
        int expected = 1;
        int actual = timeOfDay.compareTo(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo_LaterTime() {
        TimeOfDay other = new TimeOfDay(11, 0);
        int expected = -1;
        int actual = timeOfDay.compareTo(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor_String() {
        TimeOfDay expected = new TimeOfDay(10, 30);
        TimeOfDay actual = new TimeOfDay("10:30");
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testConstructor_InvalidString() {
        TimeOfDay actual = new TimeOfDay("10:6"); // Invalid minutes format
    }

}