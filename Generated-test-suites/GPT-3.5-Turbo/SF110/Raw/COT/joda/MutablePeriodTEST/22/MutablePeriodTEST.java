import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.*;
import org.joda.time.format.*;
import org.joda.time.field.*;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4DT5H6M7.8S");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(800, period.getMillis());
    }
    
    @Test
    public void testParseWithFormatter() {
        PeriodFormatter formatter = DateTimeFormat.forPattern("P y M w d H m s S");
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4DT5H6M7.8S", formatter);
        assertEquals(1, period.get(DurationFieldType.years()));
        assertEquals(2, period.get(DurationFieldType.months()));
        assertEquals(3, period.get(DurationFieldType.weeks()));
        assertEquals(4, period.get(DurationFieldType.days()));
        assertEquals(5, period.get(DurationFieldType.hours()));
        assertEquals(6, period.get(DurationFieldType.minutes()));
        assertEquals(7, period.get(DurationFieldType.seconds()));
        assertEquals(800, period.get(DurationFieldType.millis()));
    }
    
    @Test
    public void testDefaultConstructor() {
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
    public void testPeriodTypeConstructor() {
        MutablePeriod period = new MutablePeriod(PeriodType.standard());
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
    public void testIntConstructor() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    // ... add individual test cases for other constructors and methods ...
    
}