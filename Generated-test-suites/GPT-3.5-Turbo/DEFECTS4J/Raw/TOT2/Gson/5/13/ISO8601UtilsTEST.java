import org.junit.Test;
import java.util.Date;
import java.text.ParseException;
import java.text.ParsePosition;

// Define your own package for the test class
//import com.example.project.test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        // Add assertions to verify the formatted date
        // For example:
        // assertEquals("2021-12-31T23:59:59Z", formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        // Add assertions to verify the formatted date with milliseconds
        // For example:
        // assertTrue(formattedDate.endsWith(".000Z"));
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        // Add assertions to verify the formatted date with a specific time zone
        // For example:
        // assertTrue(formattedDate.endsWith("+00:00"));
    }

    @Test
    public void testParse() {
        String isoDateString = "2021-12-31T23:59:59Z";
        Date parsedDate = null;
        try {
            ParsePosition pos = new ParsePosition(0);
            parsedDate = ISO8601Utils.parse(isoDateString, pos);
        } catch (ParseException e) {
            // Handle the ParseException
        }
        // Add assertions to verify the parsed date
        // For example:
        // assertNotNull(parsedDate);
    }

    @Test
    public void testParseInvalidDateString() {
        String invalidDateString = "invalidDate";
        Date parsedDate = null;
        try {
            ParsePosition pos = new ParsePosition(0);
            parsedDate = ISO8601Utils.parse(invalidDateString, pos);
        } catch (ParseException e) {
            // Exception expected, so test should pass
        }
        assertNull(parsedDate);
    }

    // Add more test cases for edge cases and exception handling scenarios

}