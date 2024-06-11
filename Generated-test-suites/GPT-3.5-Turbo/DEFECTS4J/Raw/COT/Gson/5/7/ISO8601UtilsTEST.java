
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601UtilsTest {

    @Test
    public void testFormatWithoutMillis() {
        // Arrange
        String expected = "2021-01-01T12:00:00Z";
        Date date = new Date(1609502400000L); // 2021-01-01 12:00:00
        // Act
        String actual = ISO8601Utils.format(date);
        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithMillis() {
        // Arrange
        String expected = "2021-01-01T12:00:00.500Z";
        Date date = new Date(1609502400500L); // 2021-01-01 12:00:00.500
        // Act
        String actual = ISO8601Utils.format(date, true);
        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithTimeZone() {
        // Arrange
        String expected = "2021-01-01T12:00:00+05:30";
        Date date = new Date(1609502400000L); // 2021-01-01 12:00:00
        TimeZone timeZone = TimeZone.getTimeZone("GMT+5:30");
        // Act
        String actual = ISO8601Utils.format(date, false, timeZone);
        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseValidDateString() {
        // Arrange
        String dateString = "2021-01-01T12:00:00.500Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1609502400500L); // 2021-01-01 12:00:00.500
        // Act
        Date actual = null;
        try {
            actual = ISO8601Utils.parse(dateString, pos);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
        // Assert
        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }
    
    @Test(expected = ParseException.class)
    public void testParseInvalidDateString() throws ParseException {
        // Arrange
        String dateString = "2021/01/01";
        ParsePosition pos = new ParsePosition(0);
        // Act
        ISO8601Utils.parse(dateString, pos);
        // Assert
        // Expecting a ParseException
    }
    
    @Test
    public void testParseDateStringWithTimeZone() {
        // Arrange
        String dateString = "2021-01-01T12:00:00+05:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1609502400000L); // 2021-01-01 12:00:00
        TimeZone timeZone = TimeZone.getTimeZone("GMT+5:30");
        // Act
        Date actual = null;
        try {
            actual = ISO8601Utils.parse(dateString, pos);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
        // Assert
        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }
    
}
