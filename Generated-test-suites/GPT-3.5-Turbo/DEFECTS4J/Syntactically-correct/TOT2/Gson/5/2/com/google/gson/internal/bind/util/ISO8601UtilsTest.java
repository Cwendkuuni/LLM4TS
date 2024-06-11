import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        // Typical case
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        // Edge case
        formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParseDate() {
        // Typical case
        String dateStr = "2022-12-31";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
        // Edge case - invalid date format
        dateStr = "2022/12/31";
        pos.setIndex(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            fail("ParseException not thrown for invalid date format");
        } catch (ParseException e) {
            // Exception expected
        }
        // Error scenario - null date
        try {
            Date parsedDate = ISO8601Utils.parse(null, pos);
            fail("ParseException not thrown for null date string");
        } catch (ParseException e) {
            // Exception expected
        }
        // Error scenario - null ParsePosition
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, null);
            fail("ParseException not thrown for null ParsePosition");
        } catch (ParseException e) {
            // Exception expected
        }
    }

    // Add more test cases for each method if needed...

}