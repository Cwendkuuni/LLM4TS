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
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(11, 30);

        assertEquals(0, time1.compareTo(time2));
        assertTrue(time1.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time1) > 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals("8:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(6, 45);
        assertEquals(Integer.valueOf(645).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        TimeOfDay time3 = new TimeOfDay(10, 30);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }

    // Edge cases and exception handling tests

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatExceptionInConstructorWithString() {
        new TimeOfDay("123:45");
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionInEquals() {
        TimeOfDay time = new TimeOfDay(5, 0);
        time.equals(null);
    }

}


