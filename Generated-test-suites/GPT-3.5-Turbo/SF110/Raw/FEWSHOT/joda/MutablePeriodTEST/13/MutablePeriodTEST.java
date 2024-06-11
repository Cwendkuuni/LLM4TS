
package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.joda.time.format.*;
import org.junit.Test;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4DT5H6M7S");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
    }
    
    @Test
    public void testParseWithFormat() {
        MutablePeriod period = MutablePeriod.parse("1Y 2M 3W 4D 5H 6M 7S", PeriodFormat.getDefault());
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
    }
    
    @Test
    public void testConstructorNoArgs() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    @Test
    public void testConstructorWithPeriodType() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod period = new MutablePeriod(periodType);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    @Test
    public void testConstructorWithValues() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
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
    public void testConstructorWithValuesAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, periodType);
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
    public void testConstructorWithDuration() {
        Duration duration = new Duration(1234567890L);
        MutablePeriod period = new MutablePeriod(duration);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(34200, period.getHours());
        assertEquals(56, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(890, period.getMillis());
    }
    
    @Test
    public void testConstructorWithStartEnd() {
        Instant start = new Instant(1234567890L);
        Instant end = new Instant(1234567891L);
        MutablePeriod period = new MutablePeriod(start, end);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(1, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    @Test
    public void testConstructorWithStartEndAndPeriodType() {
        Instant start = new Instant(1234567890L);
        Instant end = new Instant(1234567891L);
        PeriodType periodType = PeriodType.standard();
        MutablePeriod period = new MutablePeriod(start, end, periodType);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(1, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    // Other test methods go here
    
}
