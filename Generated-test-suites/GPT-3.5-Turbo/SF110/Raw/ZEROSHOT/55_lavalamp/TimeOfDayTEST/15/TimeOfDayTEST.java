import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(10, 45);

        assertEquals(0, time1.compareTo(time2));
        assertTrue(time1.compareTo(time3) > 0);
        assertTrue(time3.compareTo(time1) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals(8, time.getHours().intValue());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals(15, time.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals("8:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        TimeOfDay time3 = new TimeOfDay(10, 45);

        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        TimeOfDay time3 = new TimeOfDay(10, 45);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}