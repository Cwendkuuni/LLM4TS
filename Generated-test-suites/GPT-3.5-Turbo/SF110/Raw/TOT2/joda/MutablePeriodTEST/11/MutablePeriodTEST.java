import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        // Typical use case
        MutablePeriod period = MutablePeriod.parse("P1Y2M");
        assertNotNull(period);

        // Edge case: empty string
        period = MutablePeriod.parse("");
        assertNotNull(period);

        // Exception scenario: invalid format
        period = MutablePeriod.parse("InvalidFormat");
        assertNull(period);
    }

    @Test
    public void testConstructor() {
        // Typical use case
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertNotNull(period);

        // Edge case: negative values
        period = new MutablePeriod(-1, -2, -3, -4, -5, -6, -7, -8);
        assertNotNull(period);
    }

    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
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

    // Include more test cases for other methods

}