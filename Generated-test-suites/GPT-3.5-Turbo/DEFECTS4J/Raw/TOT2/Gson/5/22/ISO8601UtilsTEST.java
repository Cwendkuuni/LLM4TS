import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

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
    public void testFormatDateWithTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));
        assertNotNull(formattedDate);
    }

    @Test
    public void testParseValidDate() {
        String isoDate = "2022-11-15T10:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(isoDate, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Failed to parse valid date: " + e.getMessage());
        }
    }

    @Test
    public void testParseInvalidDate() {
        String invalidIsoDate = "2022-15-40";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(invalidIsoDate, pos);
            assertNull(parsedDate);
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Failed to parse date"));
        }
    }

    @Test
    public void testParseDateWithTimeZone() {
        String isoDateWithTimeZone = "2022-11-15T10:30:45+05:00";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(isoDateWithTimeZone, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Failed to parse date with timezone: " + e.getMessage());
        }
    }

    @Test
    public void testParseDateWithInvalidTimeZone() {
        String isoDateWithInvalidTimeZone = "2022-11-15T10:30:45+invalid";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(isoDateWithInvalidTimeZone, pos);
            assertNull(parsedDate);
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Invalid time zone"));
        }
    }

}