package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical case
        String formattedDate = ISO8601Utils.format(new Date());
        assertNotNull(formattedDate);
        // Add more test cases here
    }

    @Test
    public void testFormatWithMillis() {
        // Typical case
        String formattedDate = ISO8601Utils.format(new Date(), true);
        assertNotNull(formattedDate);
        // Add more test cases here
    }

    @Test
    public void testFormatWithTimeZone() {
        // Typical case
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(new Date(), true, tz);
        assertNotNull(formattedDate);
        // Add more test cases here
    }

    @Test
    public void testParse() {
        // Typical case
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse("2022-12-31T23:59:59Z", pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("ParseException should not be thrown in typical case");
        }
        // Add more test cases here
    }

}