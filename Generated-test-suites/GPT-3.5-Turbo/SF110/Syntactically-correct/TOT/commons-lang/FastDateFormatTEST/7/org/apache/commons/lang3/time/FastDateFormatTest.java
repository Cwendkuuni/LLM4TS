import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class FastDateFormatTest {

    @Test
    public void format_ObjectToString_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Object obj = new Object();
        String expected = obj.toString();
        assertEquals(expected, fastDateFormat.format(obj));
    }

    @Test
    public void format_LongToString_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        String expected = Long.toString(millis);
        assertEquals(expected, fastDateFormat.format(millis));
    }

    @Test
    public void format_DateToString_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Date date = new Date();
        String expected = date.toString();
        assertEquals(expected, fastDateFormat.format(date));
    }

    @Test
    public void format_CalendarToString_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String expected = calendar.toString();
        assertEquals(expected, fastDateFormat.format(calendar));
    }

    @Test
    public void format_LongToString_WithFieldPosition_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        fastDateFormat.format(millis, buf);
        String expected = Long.toString(millis);
        assertEquals(expected, buf.toString());
    }

    @Test
    public void format_DateToString_WithStringBuffer_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        fastDateFormat.format(date, buf);
        String expected = date.toString();
        assertEquals(expected, buf.toString());
    }

    @Test
    public void format_CalendarToString_WithStringBuffer_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        fastDateFormat.format(calendar, buf);
        String expected = calendar.toString();
        assertEquals(expected, buf.toString());
    }

    @Test
    public void parse_StringToDate_Success() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String source = "2020-01-01";
        Date expected = fastDateFormat.parse(source);
        assertNotNull(expected);
    }

    @Test
    public void parse_StringToDate_WithParsePosition_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date expected = fastDateFormat.parse(source, pos);
        assertNotNull(expected);
        assertEquals(pos.getErrorIndex(), -1);
    }

    @Test
    public void parseObject_StringToDate_WithParsePosition_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        Object expected = fastDateFormat.parseObject(source, pos);
        assertNotNull(expected);
        assertEquals(pos.getErrorIndex(), -1);
    }

    @Test
    public void getPattern_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String expected = "yyyy-MM-dd";
        assertEquals(expected, fastDateFormat.getPattern());
    }

    @Test
    public void getTimeZone_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        TimeZone expected = TimeZone.getDefault();
        assertEquals(expected, fastDateFormat.getTimeZone());
    }

    @Test
    public void getLocale_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Locale expected = Locale.getDefault();
        assertEquals(expected, fastDateFormat.getLocale());
    }

    @Test
    public void getMaxLengthEstimate_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        int expected = 32;
        assertEquals(expected, fastDateFormat.getMaxLengthEstimate());
    }

    @Test
    public void equals_NullObject_False() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Object obj = null;
        assertFalse(fastDateFormat.equals(obj));
    }

    @Test
    public void equals_DifferentClass_False() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Object obj = new Object();
        assertFalse(fastDateFormat.equals(obj));
    }

    @Test
    public void equals_SameInstance_True() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertTrue(fastDateFormat.equals(fastDateFormat));
    }

    @Test
    public void hashCode_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat.hashCode());
    }

    @Test
    public void toString_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String expected = "FastDateFormat[yyyy-MM-dd," + fastDateFormat.getLocale() + "," + fastDateFormat.getTimeZone().getID() + "]";
        assertEquals(expected, fastDateFormat.toString());
    }

    @Test
    public void applyRules_CalendarWithStringBuffer_Success() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.applyRules(calendar, buf);
        assertNotNull(result);
        assertEquals(buf, result);
    }
}