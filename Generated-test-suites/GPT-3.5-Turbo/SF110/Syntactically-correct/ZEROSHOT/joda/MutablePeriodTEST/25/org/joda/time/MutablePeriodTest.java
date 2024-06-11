import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.base.*;
import java.io.*;
import org.joda.convert.*;
import org.joda.time.format.*;
import org.joda.time.field.*;

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
    public void testParseWithFormatter() {
        PeriodFormatter formatter = DateTimeFormat.forPattern("PyyMMdd");
        MutablePeriod period = MutablePeriod.parse("P180801", formatter);
        assertEquals(18, period.getYears());
        assertEquals(8, period.getMonths());
        assertEquals(1, period.getDays());
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
        PeriodType periodType = PeriodType.yearMonthDayTime();
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
    public void testConstructorWithFields() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    // Add test methods for all other methods in the MutablePeriod class
    
}