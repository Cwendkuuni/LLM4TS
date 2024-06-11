
import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String periodStr = "P1Y2M3W4D";
        MutablePeriod period = MutablePeriod.parse(periodStr);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
    }

    @Test
    public void testSetYears() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(5);
        assertEquals(5, period.getYears());
    }

    @Test
    public void testAddYears() {
        MutablePeriod period = new MutablePeriod();
        period.addYears(5);
        assertEquals(5, period.getYears());
    }

    // Add more test cases for the remaining methods

}