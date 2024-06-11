package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    private static final String TEST_DATE_FORMATS[] = new String[] {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd", "EEE, dd MMM yyyy HH:mm:ss zzz"};
    private static final String TEST_DATE_STRINGS[] = new String[] {"2022-03-15T12:34:56.789Z", "2022-03-15T12:34:56-05:00", "2022-03-15", "Wed, 15 Mar 2022 12:34:56 EDT"};

    /**
     * Test basic parse functionality using standard date format instances.
     * @throws ParseException
     */
    @Test
    public void testParse() throws ParseException {
        for (int i = 0; i < TEST_DATE_FORMATS.length; i++) {
            String format = TEST_DATE_FORMATS[i];
            String dateString = TEST_DATE_STRINGS[i];
            DateFormat df = StdDateFormat.getInstance();
            df.setDateFormatString(format);
            Date date = df.parse(dateString);
            assertNotNull(date);
            assertTrue(date.getClass().isInstance(new Date()));
            Date expectedDate = new Date(1647150296000L); // 15 Mar 2022, 12:34:56
            assertEquals(expectedDate, date);
            assertEquals(format, df.getDateFormatString());
        }
    }

    /**
     * Test parse functionality with custom timezone and locale.
     * @throws ParseException
     */
    @Test
    public void testParseWithCustomTimezoneAndLocale() throws ParseException {
        TimeZone customTimezone = TimeZone.getTimeZone("America/Los_Angeles");
        Locale customLocale = Locale.US;

        for (String format : TEST_DATE_FORMATS) {
            String dateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.length - 1 - (TEST_DATE_FORMATS.length % TEST_DATE_STRINGS.length)]; // alternate between formats
            DateFormat df = new StdDateFormat(customTimezone, customLocale);
            df.setDateFormatString(format);
            Date date = df.parse(dateString);
            assertNotNull(date);
            assertTrue(date.getClass().isInstance(new Date()));
            Date expectedDate = new Date(1647150296000L - 7 * 60 * 60 * 1000); // Pacific Time, 15 Mar 2022, 12:34:56
            assertEquals(expectedDate, date);
            assertEquals(format, df.getDateFormatString());
        }
    }

    /**
     * Test parse functionality with custom timezone and default locale.
     * @throws ParseException
     */
    @Test
    public void testParseWithCustomTimezone() throws ParseException {
        TimeZone customTimezone = TimeZone.getTimeZone("America/Los_Angeles");

        for (String format : TEST_DATE_FORMATS) {
            String dateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.length - 1 - (TEST_DATE_FORMATS.length % TEST_DATE_STRINGS.length)]; // alternate between formats
            DateFormat df = new StdDateFormat(customTimezone);
            df.setDateFormatString(format);
            Date date = df.parse(dateString);
            assertNotNull(date);
            assertTrue(date.getClass().isInstance(new Date()));
            Date expectedDate = new Date(1647150296000L - 7 * 60 * 60 * 1000); // Pacific Time, 15 Mar 2022, 12:34:56
            assertEquals(expectedDate, date);
            assertEquals(format, df.getDateFormatString());
        }
    }

    /**
     * Test parse functionality with default timezone and custom locale.
     * @throws ParseException
     */
    @Test
    public void testParseWithCustomLocale() throws ParseException {
        Locale customLocale = Locale.FRENCH;

        for (String format : TEST_DATE_FORMATS) {
            String dateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.length % TEST_DATE_STRINGS.length]; // same format as previous test
            DateFormat df = new StdDateFormat(Locale.getDefault());
            df.setDateFormatString(format);
            df.setLocale(customLocale);
            Date date = df.parse(dateString);
            assertNotNull(date);
            assertTrue(date.getClass().isInstance(new Date()));
            Date expectedDate = new Date(1647150296000L); // GMT, 15 Mar 2022, 12:34:56
            assertEquals(expectedDate, date);
            assertEquals(format, df.getDateFormatString());
            assertEquals(customLocale.toString(), df.getLocale().toString());
        }
    }

    /**
     * Test parse functionality with default timezone and default locale.
     * @throws ParseException
     */
    @Test
    public void testParseWithDefaults() throws ParseException {
        for (String format : TEST_DATE_FORMATS) {
            String dateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.length % TEST_DATE_STRINGS.length]; // same format as previous test
            DateFormat df = StdDateFormat.getInstance();
            df.setDateFormatString(format);
            Date date = df.parse(dateString);
            assertNotNull(date);
            assertTrue(date.getClass().isInstance(new Date()));
            Date expectedDate = new Date(1647150296000L); // GMT, 15 Mar 2022, 12:34:56
            assertEquals(expectedDate, date);
            assertEquals(format, df.getDateFormatString());
            assertEquals(Locale.getDefault().toString(), df.getLocale().toString());
        }
    }

    /**
     * Test parse functionality with malformed date strings.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testParseWithMalformedDateStrings() throws ParseException {
        String malformedDateString = "2022-03-15T12:34:56-05:00XYZ"; // invalid character "X"
        DateFormat df = StdDateFormat.getInstance();
        df.setDateFormatString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.parse(malformedDateString); // should throw ParseException
    }

    /**
     * Test format functionality using standard date format instances.
     */
    @Test
    public void testFormat() {
        for (String format : TEST_DATE_FORMATS) {
            DateFormat df = StdDateFormat.getInstance();
            df.setDateFormatString(format);
            String dateString = df.format(new GregorianCalendar(2022, Calendar.MARCH, 15, 12, 34, 56, 789).getTime());
            String expectedDateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.indexOf(format)];
            assertEquals(expectedDateString, dateString);
        }
    }

    /**
     * Test format functionality with custom timezone and locale.
     */
    @Test
    public void testFormatWithCustomTimezoneAndLocale() {
        TimeZone customTimezone = TimeZone.getTimeZone("America/Los_Angeles");
        Locale customLocale = Locale.US;

        for (String format : TEST_DATE_FORMATS) {
            DateFormat df = new StdDateFormat(customTimezone, customLocale);
            df.setDateFormatString(format);
            df.setTimeZone(customTimezone);
            df.setLocale(customLocale);
            String dateString = df.format(new GregorianCalendar(2022, Calendar.MARCH, 15, 12, 34, 56, 789).getTime());
            String expectedDateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.length - 1 - (TEST_DATE_FORMATS.length % TEST_DATE_STRINGS.length)]; // alternate between formats
            assertEquals(expectedDateString, dateString);
        }
    }

    /**
     * Test format functionality with custom timezone and default locale.
     */
    @Test
    public void testFormatWithCustomTimezone() {
        TimeZone customTimezone = TimeZone.getTimeZone("America/Los_Angeles");

        for (String format : TEST_DATE_FORMATS) {
            DateFormat df = new StdDateFormat(customTimezone);
            df.setDateFormatString(format);
            String dateString = df.format(new GregorianCalendar(2022, Calendar.MARCH, 15, 12, 34, 56, 789).getTime());
            String expectedDateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.length - 1 - (TEST_DATE_FORMATS.length % TEST_DATE_STRINGS.length)]; // alternate between formats
            assertEquals(expectedDateString, dateString);
        }
    }

    /**
     * Test format functionality with default timezone and custom locale.
     */
    @Test
    public void testFormatWithCustomLocale() {
        Locale customLocale = Locale.FRENCH;

        for (String format : TEST_DATE_FORMATS) {
            DateFormat df = StdDateFormat.getInstance();
            df.setDateFormatString(format);
            df.setLocale(customLocale);
            String dateString = df.format(new GregorianCalendar(2022, Calendar.MARCH, 15, 12, 34, 56, 789).getTime());
            String expectedDateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.indexOf(format)];
            assertEquals(expectedDateString, dateString);
        }
    }

    /**
     * Test format functionality with default timezone and default locale.
     */
    @Test
    public void testFormatWithDefaults() {
        for (String format : TEST_DATE_FORMATS) {
            DateFormat df = StdDateFormat.getInstance();
            df.setDateFormatString(format);
            String dateString = df.format(new GregorianCalendar(2022, Calendar.MARCH, 15, 12, 34, 56, 789).getTime());
            String expectedDateString = TEST_DATE_STRINGS[TEST_DATE_FORMATS.indexOf(format)];
            assertEquals(expectedDateString, dateString);
        }
    }
}