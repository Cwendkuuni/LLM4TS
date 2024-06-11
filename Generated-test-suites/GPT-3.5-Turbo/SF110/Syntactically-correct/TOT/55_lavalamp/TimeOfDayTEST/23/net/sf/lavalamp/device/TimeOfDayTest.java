import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    /**
     * Test case for the constructor with int parameters
     * Typical use case
     */
    @Test
    public void testTimeOfDayConstructor1() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    /**
     * Test case for the constructor with int parameters
     * Edge case: hours = 0, minutes = 0
     */
    @Test
    public void testTimeOfDayConstructor2() {
        TimeOfDay time = new TimeOfDay(0, 0);
        assertEquals(Integer.valueOf(0), time.getHours());
        assertEquals(Integer.valueOf(0), time.getMinutes());
    }

    /**
     * Test case for the constructor with int parameters
     * Error scenario: negative hours
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructor3() {
        new TimeOfDay(-3, 45);
    }

    /**
     * Test case for the constructor with String parameter
     * Typical use case
     */
    @Test
    public void testTimeOfDayConstructor4() {
        TimeOfDay time = new TimeOfDay("13:15");
        assertEquals(Integer.valueOf(13), time.getHours());
        assertEquals(Integer.valueOf(15), time.getMinutes());
    }

    /**
     * Test case for the constructor with String parameter
     * Edge case: time = "00:00"
     */
    @Test
    public void testTimeOfDayConstructor5() {
        TimeOfDay time = new TimeOfDay("00:00");
        assertEquals(Integer.valueOf(0), time.getHours());
        assertEquals(Integer.valueOf(0), time.getMinutes());
    }

    /**
     * Test case for the constructor with String parameter
     * Error scenario: invalid time format "12:5"
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructor6() {
        new TimeOfDay("12:5");
    }

    /**
     * Test case for compareTo method
     * Typical use case: this time is before the given time
     */
    @Test
    public void testCompareTo1() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(9, 0);
        assertTrue(time1.compareTo(time2) < 0);
    }

    /**
     * Test case for compareTo method
     * Typical use case: this time is after the given time
     */
    @Test
    public void testCompareTo2() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        assertTrue(time1.compareTo(time2) > 0);
    }

    /**
     * Test case for compareTo method
     * Typical use case: this time is equal to the given time
     */
    @Test
    public void testCompareTo3() {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        assertEquals(0, time1.compareTo(time2));
    }

    /**
     * Test case for getHours method
     * Typical use case
     */
    @Test
    public void testGetHours1() {
        TimeOfDay time = new TimeOfDay(12, 10);
        assertEquals(Integer.valueOf(12), time.getHours());
    }

    /**
     * Test case for getHours method
     * Edge case: hours = 0
     */
    @Test
    public void testGetHours2() {
        TimeOfDay time = new TimeOfDay(0, 45);
        assertEquals(Integer.valueOf(0), time.getHours());
    }

    /**
     * Test case for getMinutes method
     * Typical use case
     */
    @Test
    public void testGetMinutes1() {
        TimeOfDay time = new TimeOfDay(18, 55);
        assertEquals(Integer.valueOf(55), time.getMinutes());
    }

    /**
     * Test case for getMinutes method
     * Edge case: minutes = 0
     */
    @Test
    public void testGetMinutes2() {
        TimeOfDay time = new TimeOfDay(9, 0);
        assertEquals(Integer.valueOf(0), time.getMinutes());
    }

    /**
     * Test case for toString method
     * Typical use case
     */
    @Test
    public void testToString1() {
        TimeOfDay time = new TimeOfDay(7, 20);
        assertEquals("7:20", time.toString());
    }

    /**
     * Test case for toString method
     * Edge case: hours = 0, minutes = 0
     */
    @Test
    public void testToString2() {
        TimeOfDay time = new TimeOfDay(0, 0);
        assertEquals("0:0", time.toString());
    }

    /**
     * Test case for hashCode method
     * Typical use case
     */
    @Test
    public void testHashCode1() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(14, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    /**
     * Test case for hashCode method
     * Typical use case: different times, different hash codes
     */
    @Test
    public void testHashCode2() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(15, 0);
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }

    /**
     * Test case for equals method
     * Typical use case: this time equals the given time
     */
    @Test
    public void testEquals1() {
        TimeOfDay time1 = new TimeOfDay(10, 45);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertTrue(time1.equals(time2));
    }

    /**
     * Test case for equals method
     * Typical use case: this time does not equal the given time
     */
    @Test
    public void testEquals2() {
        TimeOfDay time1 = new TimeOfDay(7, 30);
        TimeOfDay time2 = new TimeOfDay(9, 0);
        assertFalse(time1.equals(time2));
    }

    /**
     * Test case for equals method
     * Typical use case: equal objects are equal
     */
    @Test
    public void testEquals3() {
        TimeOfDay time = new TimeOfDay(5, 50);
        assertTrue(time.equals(time));
    }

    /**
     * Test case for equals method
     * Typical use case: comparing with null should return false
     */
    @Test
    public void testEquals4() {
        TimeOfDay time = new TimeOfDay(13, 15);
        assertFalse(time.equals(null));
    }

    /**
     * Test case for equals method
     * Typical use case: comparing with object of different class should return false
     */
    @Test
    public void testEquals5() {
        TimeOfDay time = new TimeOfDay(18, 30);
        assertFalse(time.equals(new Object()));
    }
}