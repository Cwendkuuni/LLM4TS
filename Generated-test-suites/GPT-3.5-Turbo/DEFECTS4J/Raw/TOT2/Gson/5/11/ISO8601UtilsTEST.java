import static org.junit.Assert.*;
import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical Use Case
        assertEquals("2021-09-01T12:30:45Z", ISO8601Utils.format(new Date(1630528245000)));

        // Edge Cases
        assertEquals("1970-01-01T00:00:00Z", ISO8601Utils.format(new Date(0))); // Epoch
        assertEquals("2021-12-31T23:59:59Z", ISO8601Utils.format(new Date(1640995199000))); // End of Year
    }

    @Test
    public void testParse() {
        // Typical Use Case
        Date date = ISO8601Utils.parse("2021-09-01T12:30:45Z", new ParsePosition(0));
        assertEquals(new Date(1630528245000), date);

        // Edge Cases
        try {
            // Date not in appropriate format
            ISO8601Utils.parse("2021/09/01T12:30:45Z", new ParsePosition(0));
            fail("ParseException expected");
        } catch (ParseException e) {
            // Expected
        }

        try {
            // No time zone indicator
            ISO8601Utils.parse("2021-09-01T12:30:45", new ParsePosition(0));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            // Invalid time zone indicator
            ISO8601Utils.parse("2021-09-01T12:30:45X", new ParsePosition(0));
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

}