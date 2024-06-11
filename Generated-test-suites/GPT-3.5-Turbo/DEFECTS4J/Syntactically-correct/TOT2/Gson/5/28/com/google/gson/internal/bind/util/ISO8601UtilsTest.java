package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date currentDate = new Date();
        String formattedDate = ISO8601Utils.format(currentDate);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date currentDate = new Date();
        String formattedDate = ISO8601Utils.format(currentDate, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() {
        String isoDate = "2021-09-15T12:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(isoDate, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testParseWithInvalidDate() {
        String invalidDate = "2021-09-15T12:30:45"; // missing timezone
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(invalidDate, pos);
            fail("Exception should be thrown for invalid date");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseWithInvalidFormat() {
        String invalidDate = "2021-09-15T12:30:45+05:00+06:00"; // invalid timezone
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(invalidDate, pos);
            fail("Exception should be thrown for invalid date format");
        } catch (ParseException e) {
            // Expected exception
        }
    }
}