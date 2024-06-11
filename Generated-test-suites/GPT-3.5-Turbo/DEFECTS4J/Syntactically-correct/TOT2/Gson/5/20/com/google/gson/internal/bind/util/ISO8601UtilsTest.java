import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {
    
    @Test
    public void testFormat() {
        Date date = new Date(1622352600000L); // June 6, 2021 12:30:00 GMT
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-05-30T12:30:00Z", formattedDate);
    }
    
    @Test
    public void testFormatWithMillis() {
        Date date = new Date(1622352600000L); // June 6, 2021 12:30:00 GMT
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("2021-05-30T12:30:00.000Z", formattedDate);
    }
    
    @Test
    public void testParse() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse("2021-05-30T12:30:00Z", pos);
        assertNotNull(parsedDate);
    }

    @Test
    public void testParseInvalidDate() {
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse("invalid-date", pos);
        });
    }
    
    @Test
    public void testParseWithOffset() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse("2021-05-30T12:30:00+02:00", pos);
        assertNotNull(parsedDate);
    }
}