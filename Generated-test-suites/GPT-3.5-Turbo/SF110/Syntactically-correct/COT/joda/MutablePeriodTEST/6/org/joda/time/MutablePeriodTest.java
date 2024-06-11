import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }
    
    @Test
    public void testParseWithPeriodFormatter() {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
            .appendYears()
            .appendSuffix(" year", " years")
            .appendSeparator(", ")
            .appendMonths()
            .appendSuffix(" month", " months")
            .appendSeparator(", ")
            .appendDays()
            .appendSuffix(" day", " days")
            .toFormatter();
        MutablePeriod period = MutablePeriod.parse("1 year, 2 months, 3 days", formatter);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }
    
    @Test
    public void testMutablePeriodDefaultConstructor() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
    }
    
    @Test
    public void testMutablePeriodWithPeriodType() {
        PeriodType periodType = PeriodType.yearMonthDayTime();
        MutablePeriod period = new MutablePeriod(periodType);
        assertEquals(periodType, period.getPeriodType());
    }
    
    @Test
    public void testMutablePeriodWithInts() {
        int years = 1;
        int months = 2;
        int weeks = 3;
        int days = 4;
        MutablePeriod period = new MutablePeriod(years, months, weeks, days);
        assertEquals(years, period.getYears());
        assertEquals(months, period.getMonths());
        assertEquals(weeks, period.getWeeks());
        assertEquals(days, period.getDays());
    }
    
    @Test
    public void testMutablePeriodWithIntsAndYears() {
        int years = 1;
        int months = 2;
        int weeks = 3;
        int days = 4;
        int hours = 5;
        int minutes = 6;
        int seconds = 7;
        int millis = 8;
        PeriodType periodType = PeriodType.yearMonthDayTime();
        MutablePeriod period = new MutablePeriod(years, months, weeks, days, hours, minutes, seconds, millis, periodType);
        assertEquals(years, period.getYears());
        assertEquals(months, period.getMonths());
        assertEquals(weeks, period.getWeeks());
        assertEquals(days, period.getDays());
        assertEquals(hours, period.getHours());
        assertEquals(minutes, period.getMinutes());
        assertEquals(seconds, period.getSeconds());
        assertEquals(millis, period.getMillis());
        assertEquals(periodType, period.getPeriodType());
    }
    
    // Add more test cases for other methods
    
}