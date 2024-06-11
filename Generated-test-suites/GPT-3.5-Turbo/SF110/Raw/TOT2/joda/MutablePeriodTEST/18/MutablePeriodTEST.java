import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4D5H6M7S");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
    }

    @Test
    public void testMutablePeriodConstructor() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    // Add more tests for other public methods

}