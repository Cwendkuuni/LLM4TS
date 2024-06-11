import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        
        // Edge case: Test with milliseconds
        String formattedDateWithMillis = ISO8601Utils.format(date, true);
        assertNotNull(formattedDateWithMillis);
        
        // Error scenario: Test with null date
        String formattedNullDate = ISO8601Utils.format(null);
        assertNull(formattedNullDate);
    }

    @Test
    public void testParse() {
        // Typical use case
        String dateStr = "2022-05-20T15:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Exception should not be thrown for valid date format");
        }
        
        // Edge case: Test with optional seconds and milliseconds
        String dateStrOptional = "2022-05-20T15:30:30.123Z";
        pos.setIndex(0);
        try {
            Date parsedDateOptional = ISO8601Utils.parse(dateStrOptional, pos);
            assertNotNull(parsedDateOptional);
        } catch (ParseException e) {
            fail("Exception should not be thrown for valid date format with optional fields");
        }
        
        // Error scenario: Test with invalid date format
        String invalidDateStr = "2022-05-20X15:30:00Z";
        pos.setIndex(0);
        try {
            Date parsedInvalidDate = ISO8601Utils.parse(invalidDateStr, pos);
            fail("Exception should be thrown for invalid date format");
        } catch (ParseException e) {
            // Expected exception
        }
    }
}