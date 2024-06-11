import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormatInstance() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertEquals("StdDateFormat instance created with default timezone and locale", DEFAULT_TIMEZONE, stdDateFormat.getTimeZone());
        assertEquals("StdDateFormat instance created with default locale", Locale.US, stdDateFormat.getLocale());
    }

    @Test
    public void testStdDateFormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        StdDateFormat stdDateFormat = new StdDateFormat(timeZone, Locale.US);
        assertEquals("StdDateFormat instance created with custom timezone and default locale", timeZone, stdDateFormat.getTimeZone());
    }

    @Test
    public void testStdDateFormatWithLocale() {
        Locale locale = Locale.CHINESE;
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getDefault(), locale);
        assertEquals("StdDateFormat instance created with default timezone and custom locale", locale, stdDateFormat.getLocale());
    }

    @Test
    public void testStdDateFormatClone() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getDefault(), Locale.US);
        StdDateFormat clonedStdDateFormat = stdDateFormat.clone();
        assertEquals("Cloned StdDateFormat instance should have the same properties as original instance", stdDateFormat, clonedStdDateFormat);
    }

    @Test
    public void testStdDateFormatParseISO8601Format() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Date parsed using ISO-8601 format should be the last day of the year 2022 at 23:59:59.000Z", new Date(Long.parseLong("1671161599000")), date);
    }

    @Test
    public void testStdDateFormatParseISO8601FormatWithTimeZoneOffset() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.000+01:00");
        assertEquals("Date parsed using ISO-8601 format with timezone offset should be the last day of the year 2022 at 23:59:59.000+01:00", new Date(Long.parseLong("1671208099000")), date);
    }

    @Test
    public void testStdDateFormatParseRFC1123Format() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Fri, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Date parsed using RFC 1123 format should be the last day of the year 2022 at 23:59:59 GMT", new Date(Long.parseLong("1671161599000")), date);
    }

    @Test
    public void testStdDateFormatParseRFC1123FormatWithTimeZoneOffset() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Fri, 31 Dec 2022 23:59:59 +01:00");
        assertEquals("Date parsed using RFC 1123 format with timezone offset should be the last day of the year 2022 at 23:59:59 +01:00", new Date(Long.parseLong("1671208099000")), date);
    }

    @Test
    public void testStdDateFormatParseInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertThrows(ParseException.class, () -> stdDateFormat.parse("InvalidDateString"));
    }

    @Test
    public void testStdDateFormatParseNullInput() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertNull("Parse method should return null when passed a null input", stdDateFormat.parse(null));
    }
}