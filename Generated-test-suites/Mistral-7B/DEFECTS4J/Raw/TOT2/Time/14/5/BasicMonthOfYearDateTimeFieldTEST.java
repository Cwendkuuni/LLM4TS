import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.field.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        Chronology chronology = DateTimeUtils.getUTC();
        BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());

        long instant = new BaseDateTime(1420070400000L, chronology).getMillis(); // 2015-01-01
        int expectedMonth = 1;
        int actualMonth = monthField.get(instant);

        assertEquals(expectedMonth, actualMonth);
    }
}