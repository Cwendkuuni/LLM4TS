import org.junit.*;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(0);
        String expected = "1970-01-01T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(0);
        String expected = "1970-01-01T00:00:00.000Z";
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date(0);
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String expected = "1970-01-01T00:00:00+00:00";
        String actual = ISO8601Utils.format(date, false, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "1970-01-01T00:00:00Z";
        Date expected = new Date(0);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateString = "1970-01-01T00:00:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        String dateString = "1970-01-01T00:00:00+00:00";
        Date expected = new Date(0);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected, actual);
    }
}