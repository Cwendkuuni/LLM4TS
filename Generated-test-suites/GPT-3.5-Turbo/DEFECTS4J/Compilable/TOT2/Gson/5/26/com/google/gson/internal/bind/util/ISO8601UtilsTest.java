package com.google.gson.internal.bind.util;
import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat1() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        assertEquals(20, formattedDate.length());
    }

    @Test
    public void testFormat2() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.contains("."));
    }

    @Test
    public void testFormat3() {
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(date, true, tz);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.contains("+") || formattedDate.contains("-"));
    }

    @Test
    public void testParseValidDate() {
        String dateStr = "2022-12-31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Valid date should not throw ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testParseInvalidDate() {
        String dateStr = "2022-12-31T23:59:59";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStr, pos);
            fail("Invalid date should throw ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseIncompleteDate() {
        String dateStr = "2022-12-31T23:59";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStr, pos);
            fail("Incomplete date should throw ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseInvalidTimezone() {
        String dateStr = "2022-12-31T23:59:59ABC";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStr, pos);
            fail("Invalid timezone format should throw ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}