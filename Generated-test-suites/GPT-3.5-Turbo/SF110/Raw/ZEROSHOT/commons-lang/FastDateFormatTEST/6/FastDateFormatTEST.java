package org.apache.commons.lang3.time;

import org.junit.*;
import static org.junit.Assert.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance();
    }

    @After
    public void tearDown() {
        fastDateFormat = null;
    }

    @Test
    public void testGetInstance() {
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "dd/MM/yyyy";
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
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
    public void testGetDateInstanceWithStyleAndLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithStyleAndTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
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
    public void testGetTimeInstanceWithStyleAndLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithStyleAndTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.LONG;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithStyleAndLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.LONG;
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithStyleAndTimeZone() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.LONG;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithStyleAndTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.LONG;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testFormatObject() {
        Object obj = new Date();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = fastDateFormat.format(obj, buffer, pos);
        assertNotNull(result);
        assertEquals(buffer.toString(), result.toString());
    }

    @Test
    public void testFormatLong() {
        long millis = System.currentTimeMillis();
        String result = fastDateFormat.format(millis);
        assertNotNull(result);
    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        String result = fastDateFormat.format(date);
        assertNotNull(result);
    }

    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        String result = fastDateFormat.format(calendar);
        assertNotNull(result);
    }

    @Test
    public void testFormatLongWithStringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(millis, buffer);
        assertNotNull(result);
        assertEquals(buffer.toString(), result.toString());
    }

    @Test
    public void testFormatDateWithStringBuffer() {
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(date, buffer);
        assertNotNull(result);
        assertEquals(buffer.toString(), result.toString());
    }

    @Test
    public void testFormatCalendarWithStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(calendar, buffer);
        assertNotNull(result);
        assertEquals(buffer.toString(), result.toString());
    }

    @Test
    public void testParse() throws ParseException {
        String source = "01/01/2022";
        Date result = fastDateFormat.parse(source);
        assertNotNull(result);
    }

    @Test
    public void testParseWithParsePosition() {
        String source = "01/01/2022";
        ParsePosition pos = new ParsePosition(0);
        Date result = fastDateFormat.parse(source, pos);
        assertNotNull(result);
    }

    @Test
    public void testParseObject() {
        String source = "01/01/2022";
        ParsePosition pos = new ParsePosition(0);
        Object result = fastDateFormat.parseObject(source, pos);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testGetPattern() {
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        Locale locale = fastDateFormat.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int maxLength = fastDateFormat.getMaxLengthEstimate();
        assertTrue(maxLength > 0);
    }

    @Test
    public void testEquals() {
        FastDateFormat other = FastDateFormat.getInstance();
        assertTrue(fastDateFormat.equals(other));
    }

    @Test
    public void testHashCode() {
        FastDateFormat other = FastDateFormat.getInstance();
        assertEquals(fastDateFormat.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String result = fastDateFormat.toString();
        assertNotNull(result);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1);
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.applyRules(calendar, buffer);
        assertNotNull(result);
        assertEquals(buffer.toString(), result.toString());
    }
}