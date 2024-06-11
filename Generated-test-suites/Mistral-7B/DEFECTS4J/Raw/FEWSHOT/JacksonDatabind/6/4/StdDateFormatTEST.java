package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testGetBlueprintISO8601Format() {
        // This method is deprecated, but just to make sure it returns the same instance as getISO8601Format(null, null)
        DateFormat format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals(DATE_FORMAT_ISO8601, format);
    }

    @Test(expected = Deprecated.class)
    public void testGetBlueprintISO8601FormatDeprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat format = StdDateFormat.getISO8601Format(null, null);
        assertEquals(DATE_FORMAT_ISO8601, format);
    }

    @Test
    public void testGetISO8601FormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat format = StdDateFormat.getISO8601Format(timeZone, null);
        assertEquals(DATE_FORMAT_ISO8601, format);
        assertSame(timeZone, format.getTimeZone());
    }

    @Test
    public void testGetISO8601FormatWithLocale() {
        Locale locale = new Locale("en", "US");
        DateFormat format = StdDateFormat.getISO8601Format(null, locale);
        assertEquals(DATE_FORMAT_ISO8601, format);
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testGetISO8601FormatWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = new Locale("en", "US");
        DateFormat format = StdDateFormat.getISO8601Format(timeZone, locale);
        assertEquals(DATE_FORMAT_ISO8601, format);
        assertSame(timeZone, format.getTimeZone());
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat format = StdDateFormat.getRFC1123Format(null, null);
        assertEquals(DATE_FORMAT_RFC1123, format);
    }

    @Test
    public void testGetRFC1123FormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat format = StdDateFormat.getRFC1123Format(timeZone, null);
        assertEquals(DATE_FORMAT_RFC1123, format);
        assertSame(timeZone, format.getTimeZone());
    }

    @Test
    public void testGetRFC1123FormatWithLocale() {
        Locale locale = new Locale("en", "US");
        DateFormat format = StdDateFormat.getRFC1123Format(null, locale);
        assertEquals(DATE_FORMAT_RFC1123, format);
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testGetRFC1123FormatWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = new Locale("en", "US");
        DateFormat format = StdDateFormat.getRFC1123Format(timeZone, locale);
        assertEquals(DATE_FORMAT_RFC1123, format);
        assertSame(timeZone, format.getTimeZone());
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testInstance() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertTrue(dateFormat instanceof StdDateFormat);
        assertNull(dateFormat._timezone);
        assertNull(dateFormat._formatRFC1123);
        assertNull(dateFormat._formatISO8601);
        assertNull(dateFormat._formatISO8601_z);
        assertNull(dateFormat._formatPlain);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        StdDateFormat dateFormat = new StdDateFormat(timeZone, null);
        assertNotNull(dateFormat._timezone);
        assertSame(timeZone, dateFormat._timezone);
        assertNull(dateFormat._formatRFC1123);
        assertNull(dateFormat._formatISO8601);
        assertNull(dateFormat._formatISO8601_z);
        assertNull(dateFormat._formatPlain);
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale("en", "US");
        StdDateFormat dateFormat = new StdDateFormat(null, locale);
        assertNull(dateFormat._timezone);
        assertNull(dateFormat._formatRFC1123);
        assertNull(dateFormat._formatISO8601);
        assertNull(dateFormat._formatISO8601_z);
        assertEquals(locale, dateFormat._locale);
        assertNull(dateFormat._formatPlain);
    }

    @Test
    public void testWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = new Locale("en", "US");
        StdDateFormat dateFormat = new StdDateFormat(timeZone, locale);
        assertNotNull(dateFormat._timezone);
        assertSame(timeZone, dateFormat._timezone);
        assertNull(dateFormat._formatRFC1123);
        assertNull(dateFormat._formatISO8601);
        assertNull(dateFormat._formatISO8601_z);
        assertEquals(locale, dateFormat._locale);
        assertNull(dateFormat._formatPlain);
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = new StdDateFormat(null, null);
        StdDateFormat cloned = dateFormat.clone();
        assertNotNull(cloned);
        assertNull(cloned._timezone);
        assertNull(cloned._formatRFC1123);
        assertNull(cloned._formatISO8601);
        assertNull(cloned._formatISO8601_z);
        assertNull(cloned._formatPlain);
        assertEquals(dateFormat, cloned);
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(null, null);

        Date date = dateFormat.parse("2023-03-17T14:15:16.123Z");
        assertEquals(new Date(1678801256000L), date);

        date = dateFormat.parse("2023-03-17T14:15:16.123+00:00");
        assertEquals(new Date(1678801256000L), date);

        date = dateFormat.parse("2023-03-17T14:15:16.123-05:00");
        assertEquals(new Date(1678800656000L), date);

        date = dateFormat.parse("2023-03-17T14:15:16");
        assertEquals(new Date(1678801256000L), date);

        date = dateFormat.parse("Sat Mar 17 14:15:16 EST 2023");
        assertEquals(new Date(1678800656000L), date);

        try {
            dateFormat.parse("foo");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat(null, null);

        String dateString = dateFormat.format(new Date(1678801256000L));
        assertEquals("2023-03-17T14:15:16.123Z", dateString);

        dateString = dateFormat.format(new Date(1678800656000L));
        assertEquals("2023-03-17T14:15:16.123-05:00", dateString);

        dateString = dateFormat.format(new Date(1678801256000L), new StringBuffer(), new FieldPosition());
        assertEquals("2023-03-17T14:15:16.123Z", dateString.toString());

        dateString = dateFormat.format(new Date(1678800656000L), new StringBuffer(), new FieldPosition());
        assertEquals("2023-03-17T14:15:16.123-05:00", dateString.toString());
    }

    @Test
    public void testToString() {
        StdDateFormat dateFormat = new StdDateFormat(null, null);
        String formatString = "DateFormat " + dateFormat.getClass().getName() + " (timezone: null) (locale: java.util.Locale[en, US])";
        assertEquals(formatString, dateFormat.toString());
    }

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat(null, null);

        assertTrue(dateFormat.looksLikeISO8601("2023-03-17T14:15:16.123Z"));
        assertTrue(dateFormat.looksLikeISO8601("2023-03-17T14:15:16.123+00:00"));
        assertTrue(dateFormat.looksLikeISO8601("2023-03-17T14:15:16.123-05:00"));
        assertTrue(dateFormat.looksLikeISO8601("2023-03-17T14:15:16"));
        assertTrue(dateFormat.looksLikeISO8601("Sat Mar 17 14:15:16 EST 2023"));

        assertFalse(dateFormat.looksLikeISO8601("foo"));
        assertFalse(dateFormat.looksLikeISO8601("123"));
        assertFalse(dateFormat.looksLikeISO8601("1234"));
    }
}