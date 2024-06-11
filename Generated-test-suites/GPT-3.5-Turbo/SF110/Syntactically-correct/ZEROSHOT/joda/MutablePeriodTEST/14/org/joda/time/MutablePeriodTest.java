import static org.junit.Assert.*;
import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

public class MutablePeriodTest {

    private MutablePeriod period;

    @Before
    public void setUp() {
        period = new MutablePeriod();
    }

    @Test
    public void testParse() {
        String s = "P1Y2M3W4DT5H6M7.008S";
        MutablePeriod p = MutablePeriod.parse(s);
        assertEquals(1, p.getYears());
        assertEquals(2, p.getMonths());
        assertEquals(3, p.getWeeks());
        assertEquals(4, p.getDays());
        assertEquals(5, p.getHours());
        assertEquals(6, p.getMinutes());
        assertEquals(7, p.getSeconds());
        assertEquals(8, p.getMillis());
    }

    @Test
    public void testParseWithFormatter() {
        String s = "P1Y02M03W04DT05H06M07.008S";
        PeriodFormatter formatter = PeriodFormatterBuilder.forPattern("PYMWWDTDHMS.S").toFormatter();
        MutablePeriod p = MutablePeriod.parse(s, formatter);
        assertEquals(1, p.getYears());
        assertEquals(2, p.getMonths());
        assertEquals(3, p.getWeeks());
        assertEquals(4, p.getDays());
        assertEquals(5, p.getHours());
        assertEquals(6, p.getMinutes());
        assertEquals(7, p.getSeconds());
        assertEquals(8, p.getMillis());
    }

    @Test
    public void testConstructor1() {
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    // Add more unit tests for all methods in MutablePeriod class

}