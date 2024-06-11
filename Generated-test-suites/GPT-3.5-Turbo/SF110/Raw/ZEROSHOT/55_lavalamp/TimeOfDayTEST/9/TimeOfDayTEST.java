import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    private TimeOfDay time1;
    private TimeOfDay time2;
    private TimeOfDay time3;
    private TimeOfDay time4;
    
    @Before
    public void setUp() {
        time1 = new TimeOfDay(9, 30);
        time2 = new TimeOfDay(14, 45);
        time3 = new TimeOfDay(10, 0);
        time4 = new TimeOfDay("13:15");
    }
    
    @Test
    public void testCompareTo_SameHoursAndMinutes_Returns0() {
        assertEquals(0, time1.compareTo(time1));
    }
    
    @Test
    public void testCompareTo_SameHoursDifferentMinutes_ReturnsCorrectComparison() {
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
    }
    
    @Test
    public void testCompareTo_DifferentHours_ReturnsCorrectComparison() {
        assertTrue(time1.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time1) > 0);
    }
    
    @Test
    public void testGetHours() {
        assertEquals(9, time1.getHours().intValue());
        assertEquals(14, time2.getHours().intValue());
        assertEquals(10, time3.getHours().intValue());
        assertEquals(13, time4.getHours().intValue());
    }
    
    @Test
    public void testGetMinutes() {
        assertEquals(30, time1.getMinutes().intValue());
        assertEquals(45, time2.getMinutes().intValue());
        assertEquals(0, time3.getMinutes().intValue());
        assertEquals(15, time4.getMinutes().intValue());
    }
    
    @Test
    public void testToString() {
        assertEquals("9:30", time1.toString());
        assertEquals("14:45", time2.toString());
        assertEquals("10:0", time3.toString());
        assertEquals("13:15", time4.toString());
    }
    
    @Test
    public void testHashCode() {
        Integer expected1 = (100 * 9) + 30;
        Integer expected2 = (100 * 14) + 45;
        Integer expected3 = (100 * 10) + 0;
        Integer expected4 = (100 * 13) + 15;
        
        assertEquals(expected1.hashCode(), time1.hashCode());
        assertEquals(expected2.hashCode(), time2.hashCode());
        assertEquals(expected3.hashCode(), time3.hashCode());
        assertEquals(expected4.hashCode(), time4.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time5 = new TimeOfDay(9, 30);
        TimeOfDay time6 = new TimeOfDay(19, 45);
        
        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time5));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals(new Object()));
        assertFalse(time1.equals(time2));
        assertFalse(time1.equals(time6));
    }
}