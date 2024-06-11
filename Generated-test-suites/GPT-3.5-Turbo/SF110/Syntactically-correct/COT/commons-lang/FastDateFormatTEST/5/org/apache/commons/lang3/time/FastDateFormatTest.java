import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        // Test case to check if getInstance() method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        // Test case to check if getInstance(String pattern) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        // Test case to check if getInstance(String pattern, TimeZone timeZone) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        // Test case to check if getInstance(String pattern, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        // Test case to check if getInstance(String pattern, TimeZone timeZone, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("PST"), Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        // Test case to check if getDateInstance(int style) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        // Test case to check if getDateInstance(int style, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        // Test case to check if getDateInstance(int style, TimeZone timeZone) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        // Test case to check if getDateInstance(int style, TimeZone timeZone, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("PST"), Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        // Test case to check if getTimeInstance(int style) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        // Test case to check if getTimeInstance(int style, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        // Test case to check if getTimeInstance(int style, TimeZone timeZone) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        // Test case to check if getTimeInstance(int style, TimeZone timeZone, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("PST"), Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        // Test case to check if getDateTimeInstance(int dateStyle, int timeStyle) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        // Test case to check if getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        // Test case to check if getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        // Test case to check if getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) method returns an instance of FastDateFormat
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getTimeZone("PST"), Locale.US);
        assertNotNull(instance);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        // Test case to check if format(Object obj, StringBuffer toAppendTo, FieldPosition pos) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), buffer, position);
        assertNotNull(result);
        assertEquals(buffer, result);
    }

    @Test
    public void testFormatLong() {
        // Test case to check if format(long millis) method returns a String
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = 1577836800000L;
        String result = instance.format(millis);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testFormatDate() {
        // Test case to check if format(Date date) method returns a String
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        String result = instance.format(date);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testFormatCalendar() {
        // Test case to check if format(Calendar calendar) method returns a String
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testFormatLongStringBuffer() {
        // Test case to check if format(long millis, StringBuffer buf) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = 1577836800000L;
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(millis, buffer);
        assertNotNull(result);
        assertEquals(buffer, result);
    }

    @Test
    public void testFormatDateStringBuffer() {
        // Test case to check if format(Date date, StringBuffer buf) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(date, buffer);
        assertNotNull(result);
        assertEquals(buffer, result);
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        // Test case to check if format(Calendar calendar, StringBuffer buf) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(calendar, buffer);
        assertNotNull(result);
        assertEquals(buffer, result);
    }

    @Test
    public void testParseString() throws ParseException {
        // Test case to check if parse(String source) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2020-01-01";
        Date result = instance.parse(source);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testParseStringParsePosition() {
        // Test case to check if parse(String source, ParsePosition pos) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition position = new ParsePosition(0);
        Date result = instance.parse(source, position);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testParseObject() {
        // Test case to check if parseObject(String source, ParsePosition pos) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition position = new ParsePosition(0);
        Object result = instance.parseObject(source, position);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetPattern() {
        // Test case to check if getPattern() method returns the correct pattern
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String pattern = instance.getPattern();
        assertEquals("yyyy-MM-dd", pattern);
    }

    @Test
    public void testGetTimeZone() {
        // Test case to check if getTimeZone() method returns the correct TimeZone
        FastDateFormat instance = FastDateFormat.getInstance();
        TimeZone timeZone = instance.getTimeZone();
        assertNotNull(timeZone);
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetLocale() {
        // Test case to check if getLocale() method returns the correct Locale
        FastDateFormat instance = FastDateFormat.getInstance();
        Locale locale = instance.getLocale();
        assertNotNull(locale);
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        // Test case to check if getMaxLengthEstimate() method returns a valid maximum length estimate
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate >= 0);
    }

    @Test
    public void testEquals() {
        // Test case to check if equals(Object obj) method returns true for the same instance
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = instance1;
        boolean result = instance1.equals(instance2);
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        // Test case to check if hashCode() method returns the same value for equal instances
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        // Test case to check if toString() method returns the correct string representation of the instance
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.toString();
        assertNotNull(result);
        assertTrue(result.contains("FastDateFormat"));
    }

    @Test
    public void testApplyRules() {
        // Test case to check if applyRules(Calendar calendar, StringBuffer buf) method works correctly
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.applyRules(calendar, buffer);
        assertNotNull(result);
        assertEquals(buffer, result);
    }
}