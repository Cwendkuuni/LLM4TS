import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "2021-07-15T13:45:30.500Z";

        try {
            assertEquals(1626357930500L, dateFormat.parse(dateStr).getTime());
        } catch (ParseException e) {
            fail("ParseException thrown unexpectedly");
        }
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "Thu, 15 Jul 2021 13:45:30 GMT";

        try {
            assertEquals(1626357930000L, dateFormat.parse(dateStr).getTime());
        } catch (ParseException e) {
            fail("ParseException thrown unexpectedly");
        }
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String invalidDateStr = "INVALID DATE";

        try {
            dateFormat.parse(invalidDateStr);
            fail("ParseException expected but not thrown");
        } catch (ParseException e) {
            // Expected ParseException, test passes
        }
    }

    @Test
    public void testFormatISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        long timestamp = 1626357930500L;
        String expectedDateStr = "2021-07-15T13:45:30.500Z";

        assertEquals(expectedDateStr, dateFormat.format(new Date(timestamp)));
    }

    @Test
    public void testFormatRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        long timestamp = 1626357930000L;
        String expectedDateStr = "Thu, 15 Jul 2021 13:45:30 GMT";

        assertEquals(expectedDateStr, dateFormat.format(new Date(timestamp)));
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("EST");

        dateFormat.setTimeZone(newTimeZone);
        assertEquals(newTimeZone, dateFormat.getTimeZone());
    }
}