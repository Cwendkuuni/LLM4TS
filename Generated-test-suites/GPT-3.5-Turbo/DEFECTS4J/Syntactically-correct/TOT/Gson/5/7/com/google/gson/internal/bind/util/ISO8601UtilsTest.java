import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class ISO8601UtilsTest {

    @Test(expected = ParseException.class)
    public void testInvalidFormat() throws ParseException {
        String invalidDate = "2021-05-15";
        ISO8601Utils.parse(invalidDate, new ParsePosition(0));
    }

    @Test
    public void testValidDateWithMillisAndTimeZone() throws ParseException {
        String isoDate = "2021-05-15T09:30:45.123+05:30";
        Date expectedDate = new Date(121, 4, 15, 9, 30, 45);
        expectedDate.setTime(expectedDate.getTime() + 123);
        TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");

        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        assertEquals(expectedDate, parsedDate);
        assertEquals(tz, parsedDate.getTimeZone());
    }

    @Test
    public void testValidDateWithTimeZone() throws ParseException {
        String isoDate = "2021-05-15T09:30:45+05:30";
        Date expectedDate = new Date(121, 4, 15, 9, 30, 45);
        TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");

        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        assertEquals(expectedDate, parsedDate);
        assertEquals(tz, parsedDate.getTimeZone());
    }

    @Test
    public void testValidDateWithNegativeTimeZone() throws ParseException {
        String isoDate = "2021-05-15T09:30:45-02:30";
        Date expectedDate = new Date(121, 4, 15, 9, 30, 45);
        TimeZone tz = TimeZone.getTimeZone("GMT-02:30");

        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        assertEquals(expectedDate, parsedDate);
        assertEquals(tz, parsedDate.getTimeZone());
    }

    @Test
    public void testValidDateWithZeroTimeZone() throws ParseException {
        String isoDate = "2021-05-15T09:30:45Z";
        Date expectedDate = new Date(121, 4, 15, 9, 30, 45);
        TimeZone tz = TimeZone.getTimeZone("UTC");

        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        assertEquals(expectedDate, parsedDate);
        assertEquals(tz, parsedDate.getTimeZone());
    }

    @Test
    public void testValidDateWithoutTimeZone() throws ParseException {
        String isoDate = "2021-05-15T09:30:45";
        Date expectedDate = new Date(121, 4, 15, 9, 30, 45);

        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        assertEquals(expectedDate, parsedDate);
        assertEquals(TimeZone.getDefault(), parsedDate.getTimeZone());
    }

    @Test
    public void testValidDateWithoutTime() throws ParseException {
        String isoDate = "2021-05-15";
        Date expectedDate = new Date(121, 4, 15);

        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        assertEquals(expectedDate, parsedDate);
        assertEquals(TimeZone.getDefault(), parsedDate.getTimeZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeZone() throws ParseException {
        String isoDate = "2021-05-15T09:30:45+invalid";
        ISO8601Utils.parse(isoDate, new ParsePosition(0));
    }

}