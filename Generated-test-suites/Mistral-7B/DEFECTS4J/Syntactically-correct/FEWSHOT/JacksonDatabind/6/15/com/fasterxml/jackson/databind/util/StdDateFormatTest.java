package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testGetInstance() {
        StdDateFormat instance = StdDateFormat.instance;
        assertNotNull(instance);
        assertEquals(Locale.US, instance.getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), instance.getTimeZone());
    }

    @Test
    public void testGetBlueprintFormat() {
        DateFormat getBlueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(getBlueprintISO8601Format);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", getBlueprintISO8601Format.toPattern());
        assertEquals("GMT", getBlueprintISO8601Format.getTimeZone().getDisplayName());

        DateFormat getBlueprintRFC1123Format = StdDateFormat.getBlueprintRFC1123Format();
        assertNotNull(getBlueprintRFC1123Format);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", getBlueprintRFC1123Format.toPattern());
        assertEquals("GMT", getBlueprintRFC1123Format.getTimeZone().getDisplayName());
    }

    @Test
    public void testParseDateWithDefaultTimeZone() {
        StdDateFormat instance = StdDateFormat.instance;

        Date parsedDate = instance.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(parsedDate);
        assertEquals(2022, parsedDate.getYear());
        assertEquals(11, parsedDate.getMonth());
        assertEquals(30, parsedDate.getDate());
        assertEquals(23, parsedDate.getHours());
        assertEquals(59, parsedDate.getMinutes());
        assertEquals(59, parsedDate.getSeconds());
        assertEquals(123, parsedDate.getMilliseconds());
        assertEquals(0, parsedDate.getTimezoneOffset());

        parsedDate = instance.parse("2022-12-31");
        assertNotNull(parsedDate);
        assertEquals(2022, parsedDate.getYear());
        assertEquals(11, parsedDate.getMonth());
        assertEquals(30, parsedDate.getDate());
        assertEquals(0, parsedDate.getHours());
        assertEquals(0, parsedDate.getMinutes());
        assertEquals(0, parsedDate.getSeconds());
        assertEquals(0, parsedDate.getMilliseconds());
        assertEquals(0, parsedDate.getTimezoneOffset());
    }

    @Test
    public void testParseDateWithCustomTimeZone() {
        StdDateFormat instanceWithCustomTimeZone = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);

        Date parsedDateWithCustomTimeZone = instanceWithCustomTimeZone.parse("2022-12-31T23:59:59.123Z");
        Calendar calendar = Calendar.getInstance(instanceWithCustomTimeZone.getTimeZone());
        calendar.setTime(parsedDateWithCustomTimeZone);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
        ZoneId zoneId = calendar.getTimeZone().toZoneId();
        assertTrue(zoneId.toString().startsWith("America/New_York"));

        parsedDateWithCustomTimeZone = instanceWithCustomTimeZone.parse("2022-12-31");
        calendar.setTime(parsedDateWithCustomTimeZone);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
        assertEquals(zoneId.toString(), "America/New_York");
    }

    @Test
    public void testParseDateWithInvalidFormat() {
        StdDateFormat instance = StdDateFormat.instance;

        try {
            instance.parse("invalid_date_format");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseDateWithCustomLocale() {
        StdDateFormat instanceWithCustomLocale = new StdDateFormat(null, Locale.FRENCH);

        Date parsedDateWithCustomLocale = instanceWithCustomLocale.parse("12 déc. 2022 23:59:59.123");
        Calendar calendar = Calendar.getInstance(instanceWithCustomLocale.getTimeZone());
        calendar.setTime(parsedDateWithCustomLocale);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
        assertEquals("FR", calendar.getDisplayName(Calendar.LANGUAGE));

        parsedDateWithCustomLocale = instanceWithCustomLocale.parse("12 déc. 2022");
        calendar.setTime(parsedDateWithCustomLocale);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
        assertEquals("FR", calendar.getDisplayName(Calendar.LANGUAGE));
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        StdDateFormat instanceWithCustomTimeZone = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        Date date = new GregorianCalendar(2022, 11, 30, 23, 59, 59, 123).getTime();

        String formattedDate = instanceWithCustomTimeZone.format(date);
        String expectedFormattedDate = "Thu, 30 Dec 2022 20:59:59.123 PST";
        assertEquals(expectedFormattedDate, formattedDate);
    }

    @Test
    public void testFormatDateWithCustomLocale() {
        StdDateFormat instanceWithCustomLocale = new StdDateFormat(null, new Locale("fr", "FR"));
        Date date = new GregorianCalendar(2022, 11, 30, 23, 59, 59, 123).getTime();

        String formattedDate = instanceWithCustomLocale.format(date);
        String expectedFormattedDate = "dim. 30 déc. 2022 21:59:59.123";
        assertEquals(expectedFormattedDate, formattedDate);
    }

    @Test
    public void testFormatMillisecondsWithCustomTimeZone() {
        StdDateFormat instanceWithCustomTimeZone = new StdDateFormat(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN);

        long milliseconds = System.currentTimeMillis();
        String formattedMilliseconds = instanceWithCustomTimeZone.format(new Date(milliseconds));

        String expectedFormattedMilliseconds = "日, [月日] 時:分:秒 JST";
        int index = formattedMilliseconds.indexOf(expectedFormattedMilliseconds);
        assertTrue(index >= 0);
    }

    @Test
    public void testFormatMillisecondsWithCustomLocale() {
        StdDateFormat instanceWithCustomLocale = new StdDateFormat(null, Locale.SIMPLIFIED_CHINESE);

        long milliseconds = System.currentTimeMillis();
        String formattedMilliseconds = instanceWithCustomLocale.format(new Date(milliseconds));

        String expectedFormattedMilliseconds = "yyyy年MM月dd日 HH时mm分ss秒";
        int index = formattedMilliseconds.indexOf(expectedFormattedMilliseconds);
        assertTrue(index >= 0);
    }

    @Test
    public void testClone() {
        StdDateFormat instance = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        StdDateFormat clonedInstance = (StdDateFormat) instance.clone();

        assertEquals(instance.getLocale(), clonedInstance.getLocale());
        assertFalse(instance.getTimeZone().equals(clonedInstance.getTimeZone()));
    }
}