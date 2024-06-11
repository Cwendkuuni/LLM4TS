package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithInts() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    // Additional test cases for edge cases and exceptions can be added here

}