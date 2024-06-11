import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY);
        LocalDateTime dt = new LocalDateTime(2023, 1, 31, 0, 0);
        long instant = dt.toInstant().getMillis();
        int expectedMonth = 1;
        int actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);
    }
}