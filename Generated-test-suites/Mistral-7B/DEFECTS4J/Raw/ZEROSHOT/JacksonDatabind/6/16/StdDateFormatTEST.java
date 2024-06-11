package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    private static final DateFormat instance = StdDateFormat.instance;

    @Test
    public void testParseISO8601() throws Exception {
        String[] inputs = {
                "2012-12-31T23:59:59.999Z",
                "2012-12-31T23:59:59.999-05:00",
                "2012-12-31T23:59:59.999+08:00",
                "2012-12-31T23:59:59.999+08:00:00",
                "2012-12-31T23:59:59.999Z07:00"
        };

        for (String input : inputs) {
            Calendar expected = new GregorianCalendar(2012, 11, 31, 23, 59, 59);
            expected.add(Calendar.MILLISECOND, 999);
            expected.add(Calendar.HOUR_OF_DAY, getTimeZoneOffset(input) / 60);
            Date actual = instance.parse(input);
            assertEquals(expected.getTimeInMillis(), actual.getTime());
        }
    }

    @Test
    public void testParseRFC1123() throws Exception {
        String[] inputs = {
                "Mon, 31 Dec 2012 23:59:59 GMT",
                "Mon, 31 Dec 2012 23:59:59 EST",
                "Mon, 31 Dec 2012 23:59:59 PST"
        };

        for (String input : inputs) {
            Calendar expected = new GregorianCalendar(2012, 11, 31, 23, 59, 59);
            TimeZone timeZone = getTimeZoneForID(input.substring(input.lastIndexOf(" ")).trim());
            expected.setTimeZone(timeZone);
            Date actual = instance.parse(input);
            assertEquals(expected.getTimeInMillis(), actual.getTime());
        }
    }

    @Test
    public void testParseInvalid() throws Exception {
        String[] inputs = {
                "2012-12-31T23:59:59 invalid",
                "Mon, 31 Dec 2012 23:59:59 invalid"
        };

        for (String input : inputs) {
            try {
                instance.parse(input);
                fail("Expected ParseException");
            } catch (ParseException e) {
                // expected
            }
        }
    }

    @Test
    public void testFormat() throws Exception {
        Date date = new Date(1357041699000L);

        String[] formats = { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "EEE, dd MMM yyyy HH:mm:ss zzz" };
        String[] expected = { "2012-12-31T23:59:59.000Z", "Mon, 31 Dec 2012 23:59:59 GMT" };

        for (int i = 0; i < formats.length; i++) {
            String output = instance.format(date, new StringBuffer(), null);
            assertEquals(expected[i], output);
        }
    }

    @Test
    public void testClone() throws Exception {
        DateFormat org = new StdDateFormat();
        DateFormat cloned = org.clone();
        assertEquals(org, cloned);

        String input = "Mon, 31 Dec 2012 23:59:59 EST";
        assertEquals(instance.parse(input).getTime(), org.parse(input).getTime());
        assertEquals(instance.parse(input).getTime(), cloned.parse(input).getTime());
    }

    private int getTimeZoneOffset(String input) {
        int offset = 0;
        if (input.charAt(input.length() - 3) == ':') {
            offset = Integer.parseInt(input.substring(input.length() - 5).trim());
        } else {
            offset = (input.charAt(input.length() - 3) == '+' ? 1 : -1) * 60 * Integer.parseInt(input.substring(input.length() - 5).trim());
        }
        return offset;
    }

    private TimeZone getTimeZoneForID(String id) {
        return TimeZone.getTimeZone(id);
    }
}