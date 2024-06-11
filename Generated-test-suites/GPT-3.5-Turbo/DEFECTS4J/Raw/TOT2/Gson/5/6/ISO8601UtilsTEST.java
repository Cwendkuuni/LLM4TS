import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

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
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParseDate() {
        String dateStr = "2021-09-30T12:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("ParseException thrown while parsing date: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateStr = "2021/09/30T12:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidInteger() {
        String value = "abc";
        try {
            ISO8601Utils.parseInt(value, 0, value.length());
        } catch (NumberFormatException e) {
            assertTrue(e.getMessage().contains("Invalid number"));
            throw e;
         }
    }

}