import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    private TimeOfDay time1;
    private TimeOfDay time2;

    @Before
    public void setUp() {
        time1 = new TimeOfDay(10, 30);
        time2 = new TimeOfDay("09:45");
    }

    @Test
    public void testCompareTo() {
        assertEquals(1, time1.compareTo(time2));
        assertEquals(-1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(new TimeOfDay(10, 30)));
    }

    @Test
    public void testGetHours() {
        assertEquals(10, time1.getHours().intValue());
        assertEquals(9, time2.getHours().intValue());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(30, time1.getMinutes().intValue());
        assertEquals(45, time2.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        assertEquals("10:30", time1.toString());
        assertEquals("09:45", time2.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(1030, time1.hashCode());
        assertEquals(945, time2.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(time1.equals(new TimeOfDay(10, 30)));
        assertFalse(time1.equals(time2));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("10:30"));
    }
}