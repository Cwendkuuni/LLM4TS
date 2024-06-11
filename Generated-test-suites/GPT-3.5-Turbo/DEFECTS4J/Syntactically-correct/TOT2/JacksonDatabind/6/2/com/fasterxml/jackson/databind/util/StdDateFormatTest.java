import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {
    
    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+1");
        dateFormat.setTimeZone(timeZone);
        assertEquals(timeZone, dateFormat._timezone);
    }
    
    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "2021-09-30T10:30:00.000Z";
        try {
            assertNotNull(dateFormat.parse(dateStr));
        } catch (ParseException e) {
            fail("Failed to parse ISO8601 date format");
        }
    }
    
    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "Thu, 30 Sep 2021 10:30:00 GMT";
        try {
            assertNotNull(dateFormat.parse(dateStr));
        } catch (ParseException e) {
            fail("Failed to parse RFC1123 date format");
        }
    }
    
    @Test(expected = ParseException.class)
    public void testInvalidDateFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "InvalidDate";
        dateFormat.parse(dateStr);
    }

    @Test
    public void testFormatISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedDate = dateFormat.format(date, buffer, new FieldPosition(0));
        assertNotNull(formattedDate);
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+2");
        StdDateFormat newDateFormat = dateFormat.withTimeZone(timeZone);
        assertEquals(timeZone, newDateFormat._timezone);
    }

    @Test
    public void testWithLocale() {
        StdDateFormat dateFormat = new StdDateFormat();
        Locale locale = Locale.CANADA;
        StdDateFormat newDateFormat = dateFormat.withLocale(locale);
        assertEquals(locale, newDateFormat._locale);
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertNotNull(clonedDateFormat);
        assertNotSame(dateFormat, clonedDateFormat);
    }

}