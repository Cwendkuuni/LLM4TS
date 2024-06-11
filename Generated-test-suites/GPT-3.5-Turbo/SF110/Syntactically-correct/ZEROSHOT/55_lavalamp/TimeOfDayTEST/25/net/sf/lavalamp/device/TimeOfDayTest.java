import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithInts() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(new Integer(9), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(14, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time3));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(17, 45);
        assertEquals(new Integer(17), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(8, 35);
        assertEquals(new Integer(35), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(22, 15);
        assertEquals("22:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(8, 0);
        assertEquals(new Integer(800).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        TimeOfDay time3 = new TimeOfDay(15, 45);
        
        assertEquals(true, time1.equals(time2));
        assertEquals(false, time1.equals(time3));
    }
}