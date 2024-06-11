import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithInt() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours().intValue());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(1230).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time2));
    }
}