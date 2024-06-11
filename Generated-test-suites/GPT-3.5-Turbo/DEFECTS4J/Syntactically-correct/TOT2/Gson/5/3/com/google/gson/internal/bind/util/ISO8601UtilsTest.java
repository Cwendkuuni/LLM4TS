import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        // Add more assertions to validate the formatting of the date
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
        // Add more assertions to validate the formatting of the date with milliseconds
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertNotNull(formattedDate);
        // Add more assertions to validate the formatting of the date with a specified timezone
    }

    @Test
    public void testParse() {
        String dateStr = "2022-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
            // Add more assertions to validate the parsing of the date
        } catch (ParseException e) {
            fail("Parsing date failed: " + e.getMessage());
        }
    }

    // Add more test methods with different scenarios for comprehensive testing

}