package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test parsing with default timezone and locale
        String dateString = "2022-03-01T14:30:45.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", "Wed Mar 01 14:30:45 UTC 2022",
                dateFormat.format(parsedDate));

        dateString = "2022-03-01";
        parsedDate = dateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", "Wed Mar 01 00:00:00 UTC 2022",
                dateFormat.format(parsedDate));

        dateString = "Wed, 01 Mar 2022 14:30:45 EDT";
        parsedDate = dateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", "Wed Mar 01 18:30:45 EDT 2022",
                dateFormat.format(parsedDate));

        // Test parsing with custom timezone and locale
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        Locale customLocale = Locale.US;
        StdDateFormat customDateFormat = new StdDateFormat(customTimeZone, customLocale);
        dateString = "Wed, 01 Mar 2022 14:30:45 EDT";
        parsedDate = customDateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", "Wed Mar 01 19:30:45 EDT 2022",
                customDateFormat.format(parsedDate));

        // Test cloning
        StdDateFormat clonedDateFormat = dateFormat.clone();
        parsedDate = clonedDateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", "Wed Mar 01 14:30:45 UTC 2022",
                clonedDateFormat.format(parsedDate));

        // Test setting timezone and locale
        StdDateFormat customTimeZoneDateFormat = new StdDateFormat(customTimeZone, customLocale);
        StdDateFormat timeZoneDateFormat = customTimeZoneDateFormat.withTimeZone(TimeZone.getTimeZone("UTC"));
        parsedDate = timeZoneDateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", "Wed Mar 01 14:30:45 UTC 2022",
                timeZoneDateFormat.format(parsedDate));

        StdDateFormat localeDateFormat = timeZoneDateFormat.withLocale(Locale.CHINESE);
        parsedDate = localeDateFormat.parse(dateString);
        assertEquals("Incorrect date parsing", " Wed 三 03月 01日 14:30:45 UTC 2022",
                localeDateFormat.format(parsedDate));
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "InvalidDateString";
        dateFormat.parse(dateString);
        fail("Expected ParseException");
    }
}