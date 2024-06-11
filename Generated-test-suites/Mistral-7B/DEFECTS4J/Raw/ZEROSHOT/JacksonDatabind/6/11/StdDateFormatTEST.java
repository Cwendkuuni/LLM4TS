package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test parse with ISO-8601 format
        Date date1 = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Unexpected date Parsed from ISO-8601", new Date(1671562399000L), date1);

        // Test parse with ISO-8601 format and custom timezone
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat stdDateFormatWithTimezone = stdDateFormat.withTimeZone(timeZone);
        Date date2 = stdDateFormatWithTimezone.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Unexpected date Parsed from ISO-8601 with custom timezone", new Date(1671528799000L), date2);

        // Test parse with RFC-1123 format
        Date date3 = stdDateFormat.parse("Sat, 31 Dec 2022 23:59:59 EST");
        assertEquals("Unexpected date Parsed from RFC-1123", new Date(1671528799000L), date3);

        // Test parse with RFC-1123 format and custom timezone
        StdDateFormat stdDateFormatWithTimezoneRFC1123 = stdDateFormat.withLocale(Locale.US).withTimeZone(timeZone);
        Date date4 = stdDateFormatWithTimezoneRFC1123.parse("Sat, 31 Dec 2022 23:59:59 EST");
        assertEquals("Unexpected date Parsed from RFC-1123 with custom timezone", new Date(1671528799000L), date4);

        // Test parse with invalid date string
        String invalidDateString = "invalidDateString";
        try {
            stdDateFormat.parse(invalidDateString);
            fail("Expected ParseException for invalid date string");
        } catch (ParseException e) {
            // Expected
        }

        // Test format
        String iso8601FormattedDate = stdDateFormat.format(new Date(1671562399000L));
        assertEquals("Unexpected ISO-8601 formatted date", "2022-12-31T23:59:59.123Z", iso8601FormattedDate);

        // Test format with custom timezone
        String rfc1123FormattedDateWithCustomTimezone = stdDateFormatWithTimezoneRFC1123.format(new Date(1671528799000L));
        assertEquals("Unexpected RFC-1123 formatted date with custom timezone", "Sat Dec 31 23:59:59 EST 2022", rfc1123FormattedDateWithCustomTimezone);
    }
}