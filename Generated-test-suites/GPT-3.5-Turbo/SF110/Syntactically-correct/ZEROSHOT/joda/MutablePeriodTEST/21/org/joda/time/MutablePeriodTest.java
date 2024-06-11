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
    public void testParseWithFormatter() {
        PeriodFormatter formatter = PeriodFormatterBuilder()
                                    .appendYears()
                                    .appendSuffix(" years")
                                    .appendMonths()
                                    .appendSuffix(" months")
                                    .appendDays()
                                    .appendSuffix(" days")
                                    .toFormatter();
        MutablePeriod period = MutablePeriod.parse("1 years 2 months 3 days", formatter);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }

    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        period.clear();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    // Add more test methods for all other methods in the MutablePeriod class

}