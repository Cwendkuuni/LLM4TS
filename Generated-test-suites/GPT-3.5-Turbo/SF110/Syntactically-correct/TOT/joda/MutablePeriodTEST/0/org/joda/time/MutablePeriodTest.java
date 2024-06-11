import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3DT4H5M6S");
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(5, period.getMinutes());
        assertEquals(6, period.getSeconds());
    }

    @Test
    public void testParseWithFormat() {
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        MutablePeriod period = MutablePeriod.parse("P1Y2M3DT4H5M6S", formatter);
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(5, period.getMinutes());
        assertEquals(6, period.getSeconds());
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
    public void testConstructorWithDurationType() {
        MutablePeriod period = new MutablePeriod(1000L, PeriodType.yearWeekDayTime());
        assertEquals(1, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
        
        period = new MutablePeriod(1000L, PeriodType.dayTime());
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(1, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    // Add more test methods for other constructors and methods

}