
package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        //Given
        Date date = new Date();

        //When
        String result = ISO8601Utils.format(date);

        //Then
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
    }

    @Test
    public void testFormatWithMillis() {
        //Given
        Date date = new Date();

        //When
        String result = ISO8601Utils.format(date, true);

        //Then
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z"));
    }

    @Test
    public void testFormatWithTimeZone() {
        //Given
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+02:00");

        //When
        String result = ISO8601Utils.format(date, false, timeZone);

        //Then
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[+-]\\d{2}:\\d{2}"));
    }

    @Test
    public void testParse() throws ParseException {
        //Given
        String date = "2021-01-01T10:00:00Z";
        ParsePosition pos = new ParsePosition(0);

        //When
        Date result = ISO8601Utils.parse(date, pos);

        //Then
        assertNotNull(result);
        assertEquals(0, pos.getErrorIndex());
    }
}
