package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test for default timezone
        String expected = "2021-05-11T11:30:00Z";
        Date date = new Date(1620730200000L);
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);

        // Test for GMT timezone with milliseconds
        expected = "2021-05-11T12:30:00.500+01:00";
        actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT+01:00"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        // Test for valid date string
        String dateStr = "2021-05-11T11:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1620730200000L);
        Date actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);

        // Test for invalid date string
        dateStr = "2021-05-11T11:30:00ZZ";
        pos = new ParsePosition(0);
        try {
            actual = ISO8601Utils.parse(dateStr, pos);
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"2021-05-11T11:30:00ZZ\"]", e.getMessage());
        }
    }

}