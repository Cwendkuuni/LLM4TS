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
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(date, true, tz);
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2021-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testInvalidDateFormat() throws ParseException {
        String dateString = "2021-01-01 12:00:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimezone() throws ParseException {
        String dateString = "2021-01-01T12:00:00GMT";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}