import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(date, true, tz);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-11-07T13:20:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String invalidDateString = "2021/11/07T13:20:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidDateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String dateString = "2021-11-07T13:20:00+0500";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}