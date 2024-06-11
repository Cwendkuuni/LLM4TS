import org.junit.Test;
import org.joda.time.DateTimeConstants;
import org.joda.time.JodaTimeUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.ISO_8601_DATETIME, DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGetMonthOfYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.ISO_8601_DATETIME, DateTimeConstants.JANUARY);
        LocalDateTime testDate = new LocalDateTime(2022, 1, 31, 12, 0);
        int actual = field.get(testDate.getMillis());
        assertEquals(1, actual);
    }

    @Test
    public void testAddMonths() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.ISO_8601_DATETIME, DateTimeConstants.JANUARY);
        LocalDateTime testDate = new LocalDateTime(2022, 1, 31, 12, 0);
        long expected = new LocalDateTime(2022, 2, 28, 12, 0).getMillis();
        long actual = field.add(testDate.getMillis(), 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddMonthsWrapping() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.ISO_8601_DATETIME, DateTimeConstants.JANUARY);
        LocalDateTime testDate = new LocalDateTime(2022, 1, 31, 12, 0);
        long expected = new LocalDateTime(2022, 12, 31, 12, 0).getMillis();
        long actual = field.addWrapField(testDate.getMillis(), 11);
        assertEquals(expected, actual);
    }
}