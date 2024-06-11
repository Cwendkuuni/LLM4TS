import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test with date and without millis
        Date date1 = new Date(120, 0, 1, 12, 0, 0);
        String expected1 = "2020-01-01T12:00:00Z";
        assertEquals(expected1, ISO8601Utils.format(date1));

        // Test with date, millis, and custom timezone
        Date date2 = new Date(120, 0, 1, 12, 0, 0);
        String expected2 = "2020-01-01T12:00:00.000+02:00";
        assertEquals(expected2, ISO8601Utils.format(date2, true, TimeZone.getTimeZone("IST")));
    }

    @Test
    public void testParse() throws ParseException {
        // Test parsing date with default timezone
        String date1 = "2020-01-01T12:00:00Z";
        ParsePosition pos1 = new ParsePosition(0);
        Date expected1 = new Date(120, 0, 1, 12, 0, 0);
        assertEquals(expected1, ISO8601Utils.parse(date1, pos1));
        assertEquals(date1.length(), pos1.getIndex());

        // Test parsing date with custom timezone and milliseconds
        String date2 = "2020-01-01T12:00:00.000+02:00";
        ParsePosition pos2 = new ParsePosition(0);
        Date expected2 = new Date(120, 0, 1, 12, 0, 0);
        assertEquals(expected2, ISO8601Utils.parse(date2, pos2));
        assertEquals(date2.length(), pos2.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        // Test parsing invalid date
        String date = "2020-00-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }
}