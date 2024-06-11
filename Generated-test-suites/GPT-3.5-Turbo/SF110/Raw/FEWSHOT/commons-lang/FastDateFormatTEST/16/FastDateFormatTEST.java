package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals("FastDateFormat[,<Default Locale>,<Default TimeZone>]", instance.toString());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
        assertEquals("FastDateFormat[yyyy-MM-dd,<Default Locale>,<Default TimeZone>]", instance.toString());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
        assertEquals("FastDateFormat[yyyy-MM-dd,<Default Locale>,Europe/London]", instance.toString());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[yyyy-MM-dd,en_US,<Default TimeZone>]", instance.toString());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[yyyy-MM-dd,en_US,Europe/London]", instance.toString());
    }

    @Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy,<Default TimeZone>]", instance.toString());
    }

    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy,en_US,<Default TimeZone>]", instance.toString());
    }

    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy,<Default Locale>,Europe/London]", instance.toString());
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy,en_US,Europe/London]", instance.toString());
    }

    @Test
    public void testGetTimeInstanceWithStyle() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm a,<Default Locale>]", instance.toString());
    }

    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm a,en_US]", instance.toString());
    }

    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm a,<Default Locale>,Europe/London]", instance.toString());
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm a,en_US,Europe/London]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy h:mm a,<Default Locale>]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy h:mm a,en_US]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy h:mm a,<Default Locale>,Europe/London]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy h:mm a,en_US,Europe/London]", instance.toString());
    }

    @Test
    public void testFormatWithObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), toAppendTo, pos);
        assertEquals(toAppendTo, result);
        assertEquals(0, pos.getBeginIndex());
        assertEquals(toAppendTo.length(), pos.getEndIndex());
    }

    @Test
    public void testFormatWithMillis() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = instance.format(System.currentTimeMillis());
        assertNotNull(result);
    }

    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = instance.format(new Date());
        assertNotNull(result);
    }

    @Test
    public void testFormatWithCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = instance.format(Calendar.getInstance());
        assertNotNull(result);
    }

    @Test
    public void testFormatWithMillisAndStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(System.currentTimeMillis(), buf);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatWithDateAndStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(new Date(), buf);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatWithCalendarAndStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(Calendar.getInstance(), buf);
        assertEquals(buf, result);
    }

    @Test
    public void testParseWithString() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Date result = instance.parse("2020-01-01");
        assertNotNull(result);
    }

    @Test
    public void testParseWithStringAndParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse("2020-01-01", pos);
        assertNotNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseObjectWithStringAndParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject("2020-01-01", pos);
        assertNotNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String pattern = instance.getPattern();
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd", pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        TimeZone timeZone = instance.getTimeZone();
        assertNotNull(timeZone);
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Locale locale = instance.getLocale();
        assertNotNull(locale);
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate >= 0);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));

        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(instance1.equals(instance2));

        FastDateFormat instance3 = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("Europe/London"));
        assertFalse(instance1.equals(instance3));

        FastDateFormat instance4 = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertFalse(instance1.equals(instance4));
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(instance.hashCode(), instance.hashCode());
    }
}