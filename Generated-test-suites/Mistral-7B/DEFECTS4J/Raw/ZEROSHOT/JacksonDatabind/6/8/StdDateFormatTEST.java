package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test getBlueprintISO8601Format() (deprecated)
        DateFormat blueprintISO8601Format = dateFormat.getBlueprintISO8601Format();
        assertEquals("Blueprint for ISO8601 format should not be null", true, blueprintISO8601Format != null);
        assertEquals("Blueprint for ISO8601 format should use GMT timezone", "GMT", blueprintISO8601Format.getTimeZone().getID());

        // Test getISO8601Format()
        DateFormat iso8601Format = dateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        assertEquals("ISO8601 format should not be null", true, iso8601Format != null);
        assertEquals("ISO8601 format should use UTC timezone", "UTC", iso8601Format.getTimeZone().getID());

        // Test parse() with ISO8601 format
        Date parsedDate;
        ParsePosition parsePosition = new ParsePosition(0);
        String isoDate = "2023-03-11T15:32:10.123Z";
        parsedDate = iso8601Format.parse(isoDate, parsePosition);
        assertEquals("Parse of ISO8601 format date should not be null", true, parsedDate != null);
        assertEquals("Parse of ISO8601 format date should have correct value", "Sat Mar 11 15:32:10 UTC 2023", parsedDate.toString());

        // Test parse() with RFC1123 format
        DateFormat rfc1123Format = dateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.US);
        String rfcDate = "Sat, 11 Mar 2023 15:32:10 UTC";
        parsedDate = rfc1123Format.parse(rfcDate, parsePosition);
        assertEquals("Parse of RFC1123 format date should not be null", true, parsedDate != null);
        assertEquals("Parse of RFC1123 format date should have correct value", "Sat Mar 11 15:32:10 UTC 2023", parsedDate.toString());

        // Test parse() with invalid date strings
        String invalidDate = "invalidDate";
        parsedDate = iso8601Format.parse(invalidDate, parsePosition);
        assertNull("Parse of invalid date should return null", parsedDate);
        assertEquals("Parse position should indicate error index", -1, parsePosition.getErrorIndex());

        // Test format()
        StringFormater formatter = new StringFormater();
        StringFormater formatter1 = new StringFormater();
        String formattedDate;
        Date date = new Date(1678144000000L); // Sat Mar 11 2023 12:00:00 GMT
        formattedDate = iso8601Format.format(date, formatter);
        assertEquals("Format of ISO8601 format date should have correct value", "2023-03-11T00:00:00Z", formattedDate);

        formattedDate = rfc1123Format.format(date, formatter1);
        assertEquals("Format of RFC1123 format date should have correct value", "Sat, 11 Mar 2023 00:00:00 UTC", formattedDate);

        // Test setTimeZone()
        TimeZone newTimeZone = TimeZone.getTimeZone("Europe/Moscow");
        DateFormat newFormat = dateFormat.clone();
        newFormat.setTimeZone(newTimeZone);
        String moscowDate = "2023-03-11T17:00:10.123Z";
        parsedDate = newFormat.parse(moscowDate, parsePosition);
        assertEquals("Parse of date in Moscow timezone should have correct value", "Sat Mar 11 17:00:10 MSK 2023", parsedDate.toString());
    }

    private static class StringFormater extends StringBuffer {
        StringFormater() {
            setLength(0);
        }
    }
}