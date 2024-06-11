package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1633052800000L); // Tue, 02 Mar 2021 00:00:00 GMT
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-03-02T00:00:00Z", formattedDate);

        Date dateWithMilliseconds = new Date(1633052800001L); // Tue, 02 Mar 2021 00:00:00.001 GMT
        String formattedDateWithMilliseconds = ISO8601Utils.format(dateWithMilliseconds, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-03-02T00:00:00.001Z", formattedDateWithMilliseconds);

        Date dateWithTimezone = new Date(1633052800000L); // Tue, 02 Mar 2021 00:00:00 GMT
        String formattedDateWithTimezone = ISO8601Utils.format(dateWithTimezone, true, new TimeZone("Europe/London"));
        assertEquals("2021-03-02T00:01:00+00:00", formattedDateWithTimezone);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-03-02T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1633052800000L), parsedDate);
        assertEquals(0, pos.getIndex());

        String dateStringWithMilliseconds = "2021-03-02T00:00:00.001Z";
        pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(dateStringWithMilliseconds, pos);
        assertEquals(new Date(1633052800001L), parsedDate);
        assertEquals(4, pos.getIndex());

        String dateStringWithTimezone = "2021-03-02T00:01:00+00:00";
        pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(dateStringWithTimezone, pos);
        assertEquals(new Date(1633052861000L), parsedDate);
        assertEquals(16, pos.getIndex());

        String invalidDateString = "2021-03-02T00:01:00A";
        pos = new ParsePosition(0);
        try {
            parsedDate = ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
        assertEquals(8, pos.getIndex());
    }

}