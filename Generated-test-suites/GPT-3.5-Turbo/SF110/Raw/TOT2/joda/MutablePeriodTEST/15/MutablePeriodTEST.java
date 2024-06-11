import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D", ISOPeriodFormat.standard());
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }

    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        period.clear();
        assertEquals(0, period.size());
    }

    @Test
    public void testSetValue() {
        MutablePeriod period = new MutablePeriod();
        period.setValue(PeriodType.YEAR_INDEX, 5);
        assertEquals(5, period.getYears());
    }

    // Testing edge cases and error scenarios can be added here

    // More test cases can be added for the remaining methods

}