import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 20);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(8, 45);
        assertEquals(new Integer(8), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(8, 45);
        assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 25);
        assertEquals("5:25", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(3, 40);
        assertEquals(340, time.hashCode());
    }

    @Test
    public void testEquals_True() {
        TimeOfDay time1 = new TimeOfDay(6, 0);
        TimeOfDay time2 = new TimeOfDay(6, 0);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEquals_False() {
        TimeOfDay time1 = new TimeOfDay(9, 45);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        assertFalse(time1.equals(time2));
    }

}