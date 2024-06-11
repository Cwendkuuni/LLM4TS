import org.junit.*;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_noMillis_noOffset() {
        // Arrange
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2022-01-01T12:30:45Z";
        
        // Act
        String result = ISO8601Utils.format(date);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_millis_noOffset() {
        // Arrange
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2022-01-01T12:30:45.000Z";
        
        // Act
        String result = ISO8601Utils.format(date, true);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_noMillis_withOffset() {
        // Arrange
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2022-01-01T12:30:45+00:00";
        
        // Act
        String result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_millis_withOffset() {
        // Arrange
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2022-01-01T12:30:45.000+00:00";
        
        // Act
        String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testParse_validDateString_noOffset() throws ParseException {
        // Arrange
        String dateString = "2022-01-01T12:30:45Z";
        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        
        // Act
        Date result = ISO8601Utils.parse(dateString, new ParsePosition(0));
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testParse_validDateString_withOffset() throws ParseException {
        // Arrange
        String dateString = "2022-01-01T12:30:45+00:00";
        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        
        // Act
        Date result = ISO8601Utils.parse(dateString, new ParsePosition(0));
        
        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidDateString() throws ParseException {
        // Arrange
        String dateString = "2022-01-01 12:30:45Z";
        
        // Act
        ISO8601Utils.parse(dateString, new ParsePosition(0));
        
        // Assert
        // ParseException is expected
    }

}