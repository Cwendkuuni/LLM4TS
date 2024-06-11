package org.joda.time;
import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {
    
    @Test
    public void testParse() {
        String input = "P1Y2M3DT4H5M6S";
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 0, 0, PeriodType.standard());
        assertEquals(expected, MutablePeriod.parse(input));
    }
    
    @Test
    public void testMutablePeriodConstructor() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
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
    public void testSetValue() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setValue(DurationFieldType.YEARS, 10);
        assertEquals(10, period.getYears());
    }
    
    @Test
    public void testSetWeeksEdgeCase() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setWeeks(52); // Max weeks value
        assertEquals(52, period.getWeeks());
    }
    
    @Test
    public void testAddMillisException() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        try {
            period.addMillis(-10); // Adding negative millis should throw an exception
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
}