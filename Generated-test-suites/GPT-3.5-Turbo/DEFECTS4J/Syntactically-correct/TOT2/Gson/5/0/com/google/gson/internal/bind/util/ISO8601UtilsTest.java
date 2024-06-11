import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
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
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() {
        String dateString = "2021-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateString, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Parse exception thrown");
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidParse() throws ParseException {
        String dateString = "2021-01-01T12:00-0400";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

}