import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ISO8601UtilsTest {

    private ISO8601Utils ISO8601Utils;

    @Before
    public void setup() {
        ISO8601Utils = new ISO8601Utils();
    }

    // Test case for formatting a date without time and milliseconds
    @Test
    public void testFormatDateWithoutTimeAndMilliseconds() {
        Date date = new Date(1633059200000L); // Thu, 13 Sep 2018 00:00:00 GMT
        String expected = "2018-09-13";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    // Test case for formatting a date with time and milliseconds
    @Test
    public void testFormatDateWithTimeAndMilliseconds() {
        Date date = new Date(1633059200000L + 12345); // Thu, 13 Sep 2018 00:00:12.345 GMT
        String expected = "2018-09-13T00:00:12.345Z";
        String actual = ISO8601Utils.format(date, true, null);
        assertEquals(expected, actual);
    }

    // Test case for formatting a date with UTC timezone
    @Test
    public void testFormatDateWithUTC() {
        Date date = new Date(1633059200000L + 12345); // Thu, 13 Sep 2018 00:00:12.345 GMT
        String expected = "2018-09-13T00:00:12.345Z";
        String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone(ISO8601Utils.UTC_ID));
        assertEquals(expected, actual);
    }

    // Test case for parsing a date with time and milliseconds
    @Test
    public void testParseDateWithTimeAndMilliseconds() throws ParseException {
        String dateString = "2018-09-13T00:00:12.345Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date expectedDate = new Date(1633059200000L + 12345); // Thu, 13 Sep 2018 00:00:12.345 GMT
        Date actualDate = ISO8601Utils.parse(dateString, parsePosition);
        assertEquals(expectedDate, actualDate);
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    // Test case for parsing a date without time and milliseconds
    @Test
    public void testParseDateWithoutTimeAndMilliseconds() throws ParseException {
        String dateString = "2018-09-13";
        ParsePosition parsePosition = new ParsePosition(0);
        Date expectedDate = new Date(1633059200000L); // Thu, 13 Sep 2018 00:00:00 GMT
        Date actualDate = ISO8601Utils.parse(dateString, parsePosition);
        assertEquals(expectedDate, actualDate);
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    // Test case for parsing a date with an invalid format
    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        String dateString = "2018-09-13T00:00:12.345A";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    // Test case for parsing a date with an invalid timezone offset
    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimezoneOffset() throws ParseException {
        String dateString = "2018-09-13T00:00:12.345+12:60";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    // Test case for parsing a date with a null input
    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithNullInput() {
        ISO8601Utils.parse(null, new ParsePosition(0));
    }
}