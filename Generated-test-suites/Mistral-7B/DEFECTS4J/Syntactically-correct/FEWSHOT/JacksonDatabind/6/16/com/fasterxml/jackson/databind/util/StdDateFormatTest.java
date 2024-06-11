package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601Format() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date expectedDate;
        Date actualDate;

        // Test parsing with GMT timezone
        expectedDate = new Date(1609459200000L); // Thu Jan 01 2020 01:00:00 GMT
        String iso8601DateString = "2020-01-01T01:00:00Z";
        actualDate = dateFormat.parse(iso8601DateString);
        assertEquals(expectedDate, actualDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat customDateFormat = new StdDateFormat(customTimeZone, Locale.US);
        expectedDate = new Date(1609422800000L); // Wed Dec 31 2019 17:00:00 EST
        String iso8601DateStringWithCustomTimezone = "2019-12-31T17:00:00-05:00";
        actualDate = customDateFormat.parse(iso8601DateStringWithCustomTimezone);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testParseRFC1123Format() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date expectedDate;
        Date actualDate;

        // Test parsing with GMT timezone
        expectedDate = new Date(1609459200000L); // Thu Jan 01 2020 01:00:00 GMT
        String rfc1123DateString = "Wed, 31 Dec 1969 19:00:00 GMT";
        actualDate = dateFormat.parse(rfc1123DateString);
        assertEquals(expectedDate, actualDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat customDateFormat = new StdDateFormat(customTimeZone, Locale.US);
        expectedDate = new Date(1609422400000L); // Tue Dec 30 2019 16:00:00 PST
        String rfc1123DateStringWithCustomTimezone = "Tue, 30 Dec 1969 18:00:00 PST";
        actualDate = customDateFormat.parse(rfc1123DateStringWithCustomTimezone);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testParsePlainDateFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date expectedDate;
        Date actualDate;

        // Test parsing with GMT timezone
        expectedDate = new Date(1609459200000L); // Thu Jan 01 2020 01:00:00 GMT
        String plainDateString = "2020-01-01";
        actualDate = dateFormat.parse(plainDateString);
        assertNull(actualDate);

        expectedDate = new Date(1609535600000L); // Fri Jan 02 2020 00:00:00 GMT
        String plainDateStringWithTime = "2020-01-02 00:00:00";
        actualDate = dateFormat.parse(plainDateStringWithTime);
        assertEquals(expectedDate, actualDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat customDateFormat = new StdDateFormat(customTimeZone, Locale.US);
        expectedDate = new Date(1609422800000L); // Wed Dec 31 2019 17:00:00 EST
        String plainDateStringWithCustomTimezone = "2019-12-31 17:00:00";
        actualDate = customDateFormat.parse(plainDateStringWithCustomTimezone);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testFormatDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String expectedDateString;
        String actualDateString;

        Date date = new Date(1609459200000L); // Thu Jan 01 2020 01:00:00 GMT
        expectedDateString = "2020-01-01T01:00:00Z";
        actualDateString = dateFormat.format(date);
        assertEquals(expectedDateString, actualDateString);

        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat customDateFormat = new StdDateFormat(customTimeZone, Locale.US);
        expectedDateString = "2019-12-31T17:00:00-05:00";
        actualDateString = customDateFormat.format(date);
        assertEquals(expectedDateString, actualDateString);
    }
}