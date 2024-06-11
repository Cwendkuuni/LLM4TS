import org.joda.time.*;
import org.junit.Test;

public class MutablePeriodTest {
  
    @Test
    public void testParse() {
        String s = "P1Y2M3D";
        MutablePeriod period = MutablePeriod.parse(s);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }
    
    @Test
    public void testParseWithFormatter() {
        String s = "01:02:03";
        PeriodFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        MutablePeriod period = MutablePeriod.parse(s, formatter);
        assertEquals(1, period.getHours());
        assertEquals(2, period.getMinutes());
        assertEquals(3, period.getSeconds());
    }
    
    @Test
    public void testDefaultConstructor() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
    }
    
    // add more test cases for other constructors and methods
    
    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.clear();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    // add more test cases for other methods

}