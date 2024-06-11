
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case with a date without milliseconds
        Date date = new Date(1234567890L);
        String expected = "2009-02-13T23:31:30Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);

        // Typical use case with a date with milliseconds
        date = new Date(1234567890123L);
        expected = "2009-02-13T23:31:30.123Z";
        actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);

        // Edge case with a date before 1970
        date = new Date(-1234567890L);
        expected = "1929-11-18T22:28:30Z";
        actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        // Typical use case with a date without milliseconds
        String dateStr = "2009-02-13T23:31:30Z";
        Date expected = new Date(1234567890000L);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);

        // Typical use case with a date with milliseconds
        dateStr = "2009-02-13T23:31:30.123Z";
        expected = new Date(1234567890123L);
        pos = new ParsePosition(0);
        actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);

        // Edge case with a date before 1970
        dateStr = "1929-11-18T22:28:30Z";
        expected = new Date(-1234567890000L);
        pos = new ParsePosition(0);
        actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);

        // Error scenario with an invalid date string
        dateStr = "2009-02-13T23:31:30";
        pos = new ParsePosition(0);
        try {
            actual = ISO8601Utils.parse(dateStr, pos);
            fail("ParseException was expected but not thrown");
        } catch (ParseException e) {
            // Exception was thrown as expected, test passed
        }
    }

}
