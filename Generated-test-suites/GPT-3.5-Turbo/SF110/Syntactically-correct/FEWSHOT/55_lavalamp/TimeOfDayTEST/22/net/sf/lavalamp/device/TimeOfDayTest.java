package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {
    private TimeOfDay time1;
    private TimeOfDay time2;
    
    @Before
    public void setUp() {
        time1 = new TimeOfDay(10, 30);
        time2 = new TimeOfDay(10, 45);
    }
    
    @Test
    public void testConstructorWithIntegers() {
        assertEquals(10, time1.getHours().intValue());
        assertEquals(30, time1.getMinutes().intValue());
    }
    
	@Test
    public void testConstructorWithString() {
        TimeOfDay time3 = new TimeOfDay("08:15");
        assertEquals(8, time3.getHours().intValue());
        assertEquals(15, time3.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        assertTrue(time1.compareTo(time2) < 0);
        assertEquals(0, time1.compareTo(time1));
        assertTrue(time2.compareTo(time1) > 0);
    }
    
	@Test
    public void testToString() {
        assertEquals("10:30", time1.toString());
        assertEquals("10:45", time2.toString());
    }
    
	@Test
    public void testHashCode() {
        assertEquals(1030, time1.hashCode());
        assertNotEquals(1045, time1.hashCode());
        assertEquals(1045, time2.hashCode());
    }
    
	@Test
    public void testEquals() {
        TimeOfDay time3 = new TimeOfDay(10, 30);
        TimeOfDay time4 = new TimeOfDay(8, 15);
        assertEquals(time1, time3);
        assertNotEquals(time1, time2);
        assertNotEquals(time1, time4);
    }
}