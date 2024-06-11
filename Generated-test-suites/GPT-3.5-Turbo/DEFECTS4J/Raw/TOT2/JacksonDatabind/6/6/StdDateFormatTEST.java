import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01T12:00:00.000Z");
        assertNotNull(date);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Sat, 01 Jan 2022 12:00:00 GMT");
        assertNotNull(date);
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        try {
            dateFormat.parse("Invalid Date");
            fail("ParseException expected");
        } catch (ParseException e) {
            // ParseException expected
        }
    }

    @Test
    public void testFormatISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        dateFormat.format(date, buffer, null);
        assertTrue(buffer.toString().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"));
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        assertEquals(TimeZone.getTimeZone("PST"), dateFormat.getTimeZone());
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat newDateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("GMT"));
        assertEquals(TimeZone.getTimeZone("GMT"), newDateFormat.getTimeZone());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat newDateFormat = dateFormat.withLocale(Locale.FRANCE);
        assertEquals(Locale.FRANCE, newDateFormat.getLocale());
    }

}