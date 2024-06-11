import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Test;


public class ISO8601UtilsTest {

    @Test
    public void testFormatDefaultTimeZone() {
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
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(date, true, timeZone);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-05-25T10:15:30Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateString = "Invalid Date String";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateString = "2021-05-25T10:15:30";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test
    public void testCheckOffsetPositive() {
        String value = "2021-05-25T10:15:30Z";
        boolean result = ISO8601Utils.checkOffset(value, 10, 'T');
        assertTrue(result);
    }

    @Test
    public void testCheckOffsetNegative() {
        String value = "2021-05-25T10:15:30Z";
        boolean result = ISO8601Utils.checkOffset(value, 7, 'T');
        assertFalse(result);
    }

    @Test
    public void testParseInt() {
        String value = "2021-05-25T10:15:30Z";
        int result = ISO8601Utils.parseInt(value, 0, 4);
        assertEquals(2021, result);
    }

    @Test
    public void testIndexOfNonDigit() {
        String string = "2021-05-25T10:15:30Z";
        int result = ISO8601Utils.indexOfNonDigit(string, 4);
        assertEquals(7, result);
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, 5, 4);
        assertEquals("0005", buffer.toString());
    }

}