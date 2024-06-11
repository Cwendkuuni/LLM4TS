
import org.junit.*;
import static org.junit.Assert.*;

public class MutablePeriodTest {
    
    @Test
    public void testParse() {
        String s = "P1Y2M3W4DT5H6M7S";
        MutablePeriod actual = MutablePeriod.parse(s);
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 7);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithFormatter() {
        String s = "P1Y2M3W4DT5H6M7S";
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        MutablePeriod actual = MutablePeriod.parse(s, formatter);
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 7);
        assertEquals(expected, actual);
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
    
    // Add more test cases for other methods
    
}

