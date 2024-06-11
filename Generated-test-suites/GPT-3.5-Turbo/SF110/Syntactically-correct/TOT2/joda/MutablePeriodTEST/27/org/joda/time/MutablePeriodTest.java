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
    public void testConstructor() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.yearMonthDayTime());
        
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    // Add more test cases for other methods

}