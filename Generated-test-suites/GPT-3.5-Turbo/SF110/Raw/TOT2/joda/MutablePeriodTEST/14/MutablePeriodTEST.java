import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        // Test parsing a valid input string
        String input = "P1Y2M3D";
        MutablePeriod period = MutablePeriod.parse(input);
        assertEquals("Years not parsed correctly", 1, period.getYears());
        assertEquals("Months not parsed correctly", 2, period.getMonths());
        assertEquals("Days not parsed correctly", 3, period.getDays());
    }

    @Test
    public void testMutablePeriodConstructor() {
        // Test creating a MutablePeriod with specific values
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertEquals("Years not set correctly", 1, period.getYears());
        assertEquals("Months not set correctly", 2, period.getMonths());
        assertEquals("Days not set correctly", 3, period.getDays());
        assertEquals("Hours not set correctly", 4, period.getHours());
    }

    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        period.clear();
        assertEquals("Years not cleared", 0, period.getYears());
        assertEquals("Months not cleared", 0, period.getMonths());
        assertEquals("Days not cleared", 0, period.getDays());
        assertEquals("Hours not cleared", 0, period.getHours());
    }
}