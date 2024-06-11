
import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Basic test case with default timezone and no milliseconds precision
        TimeZone.setDefault(TimeZone.getTimeZone("GMT")); // Set default timezone to GMT
        Date date = new GregorianCalendar(2022, 0, 1, 10, 30, 0).getTime();
        String expected = "2022-01-01T10:30:00Z";
        assertEquals(expected, ISO8601Utils.format(date));
        
        // Test case with specified timezone and milliseconds precision
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        date = new GregorianCalendar(2022, 0, 1, 10, 30, 0).getTime();
        expected = "2022-01-01T10:30:00.000-08:00";
        assertEquals(expected, ISO8601Utils.format(date, true, timeZone));
    }

    @Test
    public void testParse() throws ParseException {
        // Basic test case
        String dateString = "2022-01-01T10:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2022, 0, 1, 10, 30, 0).getTime();
        assertEquals(expected, ISO8601Utils.parse(dateString, pos));

        // Test case with non-UTC timezone
        dateString = "2022-01-01T10:30:00.000-08:00";
        pos = new ParsePosition(0);
        expected = new GregorianCalendar(2022, 0, 1, 10, 30, 0).getTime();
        assertEquals(expected, ISO8601Utils.parse(dateString, pos));

        // Test case with no time component
        dateString = "2022-01-01";
        pos = new ParsePosition(0);
        expected = new GregorianCalendar(2022, 0, 1).getTime();
        assertEquals(expected, ISO8601Utils.parse(dateString, pos));

        // Test case with milliseconds and leap seconds
        dateString = "2022-01-01T10:30:59.123-08:00";
        pos = new ParsePosition(0);
        expected = new GregorianCalendar(2022, 0, 1, 10, 31, 0).getTime(); // Leap seconds should be truncated to 59
        assertEquals(expected, ISO8601Utils.parse(dateString, pos));
    }

}
