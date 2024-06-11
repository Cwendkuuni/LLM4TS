
import org.junit.*;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // typical use case
        Date date = new Date(1234567890L); // 2009-02-13T23:31:30Z
        String expected = "2009-02-13T23:31:30Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
       
        // edge case: Date with milliseconds
        date = new Date(1234567890123L); // 2009-02-13T23:31:30.123Z
        expected = "2009-02-13T23:31:30.123Z";
        actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
        
        // edge case: Date with custom timezone
        date = new Date(1234567890L); // 2009-02-13T23:31:30Z
        TimeZone timeZone = TimeZone.getTimeZone("GMT+2");
        expected = "2009-02-14T01:31:30+02:00";
        actual = ISO8601Utils.format(date, false, timeZone);
        assertEquals(expected, actual);
        
        // error scenario: Date is null
        date = null;
        expected = "";
        actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        // typical use case
        String input = "2009-02-13T23:31:30Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1234567890000L);
        Date actual = ISO8601Utils.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());

        // edge case: Date with milliseconds
        input = "2009-02-13T23:31:30.123Z";
        pos = new ParsePosition(0);
        expected = new Date(1234567890123L);
        actual = ISO8601Utils.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());

        // edge case: Date with custom timezone
        input = "2009-02-14T01:31:30+02:00";
        pos = new ParsePosition(0);
        expected = new Date(1234567890000L);
        actual = ISO8601Utils.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
        
        // error scenario: Invalid date format
        input = "abcd";
        pos = new ParsePosition(0);
        expected = null;
        try {
            actual = ISO8601Utils.parse(input, pos);
        } catch (ParseException e) {
            assertEquals(pos.getIndex(), 0);
            assertEquals("Failed to parse date [\"abcd\"]", e.getMessage());
        }
    }

}
