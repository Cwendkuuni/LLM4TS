import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithValues() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("08:15");
        assertEquals(Integer.valueOf(8), time.getHours());
        assertEquals(Integer.valueOf(15), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(8, 45);

        assertTrue(time1.compareTo(time1) == 0);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time1.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time1) > 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertEquals("12:0", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(8, 30);

        assertTrue(time1.hashCode() == time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(8, 30);
        TimeOfDay time3 = new TimeOfDay(10, 30);

        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertTrue(time2.equals(time1));
        assertFalse(time1.equals(time3));
        assertFalse(time3.equals(time1));
    }

    // Add more test cases for the remaining methods if needed

}

