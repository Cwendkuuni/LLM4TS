package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String iso8601Date = ISO8601Utils.format(date);
        assertNotNull(iso8601Date);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String iso8601Date = ISO8601Utils.format(date, true);
        assertNotNull(iso8601Date);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String iso8601Date = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));
        assertNotNull(iso8601Date);
    }

    @Test
    public void testFormatWithMillisAndTimeZone() {
        Date date = new Date();
        String iso8601Date = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        assertNotNull(iso8601Date);
    }

    @Test
    public void testParse() {
        String iso8601Date = "2020-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date date = ISO8601Utils.parse(iso8601Date, pos);
            assertNotNull(date);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String iso8601Date = "2020-13-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(iso8601Date, pos);
    }

    @Test
    public void testParseWithOffset() {
        String iso8601Date = "2020-01-01T12:00:00+01:00";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date date = ISO8601Utils.parse(iso8601Date, pos);
            assertNotNull(date);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOffset() throws ParseException {
        String iso8601Date = "2020-01-01T12:00:00+00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(iso8601Date, pos);
    }
}