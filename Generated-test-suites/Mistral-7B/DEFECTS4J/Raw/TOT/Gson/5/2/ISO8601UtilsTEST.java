package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 *
 * @author Johnson.D'Cruz
 */
public class ISO8601UtilsTest
{

    @Test
    public void testFormatUTCDate()
    {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        String expected = "20" + new SimpleDateFormat("yy").format(date) + "-"
                + new SimpleDateFormat("MM").format(date) + "-"
                + new SimpleDateFormat("dd").format(date) + "T"
                + new SimpleDateFormat("HH:mm:ss").format(date) + "Z";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatDateWithMillis()
    {
        Date date = new Date(System.currentTimeMillis());
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        String expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatDateWithTimeZone()
    {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Asia/Calcutta"));
        String expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+05:30'").format(date);
        assertEquals(expected, formattedDate);
    }

    @Test(expected = NullPointerException.class)
    public void testFormatNullDate()
    {
        ISO8601Utils.format(null);
    }

    @Test
    public void testParseDateUTC() throws ParseException
    {
        Date date = ISO8601Utils.parse("2019-12-31T23:59:59Z", new ParsePosition(0));
        assertEquals(new Date(1573600000000L), date);
    }

    @Test
    public void testParseDateWithMillisUTC() throws ParseException
    {
        Date date = ISO8601Utils.parse("2019-12-31T23:59:59.123Z", new ParsePosition(0));
        assertEquals(new Date(1573600059123L), date);
    }

    @Test
    public void testParseDateWithTimeZone() throws ParseException
    {
        Date date = ISO8601Utils.parse("2019-12-31T23:59:59+05:30", new ParsePosition(0));
        assertEquals(new Date(1573600719000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormat() throws ParseException
    {
        ISO8601Utils.parse("InvalidDateFormat", new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOffset() throws ParseException
    {
        ISO8601Utils.parse("2019-12-31T23:59:59InvalidOffset", new ParsePosition(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseDateWithInvalidCharacter() throws ParseException
    {
        ISO8601Utils.parse("2019-12-31T23:59:59#", new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithNegativeTimeZone() throws ParseException
    {
        ISO8601Utils.parse("2019-12-31T23:59:59-05:30", new ParsePosition(0));
    }
}