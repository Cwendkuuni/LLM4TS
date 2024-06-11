import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(8, 30);
        assertEquals(Integer.valueOf(8), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
        assertEquals("8:30", time.toString());
    }
    
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
        assertEquals("9:45", time.toString());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(9, 00);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        
        TimeOfDay time3 = new TimeOfDay(8, 30);
        TimeOfDay time4 = new TimeOfDay(8, 30);
        assertEquals(0, time3.compareTo(time4));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 45);
        assertEquals(Integer.valueOf(10), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 45);
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(11, 45);
        TimeOfDay time2 = new TimeOfDay(11, 45);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
        
        TimeOfDay time3 = new TimeOfDay(9, 15);
        assertFalse(time1.equals(time3));
        
        assertFalse(time1.equals(null));
    }
}