import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatDateWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatDateWithTimezone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));
        assertNotNull(formattedDate);
    }

    @Test
    public void testParseValidISO8601Date() {
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse("2022-01-01T12:00:00Z", pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Failed to parse valid ISO8601 date");
        }
    }

    @Test
    public void testParseInvalidISO8601Date() {
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse("2022-01-01T25:00:00Z", pos);
            fail("Parsed invalid ISO8601 date");
        } catch (ParseException e) {
            // Expected exception
        }
    }
}