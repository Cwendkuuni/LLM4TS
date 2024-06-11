import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void format_shouldReturnCorrectDateFormat_withDefaultTimezoneAndNoMillis() {
        Date date = new Date(1554102000000L); // April 1, 2019 12:00:00 AM GMT
        String expected = "2019-04-01T00:00:00Z";
        
        String actual = ISO8601Utils.format(date);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void format_shouldReturnCorrectDateFormat_withGMTTimezoneAndMillis() {
        TimeZone gmtTimezone = TimeZone.getTimeZone("GMT");
        Date date = new Date(1554102000123L); // April 1, 2019 12:00:00.123 AM GMT
        String expected = "2019-04-01T00:00:00.123Z";
        
        String actual = ISO8601Utils.format(date, true, gmtTimezone);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void format_shouldReturnCorrectDateFormat_withGMTTimezoneAndNoMillis() {
        TimeZone gmtTimezone = TimeZone.getTimeZone("GMT");
        Date date = new Date(1554102000000L); // April 1, 2019 12:00:00 AM GMT
        String expected = "2019-04-01T00:00:00Z";
        
        String actual = ISO8601Utils.format(date, false, gmtTimezone);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void parse_shouldReturnCorrectDate() throws ParseException {
        String dateString = "2019-04-01T00:00:00Z";
        ParsePosition position = new ParsePosition(0);
        Date expected = new Date(1554102000000L); // April 1, 2019 12:00:00 AM GMT
        
        Date actual = ISO8601Utils.parse(dateString, position);
        
        assertEquals(expected, actual);
        assertEquals(dateString.length(), position.getIndex());
    }
    
    @Test(expected = ParseException.class)
    public void parse_shouldThrowParseException_whenInvalidDateStringProvided() throws ParseException {
        String dateString = "2019-04-01T00:00:00";
        ParsePosition position = new ParsePosition(0);
        
        ISO8601Utils.parse(dateString, position);
    }

}