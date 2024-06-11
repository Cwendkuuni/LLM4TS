import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_String() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_String_TimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_String_Locale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_String_TimeZone_Locale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        int style = 1;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance_Locale() {
        int style = 1;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance_TimeZone() {
        int style = 1;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance_TimeZone_Locale() {
        int style = 1;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        int style = 1;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance_Locale() {
        int style = 1;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance_TimeZone() {
        int style = 1;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance_TimeZone_Locale() {
        int style = 1;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = 1;
        int timeStyle = 1;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance_Locale() {
        int dateStyle = 1;
        int timeStyle = 1;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance_TimeZone() {
        int dateStyle = 1;
        int timeStyle = 1;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance_TimeZone_Locale() {
        int dateStyle = 1;
        int timeStyle = 1;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        Object obj = new Date();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNotNull(result);
    }

    @Test
    public void testFormat_long() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(millis);
        assertNotNull(result);
    }

    @Test
    public void testFormat_Date() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(date);
        assertNotNull(result);
    }

    @Test
    public void testFormat_Calendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
    }

    @Test
    public void testFormat_long_StringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(millis, buf);
        assertNotNull(result);
    }

    @Test
    public void testFormat_Date_StringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(date, buf);
        assertNotNull(result);
    }

    @Test
    public void testFormat_Calendar_StringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(calendar, buf);
        assertNotNull(result);
    }

    @Test
    public void testParse() throws ParseException {
        String source = "2020-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse(source);
        assertNotNull(result);
    }

    @Test
    public void testParse_String_ParsePosition() {
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }

    @Test
    public void testParseObject_String_ParsePosition() {
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Object result = instance.parseObject(source, pos);
        assertNotNull(result);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String pattern = instance.getPattern();
        assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        TimeZone timeZone = instance.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Locale locale = instance.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLength = instance.getMaxLengthEstimate();
        assertNotNull(maxLength);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance = FastDateFormat.getInstance();
        boolean equals = instance.equals(null);
        assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int hashCode = instance.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String toString = instance.toString();
        assertNotNull(toString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.applyRules(calendar, buf);
        assertNotNull(result);
    }
}