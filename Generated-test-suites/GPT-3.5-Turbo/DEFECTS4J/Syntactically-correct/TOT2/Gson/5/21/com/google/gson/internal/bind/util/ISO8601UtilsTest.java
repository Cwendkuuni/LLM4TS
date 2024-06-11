package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date(1633143633000L); // 2021-10-02T15:27:13Z
        assertEquals("2021-10-02T15:27:13Z", ISO8601Utils.format(date));
    }

    @Test
    public void testFormatDateWithMillis() {
        Date date = new Date(1633143633000L); // 2021-10-02T15:27:13Z
        assertEquals("2021-10-02T15:27:13.000Z", ISO8601Utils.format(date, true));
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date(1633143633000L); // 2021-10-02T15:27:13Z
        TimeZone timezone = TimeZone.getTimeZone("GMT+02:00");
        assertEquals("2021-10-02T17:27:13.000+02:00", ISO8601Utils.format(date, true, timezone));
    }

    @Test
    public void testParseDate() throws ParseException {
        String isoDate = "2021-10-02T15:27:13Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(isoDate, pos);
        assertEquals(1633143633000L, date.getTime());
    }

    @Test
    public void testParseInvalidDate() {
        String isoDate = "2021-10-02T15:27:13"; // Missing time zone info
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(isoDate, pos);
            fail("ParseException expected for invalid date");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseDateWithOffset() throws ParseException {
        String isoDate = "2021-10-02T15:27:13+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(isoDate, pos);
        assertEquals(1633143633000L, date.getTime());
    }

    @Test
    public void testParseDateWithTimeZone() throws ParseException {
        String isoDate = "2021-10-02T15:27:13.500+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(isoDate, pos);
        assertEquals(1633143633500L, date.getTime());
    }

    @Test
    public void testParseInvalidTimeZone() {
        String isoDate = "2021-10-02T15:27:13.500GMT+02:00"; // Invalid timezone indicator
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(isoDate, pos);
            fail("ParseException expected for invalid timezone");
        } catch (ParseException e) {
            // Expected exception
        }
    }

}