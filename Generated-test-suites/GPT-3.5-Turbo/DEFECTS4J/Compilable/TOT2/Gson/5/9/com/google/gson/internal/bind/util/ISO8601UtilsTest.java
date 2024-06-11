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
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+1"));
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2022-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateString = "2022-01-01X12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String dateString = "2022-01-01T12:00:00GMT+1";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}