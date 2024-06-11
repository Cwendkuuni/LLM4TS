import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParse() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2021-09-15T10:30:45.123Z";
        try {
            Date parsedDate = stdDateFormat.parse(dateStr);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("ParseException should not be thrown for valid date format");
        }
    }

    @Test
    public void testParseWithNullString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        try {
            stdDateFormat.parse(null);
            fail("NullPointerException should be thrown for null dateStr");
        } catch (NullPointerException e) {
            // Expected behavior, test passes
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer formattedDate = stdDateFormat.format(date, stringBuffer, fieldPosition);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithNullBuffer() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        FieldPosition fieldPosition = new FieldPosition(0);
        try {
            stdDateFormat.format(date, null, fieldPosition);
            fail("NullPointerException should be thrown for null StringBuffer");
        } catch (NullPointerException e) {
            // Expected behavior, test passes
        }
    }

    // Add more test cases for other methods as needed

}