package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;

        // Test with no timezone
        date = dateFormat.parse("2022-12-31");
        assertEquals(new Date(1671459200000L), date); // Last day of 2022

        // Test with timezone
        date = dateFormat.parse("2022-12-31T23:59:59.000Z");
        assertEquals(new Date(1671462799000L), date); // Last second of 2022 with Zulu timezone

        // Test with timezone offset
        date = dateFormat.parse("2022-12-31T23:59:59.000+01:00");
        assertEquals(new Date(1671463699000L), date); // Last second of 2022 with CET timezone
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test with GMT timezone
        Date date = dateFormat.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals(new Date(1671462799000L), date); // Last second of 2022 with GMT timezone

        // Test with timezone offset
        date = dateFormat.parse("Sat, 31 Dec 2022 23:59:59 CET");
        assertEquals(new Date(1671463699000L), date); // Last second of 2022 with CET timezone
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        StringFormat stringFormat = new StringFormat();

        // Test with no timezone
        String formattedDate = dateFormat.format(new Date(1671459200000L), stringFormat, new FieldPosition[0]);
        assertEquals("Sat Dec 31 00:00:00 IST 2022", formattedDate); // Last day of 2022 with IST timezone

        // Test with timezone
        formattedDate = dateFormat.format(new Date(1671462799000L), stringFormat, new FieldPosition[0]);
        assertEquals("Sat Dec 31 23:59:59 IST 2022", formattedDate); // Last second of 2022 with IST timezone
    }
}