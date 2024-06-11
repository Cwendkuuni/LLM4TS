
import static org.junit.Assert.*;
import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        // Add more assertions if required
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
        // Add more assertions if required
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+05:30");
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        assertNotNull(formattedDate);
        // Add more assertions if required
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2022-01-01T10:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertNotNull(parsedDate);
        // Add more assertions if required
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateString() throws ParseException {
        String dateString = "InvalidDate";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        // Add more assertions if required
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIncompleteDateString() throws ParseException {
        String dateString = "2022-01-01T10:00:00+";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        // Add more assertions if required
    }

    // Add more test cases for other methods if required

}

