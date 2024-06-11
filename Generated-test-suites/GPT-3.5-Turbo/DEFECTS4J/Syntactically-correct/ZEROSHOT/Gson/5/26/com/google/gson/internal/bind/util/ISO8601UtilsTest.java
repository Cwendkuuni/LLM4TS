import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_DefaultTimeZoneNoMillis() {
        // Arrange
        Date date = new Date();
        String expected = "yyyy-MM-ddThh:mm:ssZ";

        // Act
        String result = ISO8601Utils.format(date);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_DefaultTimeZoneWithMillis() {
        // Arrange
        Date date = new Date();
        String expected = "yyyy-MM-ddThh:mm:ss.sssZ";

        // Act
        String result = ISO8601Utils.format(date, true);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_GMTTimeZoneNoMillis() {
        // Arrange
        Date date = new Date();
        String expected = "yyyy-MM-ddThh:mm:ssZ";

        // Act
        String result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_GMTTimeZoneWithMillis() {
        // Arrange
        Date date = new Date();
        String expected = "yyyy-MM-ddThh:mm:ss.sssZ";

        // Act
        String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_CustomTimeZoneNoMillis() {
        // Arrange
        Date date = new Date();
        String expected = "yyyy-MM-ddThh:mm:ssZ";

        // Act
        String result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/Paris"));

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_CustomTimeZoneWithMillis() {
        // Arrange
        Date date = new Date();
        String expected = "yyyy-MM-ddThh:mm:ss.sssZ";

        // Act
        String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/Paris"));

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testParse_ValidDateString() throws ParseException {
        // Arrange
        String date = "2019-06-25T18:30:00Z";
        Date expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
        ParsePosition pos = new ParsePosition(0);

        // Act
        Date result = ISO8601Utils.parse(date, pos);

        // Assert
        assertEquals(expected, result);
        assertEquals(date.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDateString() throws ParseException {
        // Arrange
        String date = "2019-06-25T18:30:00";
        ParsePosition pos = new ParsePosition(0);

        // Act
        ISO8601Utils.parse(date, pos);
    }

    @Test
    public void testPadInt_NumberLessThanLength() {
        // Arrange
        StringBuilder buffer = new StringBuilder();
        int value = 1;
        int length = 2;
        String expected = "01";

        // Act
        ISO8601Utils.padInt(buffer, value, length);
        String result = buffer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testPadInt_NumberGreaterThanLength() {
        // Arrange
        StringBuilder buffer = new StringBuilder();
        int value = 123;
        int length = 2;
        String expected = "123";

        // Act
        ISO8601Utils.padInt(buffer, value, length);
        String result = buffer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testIndexOfNonDigit_IndexOfFirstNonDigit() {
        // Arrange
        String string = "12abc34";
        int offset = 0;
        int expected = 2;

        // Act
        int result = ISO8601Utils.indexOfNonDigit(string, offset);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testIndexOfNonDigit_IndexOfFirstNonDigit_NoNonDigits() {
        // Arrange
        String string = "1234";
        int offset = 0;
        int expected = string.length();

        // Act
        int result = ISO8601Utils.indexOfNonDigit(string, offset);

        // Assert
        assertEquals(expected, result);
    }
}