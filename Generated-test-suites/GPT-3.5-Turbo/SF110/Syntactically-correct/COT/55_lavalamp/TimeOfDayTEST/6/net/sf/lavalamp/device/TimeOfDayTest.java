import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 0);
        assertTrue(time1.compareTo(time2) < 0);

        TimeOfDay time3 = new TimeOfDay(12, 0);
        assertTrue(time2.compareTo(time3) < 0);

        TimeOfDay time4 = new TimeOfDay(10, 30);
        assertEquals(0, time1.compareTo(time4));

        TimeOfDay time5 = new TimeOfDay(11, 30);
        assertTrue(time5.compareTo(time1) > 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(1030).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));

        TimeOfDay time3 = new TimeOfDay(11, 0);
        assertFalse(time1.equals(time3));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testConstructorWithStringInvalidFormat() {
        TimeOfDay time = new TimeOfDay("12");
    }

    @Test(expected = NumberFormatException.class)
    public void testConstructorWithStringInvalidTime() {
        TimeOfDay time = new TimeOfDay("aa:bb");
    }

    @Test
    public void testConstructorWithStringLeadingZero() {
        TimeOfDay time = new TimeOfDay("08:30");
        assertEquals(Integer.valueOf(8), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithStringMaxValues() {
        TimeOfDay time = new TimeOfDay("23:59");
        assertEquals(Integer.valueOf(23), time.getHours());
        assertEquals(Integer.valueOf(59), time.getMinutes());
    }

    @Test
    public void testEqualsWithNull() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertFalse(time.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertFalse(time.equals("10:30"));
    }
}

