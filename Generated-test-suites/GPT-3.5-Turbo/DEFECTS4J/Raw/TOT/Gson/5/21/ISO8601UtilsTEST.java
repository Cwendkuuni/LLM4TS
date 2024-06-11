import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
    @Test
    public void testFormat() {
        Date date = new Date();
        String actual = ISO8601Utils.format(date);
        String expected = sdf.format(date);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String actual = ISO8601Utils.format(date, true);
        String expected = sdf.format(date);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+05:30");
        sdf.setTimeZone(timeZone);
        String actual = ISO8601Utils.format(date, timeZone);
        String expected = sdf.format(date);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithMillisAndTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+05:30");
        sdf.setTimeZone(timeZone);
        String actual = ISO8601Utils.format(date, true, timeZone);
        String expected = sdf.format(date);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse() throws ParseException {
        String dateStr = "2022-01-01T00:00:00.000Z";
        Date actual = ISO8601Utils.parse(dateStr);
        Date expected = sdf.parse(dateStr);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithError() {
        String dateStr = "2022-01-01T00:00:00.000";
        try {
            ISO8601Utils.parse(dateStr);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testParseWithPosition() throws ParseException {
        String dateStr = "2022-01-01T00:00:00.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
        Date expected = sdf.parse(dateStr);
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
    }
    
    @Test
    public void testParseWithPositionWithError() {
        String dateStr = "2022-01-01T00:00:00.000";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStr, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(true);
            assertEquals(0, pos.getIndex());
        }
    }
}