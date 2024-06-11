package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.java
 *
 * @author your_name
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2019, 10, 31, 12, 0, 0).getTime();

        String formattedDate1 = ISO8601Utils.format(date);
        assertEquals("2019-11-01T12:00:00Z", formattedDate1);

        String formattedDate2 = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2019-11-01T19:00:00-08:00", formattedDate2);
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);

        Date date1 = ISO8601Utils.parse("2019-11-01T12:00:00Z", pos);
        assertEquals(new GregorianCalendar(2019, 10, 31, 12, 0, 0).getTime(), date1);
        assertEquals(pos.getIndex(), 19);

        Date date2 = ISO8601Utils.parse("2019-11-01T12:00:00-08:00", pos);
        assertEquals(new GregorianCalendar(2019, 10, 31, 12, 0, -8 * 60 * 60 * 1000).getTime(), date2);
        assertEquals(pos.getIndex(), 19);

        try {
            ISO8601Utils.parse("invalid", pos);
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFormatParseRoundTrip() {
        for (String dateString : new String[]{"2019-10-31T12:00:00Z", "2019-10-31T12:00:00+02:00", "2019-11-01T19:00:00-08:00"}) {
            Date date1 = ISO8601Utils.parse(dateString, new ParsePosition(0));
            String formattedDate = ISO8601Utils.format(date1);
            assertEquals(dateString, formattedDate);
        }
    }
}