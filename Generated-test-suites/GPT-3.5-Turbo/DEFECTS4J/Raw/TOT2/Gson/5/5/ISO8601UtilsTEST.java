import static org.junit.Assert.*;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() {
        // Typical use case
        String dateString = "2022-12-31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateString, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }

        // Edge case with missing time zone
        String invalidDateString = "2022-12-31T23:59:59";
        pos.setIndex(0);
        try {
            Date parsedDate = ISO8601Utils.parse(invalidDateString, pos);
            fail("ParseException not thrown for missing time zone");
        } catch (ParseException e) {
            // Expected behavior
        }

        // Error scenario with invalid date string
        String invalidFormatString = "12-31-2022T23:59:59Z";
        pos.setIndex(0);
        try {
            Date parsedDate = ISO8601Utils.parse(invalidFormatString, pos);
            fail("ParseException not thrown for invalid format");
        } catch (ParseException e) {
            // Expected behavior
        }
    }
}