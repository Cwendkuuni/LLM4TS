import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }
}