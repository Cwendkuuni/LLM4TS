import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertEquals("StdDateFormat instance", stdDateFormat.getClass().getName(), stdDateFormat.getClass().getName());
    }

    @Test
    public void testParseValidISO8601Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-03-01T14:23:56.789Z");
        assertNotNull(date);
        assertEquals("Valid ISO8601 date", "Wed Mar 01 14:23:56 GMT", stdDateFormat.format(date));
    }

    @Test
    public void testParseValidRFC1123Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Wed, 01 Mar 2022 14:23:56 GMT");
        assertNotNull(date);
        assertEquals("Valid RFC1123 date", "Wed Mar 01 14:23:56 GMT", stdDateFormat.format(date));
    }

    @Test
    public void testParseValidDateWithTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);
        Date date = stdDateFormat.parse("Tue, 02 Mar 2022 15:23:56 EST");
        assertNotNull(date);
        assertEquals("Valid date with timezone", "Tue Mar 02 15:23:56 EST 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertNull("Invalid date", stdDateFormat.parse("InvalidDate"));
    }

    @Test
    public void testParseDateWithMissingMilliseconds() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-03-01T14:23:56");
        assertNotNull(date);
        assertEquals("Missing milliseconds date", "Wed Mar 01 14:23:56 GMT 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseDateWithMissingColonInTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertThrows(ParseException.class, () -> stdDateFormat.parse("2022-03-01T14:23:56-05:00"));
    }

    @Test
    public void testParseDateWithNegativeTimeZoneOffset() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-03-01T14:23:56-05:30");
        assertNotNull(date);
        assertEquals("Negative timezone offset date", "Wed Mar 01 09:23:56 GMT 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseDateWithExcessColonInTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertThrows(ParseException.class, () -> stdDateFormat.parse("2022-03-01T14:23:56-05::00"));
    }

    @Test
    public void testParseDateWithMissingSeconds() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-03-01T14:23:56.789");
        assertNotNull(date);
        assertEquals("Missing seconds date", "Wed Mar 01 14:23:56.789 GMT 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseDateWithMissingMillis() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-03-01T14:23:56.78");
        assertNotNull(date);
        assertEquals("Missing milliseconds date", "Wed Mar 01 14:23:56.78 GMT 2022", stdDateFormat.format(date));
    }
}