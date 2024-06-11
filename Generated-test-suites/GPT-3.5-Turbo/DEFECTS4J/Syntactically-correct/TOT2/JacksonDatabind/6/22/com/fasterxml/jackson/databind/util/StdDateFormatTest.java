import org.junit.Test;

import java.text.ParseException;
import java.util.TimeZone;
import java.util.Locale;

import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testDefaultDateFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT");
        assertEquals(defaultTimeZone, stdDateFormat.getTimeZone());
        assertEquals(Locale.US, stdDateFormat.getLocale());
    }

    @Test
    public void testCloneDateFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("PST");
        StdDateFormat newDateFormat = stdDateFormat.withTimeZone(newTimeZone);
        assertEquals(newTimeZone, newDateFormat.getTimeZone());
    }

    @Test
    public void testParseValidISO8601Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2022-12-31T23:59:59.999Z";
        long expectedTime = 1672521599999L;
        assertEquals(expectedTime, stdDateFormat.parse(dateStr).getTime());
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String invalidDateStr = "invalid date";
        ParseException exception = assertThrows(ParseException.class, () -> stdDateFormat.parse(invalidDateStr));
        assertEquals("Can not parse date \"invalid date\": not compatible with any of standard forms (\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\", \"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\", \"EEE, dd MMM yyyy HH:mm:ss zzz\", \"yyyy-MM-dd\")", exception.getMessage());
    }

    // Add more test cases as needed for edge cases and exceptions

}