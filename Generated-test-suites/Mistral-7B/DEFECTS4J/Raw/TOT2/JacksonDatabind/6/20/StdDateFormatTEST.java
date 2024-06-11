import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public class StdDateFormatTest {

    private static final StdDateFormat STD_DATE_FORMAT = StdDateFormat.instance;

    @Test
    public void testParse_ValidISO8601() throws ParseException {
        String iso8601DateString = "2022-03-14T15:27:30.123Z";
        Date parsedDate = STD_DATE_FORMAT.parse(iso8601DateString);
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(iso8601DateString);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParse_ValidRFC1123() throws ParseException {
        String rfc1123DateString = "Mon, 14 Mar 2022 15:27:30 GMT";
        Date parsedDate = STD_DATE_FORMAT.parse(rfc1123DateString);
        Date expectedDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(rfc1123DateString);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParse_ValidPlainDate() throws ParseException {
        String plainDateString = "2022-03-14";
        Date parsedDate = STD_DATE_FORMAT.parse(plainDateString);
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(plainDateString);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidFormat() throws ParseException {
        String invalidFormatString = "not_a_date";
        STD_DATE_FORMAT.parse(invalidFormatString);
        fail("Should have thrown ParseException for invalid format: " + invalidFormatString);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingYear() throws ParseException {
        String missingYearString = "03-14";
        STD_DATE_FORMAT.parse(missingYearString);
        fail("Should have thrown ParseException for missing year: " + missingYearString);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingMonth() throws ParseException {
        String missingMonthString = "2022-14";
        STD_DATE_FORMAT.parse(missingMonthString);
        fail("Should have thrown ParseException for missing month: " + missingMonthString);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingDay() throws ParseException {
        String missingDayString = "2022-03-";
        STD_DATE_FORMAT.parse(missingDayString);
        fail("Should have thrown ParseException for missing day: " + missingDayString);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingColon() throws ParseException {
        String missingColonString = "2022-03-14T152730.123Z";
        STD_DATE_FORMAT.parse(missingColonString);
        fail("Should have thrown ParseException for missing colon: " + missingColonString);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidTimeZone() throws ParseException {
        String invalidTimeZoneString = "2022-03-14T15:27:30.123InvalidTimeZone";
        STD_DATE_FORMAT.parse(invalidTimeZoneString);
        fail("Should have thrown ParseException for invalid timezone: " + invalidTimeZoneString);
    }

    @Test
    public void testFormat_ValidISO8601() {
        Date iso8601Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2022-03-14T15:27:30.123Z");
        String formattedDateString = STD_DATE_FORMAT.format(iso8601Date, new StringBuffer(), new FieldPosition(0));
        assertEquals("2022-03-14T15:27:30.123Z", formattedDateString.toString());
    }

    @Test
    public void testFormat_ValidRFC1123() {
        Date rfc1123Date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse("Mon, 14 Mar 2022 15:27:30 GMT");
        String formattedDateString = STD_DATE_FORMAT.format(rfc1123Date, new StringBuffer(), new FieldPosition(0));
        assertEquals("Mon, 14 Mar 2022 15:27:30 GMT", formattedDateString.toString());
    }

    @Test
    public void testFormat_ValidPlainDate() {
        Date plainDate = new SimpleDateFormat("yyyy-MM-dd").parse("2022-03-14");
        String formattedDateString = STD_DATE_FORMAT.format(plainDate, new StringBuffer(), new FieldPosition(0));
        assertEquals("2022-03-14", formattedDateString.toString());
    }

    @Test
    public void testClone() {
        StdDateFormat originalStdDateFormat = StdDateFormat.instance;
        StdDateFormat clonedStdDateFormat = (StdDateFormat) originalStdDateFormat.clone();
        // Verify that the cloned instance has the same behavior as the original
        Date originalDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2022-03-14T15:27:30.123Z");
        Date parsedDate = clonedStdDateFormat.parse(STD_DATE_FORMAT.format(originalDate, new StringBuffer(), new FieldPosition(0)));
        assertEquals(originalDate, parsedDate);
    }
}