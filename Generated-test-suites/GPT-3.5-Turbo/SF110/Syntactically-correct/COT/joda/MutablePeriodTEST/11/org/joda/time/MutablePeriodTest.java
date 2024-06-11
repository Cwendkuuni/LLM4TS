import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y");
        assertEquals(1, period.getYears());
    }

    @Test
    public void testParseWithFormatter() {
        PeriodFormatter formatter = PeriodFormat.getDefault();
        MutablePeriod period = MutablePeriod.parse("P1Y", formatter);
        assertEquals(1, period.getYears());
    }

    @Test
    public void testMutablePeriodDefaultConstructor() {
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
    public void testMutablePeriodPeriodTypeConstructor() {
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

    // Additional Test cases for edge cases and exception handling scenarios can be added here

}