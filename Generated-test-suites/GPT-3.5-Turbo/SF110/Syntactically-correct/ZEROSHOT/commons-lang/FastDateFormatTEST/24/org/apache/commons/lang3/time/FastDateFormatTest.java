import static org.junit.Assert.*;
import java.util.*;
import java.text.*;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        Object obj = new Date();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(obj, toAppendTo, pos);
        // Add your assertions here
    }

    @Test
    public void testFormatLong() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(millis);
        // Add your assertions here
    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(date);
        // Add your assertions here
    }

    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(calendar);
        // Add your assertions here
    }

    @Test
    public void testFormatLongStringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(millis, buf);
        // Add your assertions here
    }

    @Test
    public void testFormatDateStringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(date, buf);
        // Add your assertions here
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.format(calendar, buf);
        // Add your assertions here
    }

    @Test
    public void testParseString() throws ParseException {
        String source = "2021-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.parse(source);
        // Add your assertions here
    }

    @Test
    public void testParseStringParsePosition() {
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.parse(source, pos);
        // Add your assertions here
    }

    @Test
    public void testParseObjectStringParsePosition() {
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.parseObject(source, pos);
        // Add your assertions here
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.getPattern();
        // Add your assertions here
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.getTimeZone();
        // Add your assertions here
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.getLocale();
        // Add your assertions here
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.getMaxLengthEstimate();
        // Add your assertions here
    }

    @Test
    public void testEquals() {
        Object obj = new FastDateFormat();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.equals(obj);
        // Add your assertions here
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.hashCode();
        // Add your assertions here
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.toString();
        // Add your assertions here
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        instance.applyRules(calendar, buf);
        // Add your assertions here
    }

}