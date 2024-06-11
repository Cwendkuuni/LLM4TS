package com.google.gson.internal.bind.util;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.Date;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_Date() {
        Date date = new Date(1628012400000L); // August 3, 2021 12:00:00 AM UTC

        String formattedDate = ISO8601Utils.format(date);

        assertEquals("2021-08-03T00:00:00Z", formattedDate);
    }

    @Test
    public void testFormat_Date_Millis() {
        Date date = new Date(1628012400000L); // August 3, 2021 12:00:00 AM UTC

        String formattedDate = ISO8601Utils.format(date, true);

        assertEquals("2021-08-03T00:00:00.000Z", formattedDate);
    }

    @Test
    public void testFormat_Date_TimeZone() {
        Date date = new Date(1628012400000L); // August 3, 2021 12:00:00 AM UTC
        TimeZone timeZone = TimeZone.getTimeZone("GMT");

        String formattedDate = ISO8601Utils.format(date, false, timeZone);

        assertEquals("2021-08-03T00:00:00Z", formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-08-03T12:30:45Z";
        ParsePosition parsePosition = new ParsePosition(0);

        Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);

        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDateFormat() throws ParseException {
        String dateString = "03-08-2021T12:30:45Z"; // Invalid format
        ParsePosition parsePosition = new ParsePosition(0);

        ISO8601Utils.parse(dateString, parsePosition);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidTimeZone() throws ParseException {
        String dateString = "2021-08-03T12:30:45+0700"; // Invalid timezone format
        ParsePosition parsePosition = new ParsePosition(0);

        ISO8601Utils.parse(dateString, parsePosition);
    }
}