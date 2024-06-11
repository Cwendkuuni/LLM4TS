import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss zzz");
    }

    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        fastDateFormat.format(new Object(), stringBuffer, fieldPosition);
        // Assert the expected value
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testFormat_long() {
        long millis = System.currentTimeMillis();
        String formatted = fastDateFormat.format(millis);
        // Assert the expected value
        assertNotNull(formatted);
    }

    @Test
    public void testFormat_Date() {
        Date date = new Date();
        String formatted = fastDateFormat.format(date);
        // Assert the expected value
        assertNotNull(formatted);
    }

    @Test
    public void testFormat_Calendar() {
        Calendar calendar = Calendar.getInstance();
        String formatted = fastDateFormat.format(calendar);
        // Assert the expected value
        assertNotNull(formatted);
    }

    @Test
    public void testFormat_long_StringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(millis, stringBuffer);
        // Assert the expected value
        assertNotNull(stringBuffer.toString());
    }

    @Test
    public void testFormat_Date_StringBuffer() {
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(date, stringBuffer);
        // Assert the expected value
        assertNotNull(stringBuffer.toString());
    }

    @Test
    public void testFormat_Calendar_StringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(calendar, stringBuffer);
        // Assert the expected value
        assertNotNull(stringBuffer.toString());
    }

    @Test
    public void testParse() throws ParseException {
        String source = "2021-01-01 12:00:00 GMT";
        Date parsed = fastDateFormat.parse(source);
        // Assert the expected value
        assertNotNull(parsed);
    }

    @Test
    public void testParse_String_ParsePosition() {
        String source = "2021-01-01 12:00:00 GMT";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsed = fastDateFormat.parse(source, parsePosition);
        // Assert the expected value
        assertNotNull(parsed);
    }

    @Test
    public void testParseObject_String_ParsePosition() {
        String source = "2021-01-01 12:00:00 GMT";
        ParsePosition parsePosition = new ParsePosition(0);
        Object parsed = fastDateFormat.parseObject(source, parsePosition);
        // Assert the expected value
        assertNotNull(parsed);
    }

    @Test
    public void testGetPattern() {
        String pattern = fastDateFormat.getPattern();
        // Assert the expected value
        assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = fastDateFormat.getTimeZone();
        // Assert the expected value
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        Locale locale = fastDateFormat.getLocale();
        // Assert the expected value
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        // Assert the expected value
        assertTrue(maxLengthEstimate >= 0);
    }

    @Test
    public void testEquals_Object() {
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss zzz");
        boolean equals = fastDateFormat.equals(other);
        // Assert the expected value
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        int hashCode = fastDateFormat.hashCode();
        // Assert the expected value
        assertTrue(hashCode != 0);
    }

    @Test
    public void testToString() {
        String toString = fastDateFormat.toString();
        // Assert the expected value
        assertNotNull(toString);
    }

    @Test
    public void testApplyRules_Calendar_StringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.applyRules(calendar, stringBuffer);
        // Assert the expected value
        assertNotNull(stringBuffer.toString());
    }
}