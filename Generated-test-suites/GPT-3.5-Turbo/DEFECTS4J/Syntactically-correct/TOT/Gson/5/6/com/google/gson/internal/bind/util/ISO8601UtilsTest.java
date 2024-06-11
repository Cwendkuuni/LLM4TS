import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ISO8601UtilsTest {

    @Test
    public void format_DefaultTimeZoneNoMillis_Success() {
        // Arrange
        Date date = new Date(2019, 4, 1, 10, 30, 45);
        String expected = "2019-05-01T10:30:45Z";

        // Act
        String result = ISO8601Utils.format(date);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void format_GmtTimeZoneWithMillis_Success() {
        // Arrange
        Date date = new Date(2019, 4, 1, 10, 30, 45);
        String expected = "2019-05-01T10:30:45.000Z";

        // Act
        String result = ISO8601Utils.format(date, true);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void format_CustomTimeZoneNoMillis_Success() {
        // Arrange
        Date date = new Date(2019, 4, 1, 10, 30, 45);
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        String expected = "2019-05-01T10:30:45-08:00";

        // Act
        String result = ISO8601Utils.format(date, false, timeZone);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void parse_ValidISO8601String_Success() throws ParseException {
        // Arrange
        String input = "2019-05-01T10:30:45Z";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date expected = dateFormat.parse(input);
        ParsePosition pos = new ParsePosition(0);

        // Act
        Date result = ISO8601Utils.parse(input, pos);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void parse_InvalidISO8601String_ThrowsParseException() throws ParseException {
        // Arrange
        String input = "20190501T103045Z";
        ParsePosition pos = new ParsePosition(0);
        
        // Act
        ISO8601Utils.parse(input, pos);
    }

    @Test
    public void parse_InvalidFormatWithOffset_Success() throws ParseException {
        // Arrange
        String input = "2019-05-01T10:30:45-08:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date expected = dateFormat.parse(input);
        ParsePosition pos = new ParsePosition(0);

        // Act
        Date result = ISO8601Utils.parse(input, pos);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(input.length(), pos.getIndex());
    }

}