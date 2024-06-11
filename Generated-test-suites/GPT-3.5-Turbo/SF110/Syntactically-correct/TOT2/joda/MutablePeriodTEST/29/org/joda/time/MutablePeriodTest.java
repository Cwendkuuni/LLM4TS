package org.joda.time;
import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {
    
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }
    
    @Test
    public void testSetPeriod() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }
    
    @Test
    public void testAddDuration() {
        MutablePeriod period = new MutablePeriod();
        period.add(new Period(1, 0, 0, 0, 0, 0, 0, 0));
        assertEquals(1, period.getYears());
    }
    
    // Add more test cases for each method
    
}