
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(10, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("10:30");
        assertEquals(10, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 0);
        TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
        assertTrue(timeOfDay1.compareTo(timeOfDay3) == 0);
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals("10:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertFalse(timeOfDay1.equals(null));
        assertFalse(timeOfDay1.equals("10:30"));
    }

}
