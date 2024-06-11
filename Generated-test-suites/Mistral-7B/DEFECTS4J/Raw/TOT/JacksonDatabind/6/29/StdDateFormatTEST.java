package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        // Test parsing simple date
        Date date = df.parse("2022-03-15");
        assertEquals("2022-03-15", df.format(date));

        // Test parsing date with time
        date = df.parse("2022-03-15 10:15:30.123Z");
        assertEquals("2022-03-15 10:15:30.123", df.format(date));

        // Test parsing date with time and timezone
        date = df.parse("2022-03-15 10:15:30.123+01:00");
        assertEquals("2022-03-15 11:15:30.123", df.format(date));

        // Test parsing date with timezone but missing milliseconds
        date = df.parse("2022-03-15 10:15+01:00");
        assertEquals("2022-03-15 10:15:00.000", df.format(date));

        // Test parsing date with timezone but missing seconds and milliseconds
        date = df.parse("2022-03-15 10+01:00");
        assertEquals("2022-03-15 10:00:00.000", df.format(date));

        // Test parsing date with time but missing timezone
        date = df.parse("2022-03-15 10:15:30.123");
        assertEquals("2022-03-15 10:15:30.123", df.format(date));

        // Test parsing date with time but missing milliseconds
        date = df.parse("2022-03-15 10:15:30");
        assertEquals("2022-03-15 10:15:30.000", df.format(date));

        // Test parsing date with time but missing seconds and milliseconds
        date = df.parse("2022-03-15 10:15");
        assertEquals("2022-03-15 10:15:00.000", df.format(date));

        // Test parsing date with time but missing time
        date = df.parse("2022-03-15 10:15:30.123+01:00:00");
        assertEquals("2022-03-15 11:15:30.123", df.format(date));

        // Test parsing date with time, timezone and milliseconds, but no separator
        date = df.parse("20220315101530Z.123");
        assertEquals("2022-03-15 10:15:30.123", df.format(date));

        // Test parsing date with time, timezone and milliseconds, but separators missing
        date = df.parse("2022-03-15101530.123Z");
        assertEquals("2022-03-15 10:15:30.123", df.format(date));

        // Test parsing date with time, timezone and milliseconds, with separators in wrong order
        date = df.parse("2022-03-15.10:15:30.123Z");
        assertEquals("2022-03-15 10:15:30.123", df.format(date));

        // Test parsing date with time but no separators
        date = df.parse("20220315101530.123");
        assertEquals("2022-03-15 10:15:30.123", df.format(date));

        // Test parsing date with time but no separators, incorrect format
        date = df.parse("20220315101530.123Z.123");
        assertEquals("Can not parse date \"20220315101530.123.123\": not compatible with any of standard forms (\"[yyyy-MM-dd'T'HH:mm:ss.SSSZ]\", \"EEE, dd MMM yyyy HH:mm:ss zzz\", \"yyyy-MM-dd\", \"EEE, dd MMM yyyy HH:mm:ss z\", \"yyyy-MM-dd\")", df.getParseException().getMessage());

        // Test parsing date with invalid timezone
        date = df.parse("2022-03-15T10:15:30.123+99:00");
        assertEquals("Can not parse date \"2022-03-15T10:15:30.123+99:00\": Invalid time zone '99:00'. Valid formats are 'Z', '+hh:mm', '-hh:mm' or empty string.", df.getParseException().getMessage());
    }

    @Test
    public void testParseRFC1123() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        // Test parsing simple date
        Date date = df.parse("Tue, 15 Mar 2022 10:15:30 GMT");
        assertEquals("Tue Mar 15 10:15:30 GMT 2022", df.format(date));

        // Test parsing date with timezone but missing milliseconds
        date = df.parse("Tue, 15 Mar 2022 10:15 GMT");
        assertEquals("Tue Mar 15 10:15:00.000 GMT 2022", df.format(date));

        // Test parsing date with timezone but missing seconds and milliseconds
        date = df.parse("Tue, 15 Mar 2022 10 GMT");
        assertEquals("Tue Mar 15 10:00:00.000 GMT 2022", df.format(date));

        // Test parsing date with time but missing timezone
        date = df.parse("Tue, 15 Mar 2022 10:15:30 GMT");
        assertEquals("Tue Mar 15 10:15:30 GMT 2022", df.format(date));

        // Test parsing date with time but missing seconds and milliseconds
        date = df.parse("Tue, 15 Mar 2022 10:15 GMT");
        assertEquals("Tue Mar 15 10:15:00.000 GMT 2022", df.format(date));

        // Test parsing date with time but missing timezone and milliseconds
        date = df.parse("Tue, 15 Mar 2022 10:15");
        assertEquals("Tue Mar 15 10:15:00.000 GMT 2022", df.format(date));

        // Test parsing date with time, but incorrect format
        date = df.parse("Tue, 15 Mar 2022 10:15:30 GMT+99:00");
        assertEquals("Can not parse date \"Tue, 15 Mar 2022 10:15:30 GMT+99:00\": Invalid time zone '99:00'. Valid formats are 'Z', '+hh:mm', '-hh:mm' or empty string.", df.getParseException().getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalid() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        df.parse("InvalidDateString");
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        Date date = new Date();

        String formattedDate = df.format(date);
        assertEquals("Sat Mar 18 14:11:41 MDT 2023", formattedDate);

        formattedDate = df.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("Sat Mar 18 14:11:41 MDT 2023", formattedDate.toString());

        formattedDate = df.format(date, new StringBuffer("CustomFormat"), new FieldPosition(0, 0));
        assertEquals("CustomFormat", formattedDate.toString());
    }
}