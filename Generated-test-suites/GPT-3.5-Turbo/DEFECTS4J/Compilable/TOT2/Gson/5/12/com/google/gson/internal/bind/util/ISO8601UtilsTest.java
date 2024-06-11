package com.google.gson.internal.bind.util;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2021, Calendar.JANUARY, 1, 10, 30, 15);
        String formatted = ISO8601Utils.format(calendar.getTime());
        assertEquals("2021-01-01T10:30:15Z", formatted);
    }

    @Test
    public void testFormatWithMillis() {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2021, Calendar.JANUARY, 1, 10, 30, 15);
        String formatted = ISO8601Utils.format(calendar.getTime(), true);
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"));
    }

    @Test
    public void testParse() throws ParseException {
        String isoDate = "2021-12-31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2021, Calendar.DECEMBER, 31, 23, 59, 59);
        assertEquals(calendar.getTime(), ISO8601Utils.parse(isoDate, pos));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String invalidDate = "2021/12/31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidDate, pos);
    }

}