import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        // Typical use case
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        
        // Edge case
        period = MutablePeriod.parse("");
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
        
        // Error scenario
        try {
            MutablePeriod.parse(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor() {
        // Typical use case
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        
        // Edge case
        period = new MutablePeriod(0);
        assertEquals(0, period.getTimeInMillis());
        
        // Error scenario
        period = new MutablePeriod(null);
        assertNotNull(period);
    }

    // Additional test cases for other methods...

}