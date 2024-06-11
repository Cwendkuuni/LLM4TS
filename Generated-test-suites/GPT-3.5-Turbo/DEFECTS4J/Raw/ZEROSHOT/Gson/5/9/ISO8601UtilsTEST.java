import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(2020, 10, 1, 12, 0, 0);
        String expected = "2020-11-01T12:00:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(2020, 10, 1, 12, 0, 0);
        String expected = "2020-11-01T12:00:00.000Z";
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date(2020, 10, 1, 12, 0, 0);
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        String expected = "2020-11-01T12:00:00+05:30";
        String actual = ISO8601Utils.format(date, false, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2020-11-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(2020, 10, 1, 12, 0, 0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithPos() throws ParseException {
        String dateStr = "2020-11-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(7);
        Date expected = new Date(2020, 10, 1, 12, 0, 0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);
        assertEquals(19, pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateStr = "invalid";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatInvalidTimeZone() {
        Date date = new Date(2020, 10, 1, 12, 0, 0);
        TimeZone tz = TimeZone.getTimeZone("invalid");
        ISO8601Utils.format(date, false, tz);
    }

}