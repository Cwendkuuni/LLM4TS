import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {
    
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }
    
    @Test
    public void testConstructorWithPeriodType() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, PeriodType.monthDayTime());
        assertEquals(1, period.getMonths());
        assertEquals(2, period.getDays());
        assertEquals(3, period.getHours());
        assertEquals(4, period.getMinutes());
    }
    
    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        period.clear();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
    }
    
    // Add more test cases for the remaining methods
    
}