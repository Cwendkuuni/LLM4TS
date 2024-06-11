import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructor1() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructor2() {
        TimeOfDay time = new TimeOfDay("07:45");
        assertEquals(new Integer(7), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testConstructorInvalidFormat() {
        try {
            TimeOfDay time = new TimeOfDay("abc");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid time format", e.getMessage());
        }
    }

    @Test
    public void testCompareToEarlier() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareToLater() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(8, 0);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testCompareToSameTime() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(new Integer(9), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(new Integer(15), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals("9:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertTrue(time.equals(time));
    }

    @Test
    public void testEqualsDifferentObject() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEqualsNull() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertFalse(time.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertFalse(time.equals("9:15"));
    }

    @Test
    public void testEqualsDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testEqualsDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testEqualsDifferentHoursAndMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertFalse(time1.equals(time2));
    }

}