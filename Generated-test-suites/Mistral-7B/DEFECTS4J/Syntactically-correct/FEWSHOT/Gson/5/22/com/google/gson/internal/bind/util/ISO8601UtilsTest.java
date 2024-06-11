package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        System.out.println(formattedDate);

        Date dateWithMillis = new Date(System.currentTimeMillis());
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, true, TimeZone.getDefault());
        System.out.println(formattedDateWithMillis);

        Date dateWithOffset = new Date(System.currentTimeMillis() + 60 * 60 * 1000); // 1 hour
        String formattedDateWithOffset = ISO8601Utils.format(dateWithOffset, true, TimeZone.getTimeZone("Europe/London"));
        System.out.println(formattedDateWithOffset);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2023-03-31T14:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(2023, parsedDate.getYear());
        assertEquals(3, parsedDate.getMonth());
        assertEquals(31, parsedDate.getDate());
        assertEquals(14, parsedDate.getHours());
        assertEquals(30, parsedDate.getMinutes());
        assertEquals(45, parsedDate.getSeconds());
        assertEquals(0, parsedDate.getMilliseconds());
        assertEquals(0, parsedDate.getTimeZone().getOffset(parsedDate.getTime()));

        String dateStringWithMillis = "2023-03-31T14:30:45.123Z";
        pos = new ParsePosition(0);
        Date parsedDateWithMillis = ISO8601Utils.parse(dateStringWithMillis, pos);
        assertEquals(2023, parsedDateWithMillis.getYear());
        assertEquals(3, parsedDateWithMillis.getMonth());
        assertEquals(31, parsedDateWithMillis.getDate());
        assertEquals(14, parsedDateWithMillis.getHours());
        assertEquals(30, parsedDateWithMillis.getMinutes());
        assertEquals(45, parsedDateWithMillis.getSeconds());
        assertEquals(123, parsedDateWithMillis.getMilliseconds());
        assertEquals(0, parsedDateWithMillis.getTimeZone().getOffset(parsedDateWithMillis.getTime()));

        String dateStringWithOffset = "2023-03-31T14:30:45+01:00";
        pos = new ParsePosition(0);
        Date parsedDateWithOffset = ISO8601Utils.parse(dateStringWithOffset, pos);
        assertEquals(2023, parsedDateWithOffset.getYear());
        assertEquals(3, parsedDateWithOffset.getMonth());
        assertEquals(31, parsedDateWithOffset.getDate());
        assertEquals(14, parsedDateWithOffset.getHours());
        assertEquals(30, parsedDateWithOffset.getMinutes());
        assertEquals(45, parsedDateWithOffset.getSeconds());
        assertEquals(0, parsedDateWithOffset.getMilliseconds());
        assertEquals(60 * 60 * 1000, parsedDateWithOffset.getTimeZone().getOffset(parsedDateWithOffset.getTime()));

        String dateStringWithInvalidFormat = "2023-03-31T14:30:45+01:60";
        pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStringWithInvalidFormat, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        String dateStringWithMissingTime = "2023-03-31";
        pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStringWithMissingTime, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

}