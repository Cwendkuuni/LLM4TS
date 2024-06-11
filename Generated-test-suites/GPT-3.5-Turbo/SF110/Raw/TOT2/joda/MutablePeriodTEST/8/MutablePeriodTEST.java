import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParseString() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M");
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
    }

    @Test
    public void testParseStringPeriodFormatter() {
        MutablePeriod period = MutablePeriod.parse("P2M3D", ISOPeriodFormat.standard());
        assertNotNull(period);
        assertEquals(0, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }
    
    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        period.clear();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    // Add more individual test cases for each method
    
    // Edge cases and error scenarios test cases

}