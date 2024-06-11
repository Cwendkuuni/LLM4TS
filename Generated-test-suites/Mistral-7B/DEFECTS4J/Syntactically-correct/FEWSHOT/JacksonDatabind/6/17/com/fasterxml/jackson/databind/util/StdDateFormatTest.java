package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testGetInstance() {
        StdDateFormat instance = StdDateFormat.instance;
        assertEquals(Locale.US, instance.getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), instance.getTimeZone());
        StdDateFormat clone = instance.clone();
        assertEquals(instance, clone);

        // Test with custom locale and timezone
        StdDateFormat custom = new StdDateFormat(Locale.JAPAN, ZoneId.of("Asia/Tokyo"));
        assertNotSame(instance, custom);
        assertEquals(Locale.JAPAN, custom.getLocale());
        assertEquals(ZoneId.of("Asia/Tokyo"), custom.getTimeZone().toZoneId());
    }

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test with ISO 8601 without timezone
        Date date1 = dateFormat.parse("2023-03-15");
        assertEquals("Wed Mar 15 00:00:00 GMT 2023", dateFormat.format(date1));

        // Test with ISO 8601 with timezone
        Date date2 = dateFormat.parse("2023-03-15T12:34:56.789Z");
        assertEquals("Wed Mar 15 12:34:56.789 GMT 2023", dateFormat.format(date2));

        // Test with ISO 8601 with timezone offset
        Date date3 = dateFormat.parse("2023-03-15T12:34:56.789+01:00");
        assertEquals("Thu Mar 16 13:34:56.789 GMT 2023", dateFormat.format(date3));

        // Test with ISO 8601 with missing seconds and milliseconds
        Date date4 = dateFormat.parse("2023-03-15T12:34");
        assertEquals("Wed Mar 15 12:34:00 GMT 2023", dateFormat.format(date4));

        // Test with ISO 8601 with missing seconds
        Date date5 = dateFormat.parse("2023-03-15T12:34:");
        assertEquals("Wed Mar 15 12:34:00 GMT 2023", dateFormat.format(date5));

        // Test with ISO 8601 with missing milliseconds
        Date date6 = dateFormat.parse("2023-03-15T12:34:56.");
        assertEquals("Wed Mar 15 12:34:56.0 GMT 2023", dateFormat.format(date6));

        // Test with invalid ISO 8601
        assertThrows(ParseException.class, () -> dateFormat.parse("2023-3-15"));
        assertThrows(ParseException.class, () -> dateFormat.parse("2023-03-15T12:34:56.789++01:00"));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test with RFC 1123
        Date date1 = dateFormat.parse("Wed, 15 Mar 2023 12:34:56 GMT");
        assertEquals("Wed Mar 15 12:34:56 GMT 2023", dateFormat.format(date1));

        // Test with RFC 1123 with timezone offset
        Date date2 = dateFormat.parse("Wed, 15 Mar 2023 12:34:56 +0100");
        assertEquals("Thu Mar 16 13:34:56 GMT 2023", dateFormat.format(date2));

        // Test with RFC 1123 with missing seconds and milliseconds
        Date date3 = dateFormat.parse("Wed, 15 Mar 2023 12:34 ");
        assertEquals("Wed Mar 15 12:34:00 GMT 2023", dateFormat.format(date3));

        // Test with RFC 1123 with missing milliseconds
        Date date4 = dateFormat.parse("Wed, 15 Mar 2023 12:34:56.");
        assertEquals("Wed Mar 15 12:34:56.0 GMT 2023", dateFormat.format(date4));

        // Test with RFC 1123 with invalid timezone offset
        assertThrows(ParseException.class, () -> dateFormat.parse("Wed, 15 Mar 2023 12:34:56 +01:60"));
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test with date only
        Date date1 = new Date(1678112000000L);
        assertEquals("Tue Feb 21 00:00:00 GMT 2023", dateFormat.format(date1));

        // Test with date and time
        Date date2 = new Date(1678112000567L);
        assertEquals("Tue Feb 21 12:34:56 GMT 2023", dateFormat.format(date2));

        // Test with date, time, and timezone offset
        Date date3 = new Date(1678112123567L);
        assertEquals("Wed Feb 22 13:34:56 GMT 2023", dateFormat.format(date3));
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test with null timezone
        dateFormat.setTimeZone(null);
        assertNull(dateFormat.getTimeZone());

        // Test with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");
        dateFormat.setTimeZone(customTimeZone);
        assertEquals(customTimeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testSetLocale() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test with null locale
        dateFormat.setLocale(null);
        assertEquals(Locale.US, dateFormat.getLocale());

        // Test with custom locale
        Locale customLocale = Locale.JAPAN;
        dateFormat.setLocale(customLocale);
        assertEquals(customLocale, dateFormat.getLocale());
    }

    @Test
    public void testToString() {
        StdDateFormat dateFormat1 = new StdDateFormat();
        StdDateFormat dateFormat2 = new StdDateFormat(Locale.JAPAN, TimeZone.getTimeZone("Asia/Tokyo"));

        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: null, locale: java.util.Locale[ja, JP])",
                dateFormat1.toString());
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: java.util.TimeZone[id=Asia/Tokyo,offset=3600000,dstSavings=0,useDaylight=false], locale: java.util.Locale[ja, JP])",
                dateFormat2.toString());
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        assertThrows(DeprecationException.class, () -> StdDateFormat.getBlueprintISO8601Format());
    }

    @Test
    public void testGetISO8601Format() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        DateFormat iso8601Format = dateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("GMT"));
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: GMT, locale: US)", iso8601Format.toString());
        assertEquals("2023-03-15", iso8601Format.format(new Date(1678112000000L)));
        assertEquals("2023-03-15T12:34:56.789Z", iso8601Format.format(new Date(1678112056789L)));
    }

    @Test
    public void testGetRFC1123Format() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        DateFormat rfc1123Format = dateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("GMT"));
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: GMT, locale: US)", rfc1123Format.toString());
        assertEquals("Wed, 15 Mar 2023 12:34:56 GMT", rfc1123Format.format(new Date(1678112056789L)));
    }
}