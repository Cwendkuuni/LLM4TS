import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Locale;

public class StdDateFormatTest {
    
    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        assertTrue(dateFormat.looksLikeISO8601("2021-12-31"));
        assertFalse(dateFormat.looksLikeISO8601("2021/12/31"));
    }

    @Test
    public void testParseAsISO8601() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parseAsISO8601("2021-12-31T23:59:59.999Z", new ParsePosition(0));
        assertNotNull(date);
    }

    @Test
    public void testParseAsRFC1123() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parseAsRFC1123("Fri, 31 Dec 2021 23:59:59 GMT", new ParsePosition(0));
        assertNotNull(date);
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2021-12-31T23:59:59.999Z");
        assertNotNull(date);
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0);
        StringBuffer result = dateFormat.format(date, sb, fp);
        assertNotNull(result);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        assertEquals(TimeZone.getTimeZone("GMT"), dateFormat.getTimeZone());
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
        StdDateFormat newDateFormat = dateFormat.withLocale(Locale.UK);
        assertEquals(Locale.UK, newDateFormat.getLocale());
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertNotNull(clonedDateFormat);
    }
}