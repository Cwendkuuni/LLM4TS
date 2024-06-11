import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructor_withIntegers() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        assertEquals(Integer.valueOf(5), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(Integer.valueOf(12), time2.getHours());
        assertEquals(Integer.valueOf(0), time2.getMinutes());
    }

    @Test
    public void testConstructor_withString() {
        TimeOfDay time1 = new TimeOfDay("09:15");
        assertEquals(Integer.valueOf(9), time1.getHours());
        assertEquals(Integer.valueOf(15), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay("18:45");
        assertEquals(Integer.valueOf(18), time2.getHours());
        assertEquals(Integer.valueOf(45), time2.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 20);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(10, 20);

        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time1.compareTo(time3) == 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        assertEquals(Integer.valueOf(14), time1.getHours());

        TimeOfDay time2 = new TimeOfDay(8, 0);
        assertEquals(Integer.valueOf(8), time2.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time1 = new TimeOfDay(9, 45);
        assertEquals(Integer.valueOf(45), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay(17, 0);
        assertEquals(Integer.valueOf(0), time2.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(6, 0);
        assertEquals("6:0", time1.toString());

        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals("12:30", time2.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(5, 45);
        TimeOfDay time2 = new TimeOfDay(5, 45);

        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(7, 0);
        TimeOfDay time2 = new TimeOfDay(7, 0);
        TimeOfDay time3 = new TimeOfDay(7, 30);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}