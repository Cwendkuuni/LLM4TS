package com.fasterxml.jackson.databind.util;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class StdDateFormatTest {

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(null);
        assertEquals(null, dateFormat.getTimeZone());
    }

    @Test
    public void testParseISO8601ValidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2021-10-20T15:30:45.123Z");
        assertEquals(1634730645123L, date.getTime());
    }

    @Test
    public void testParseRFC1123ValidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Wed, 20 Oct 2021 15:30:45 GMT");
        assertEquals(1634730645000L, date.getTime());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidDate");
    }

    @Test
    public void testFormatISO8601Date() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date(1634730645123L);
        StringBuffer buffer = new StringBuffer();
        dateFormat.format(date, buffer, null);
        assertEquals("2021-10-20T15:30:45.123Z", buffer.toString());
    }

    @Test
    public void testFormatRFC1123Date() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date(1634730645000L);
        StringBuffer buffer = new StringBuffer();
        dateFormat.format(date, buffer, null);
        assertEquals("Wed, 20 Oct 2021 15:30:45 GMT", buffer.toString());
    }

}