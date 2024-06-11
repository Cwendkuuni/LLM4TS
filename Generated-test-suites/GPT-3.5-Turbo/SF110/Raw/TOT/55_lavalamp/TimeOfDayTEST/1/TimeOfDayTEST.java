
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TimeOfDayTest {
    
    private TimeOfDay tod1;
    private TimeOfDay tod2;
    private TimeOfDay tod3;
    
    @Before
    public void setUp() {
        tod1 = new TimeOfDay(8, 45);
        tod2 = new TimeOfDay("12:30");
        tod3 = new TimeOfDay(16, 0);
    }
    
    // Testing the first constructor
    @Test
    public void testConstructor1() {
        assertEquals(Integer.valueOf(8), tod1.getHours());
        assertEquals(Integer.valueOf(45), tod1.getMinutes());
    }
    
    // Testing the second constructor
    @Test
    public void testConstructor2() {
        assertEquals(Integer.valueOf(12), tod2.getHours());
        assertEquals(Integer.valueOf(30), tod2.getMinutes());
    }
    
    // Testing the compareTo method - comparing two instances of TimeOfDay
    @Test
    public void testCompareTo() {
        assertTrue(tod1.compareTo(tod2) < 0);
        assertTrue(tod1.compareTo(tod3) < 0);
        assertTrue(tod2.compareTo(tod1) > 0);
        assertTrue(tod2.compareTo(tod3) < 0);
        assertTrue(tod3.compareTo(tod1) > 0);
        assertTrue(tod3.compareTo(tod2) > 0);
        assertTrue(tod1.compareTo(tod1) == 0);
    }
    
    // Testing the getHours method
    @Test
    public void testGetHours() {
        assertEquals(Integer.valueOf(8), tod1.getHours());
        assertEquals(Integer.valueOf(12), tod2.getHours());
        assertEquals(Integer.valueOf(16), tod3.getHours());
    }
    
    // Testing the getMinutes method
    @Test
    public void testGetMinutes() {
        assertEquals(Integer.valueOf(45), tod1.getMinutes());
        assertEquals(Integer.valueOf(30), tod2.getMinutes());
        assertEquals(Integer.valueOf(0), tod3.getMinutes());
    }
    
    // Testing the toString method
    @Test
    public void testToString() {
        assertEquals("8:45", tod1.toString());
        assertEquals("12:30", tod2.toString());
        assertEquals("16:0", tod3.toString());
    }
    
    // Testing the hashCode method
    @Test
    public void testHashCode() {
        int expectedHash1 = new Integer((100 * 8) + 45).hashCode();
        int expectedHash2 = new Integer((100 * 12) + 30).hashCode();
        int expectedHash3 = new Integer((100 * 16) + 0).hashCode();
        
        assertEquals(expectedHash1, tod1.hashCode());
        assertEquals(expectedHash2, tod2.hashCode());
        assertEquals(expectedHash3, tod3.hashCode());
    }
    
    // Testing the equals method
    @Test
    public void testEquals() {
        TimeOfDay tod4 = new TimeOfDay(8, 45);
        TimeOfDay tod5 = new TimeOfDay("8:45");
        TimeOfDay tod6 = new TimeOfDay(10, 15);
        
        assertTrue(tod1.equals(tod4));
        assertTrue(tod1.equals(tod5));
        assertFalse(tod1.equals(tod2));
        assertFalse(tod1.equals(tod6));
    }
}

