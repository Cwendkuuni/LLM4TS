import org.joda.time.MutablePeriod;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String periodString = "P1Y2M3D";
        MutablePeriod expected = new MutablePeriod(1, 2, 0, 3);
        MutablePeriod result = MutablePeriod.parse(periodString, ISOPeriodFormat.standard());
        assertEquals(expected.getYears(), result.getYears());
        assertEquals(expected.getMonths(), result.getMonths());
        assertEquals(expected.getDays(), result.getDays());
    }

    // Add more test cases for other methods

}