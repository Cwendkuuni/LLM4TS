import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY);
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int expectedMonth = DateTimeConstants.JANUARY;

        int actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);
    }
}