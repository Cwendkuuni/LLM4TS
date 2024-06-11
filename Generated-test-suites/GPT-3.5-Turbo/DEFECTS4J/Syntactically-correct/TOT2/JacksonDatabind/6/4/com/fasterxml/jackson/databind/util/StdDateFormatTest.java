import static org.junit.Assert.*;
import org.junit.Test;
import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.Locale;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(newTimeZone);
        assertEquals(newTimeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        String iso8601Date = "2022-04-15T10:30:00.000Z";
        Date parsedDate = dateFormat.parse(iso8601Date);
        assertNotNull(parsedDate);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        String rfc1123Date = "Fri, 15 Apr 2022 10:30:00 GMT";
        Date parsedDate = dateFormat.parse(rfc1123Date);
        assertNotNull(parsedDate);
    }

    @Test
    public void testFormatISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        DateFormat df = dateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        df.format(date, buffer, null);
        String formattedDate = buffer.toString();
        assertNotNull(formattedDate);
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertNotSame(dateFormat, clonedDateFormat);
    }

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        assertTrue(dateFormat.looksLikeISO8601("2022-04-15T10:30:00.000Z"));
    }

    @Test
    public void testHasTimeZone() {
        assertTrue(StdDateFormat.hasTimeZone("2022-04-15T10:30:00.000Z"));
    }

    @Test
    public void testDefaultTimeZone() {
        assertEquals(TimeZone.getTimeZone("GMT"), StdDateFormat.getDefaultTimeZone());
    }

}