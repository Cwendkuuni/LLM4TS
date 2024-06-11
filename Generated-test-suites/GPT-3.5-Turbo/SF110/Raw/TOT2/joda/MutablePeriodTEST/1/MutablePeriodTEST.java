import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3DT4H5M6S");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(5, period.getMinutes());
        assertEquals(6, period.getSeconds());
    }

    @Test
    public void testConstructorWithPeriodType() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, PeriodType.standard());
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
    }

    // Add more test cases for other methods

}