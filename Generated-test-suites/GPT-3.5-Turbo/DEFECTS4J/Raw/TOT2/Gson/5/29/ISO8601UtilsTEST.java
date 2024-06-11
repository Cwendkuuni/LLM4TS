import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-10-30T15:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testInvalidDateFormat() throws ParseException {
        String dateString = "2021-10-30";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testInvalidTimeZoneIndicator() throws ParseException {
        String dateString = "2021-10-30T15:30:00X";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test
    public void testCheckOffset() {
        boolean result = ISO8601Utils.checkOffset("2021-10-30T15:30:00Z", 10, 'T');
        assertTrue(result);
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, 5, 2);
        assertEquals("05", buffer.toString());
    }

    @Test
    public void testIndexOfNonDigit() {
        int result = ISO8601Utils.indexOfNonDigit("2021-10-30T15:30:00Z", 0);
        assertEquals(4, result);
    }
}