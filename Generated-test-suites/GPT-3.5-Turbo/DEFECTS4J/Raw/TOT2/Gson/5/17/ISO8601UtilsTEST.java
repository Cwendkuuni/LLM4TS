import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

public class ISO8601UtilsTest {
    
    @Test
    public void testFormat() {
        Date date = new Date(1626271200000L); // 2021-07-14T12:00:00Z
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-07-14T12:00:00Z", formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(1626271200123L); // 2021-07-14T12:00:00.123Z
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("2021-07-14T12:00:00.123Z", formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2021-07-14T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateStr = "2021-07-14 12:00:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateStr = "2021-13-14T12:00:00Z"; // Invalid month (13)
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimezone() throws ParseException {
        String dateStr = "2021-07-14T12:00:00+10:00"; // Incorrect timezone format
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

}