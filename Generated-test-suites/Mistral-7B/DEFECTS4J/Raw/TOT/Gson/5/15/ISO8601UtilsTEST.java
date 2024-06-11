package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    @Test
    public void testFormat() {
        Date date = new Date(1633041200000L); // Thu, 16 Sep 2021 10:00:00 GMT
        String expected = "2021-09-16T10:00:00Z";

        String actual = ISO8601Utils.format(date, UTC);
        Assert.assertEquals(expected, actual);

        date = new Date(1633041200000L + 3600000); // Thu, 16 Sep 2021 11:00:00 GMT
        expected = "2021-09-16T11:00:00Z";

        actual = ISO8601Utils.format(date, UTC);
        Assert.assertEquals(expected, actual);

        date = new Date(1633041200000L - 86400000); // Thu, 15 Sep 2021 10:00:00 GMT
        expected = "2021-09-15T10:00:00Z";

        actual = ISO8601Utils.format(date, UTC);
        Assert.assertEquals(expected, actual);

        date = new Date(1633041200000L + 86400000); // Fri, 17 Sep 2021 10:00:00 GMT
        expected = "2021-09-17T10:00:00Z";

        actual = ISO8601Utils.format(date, UTC);
        Assert.assertEquals(expected, actual);

        date = new Date(1633041200000L + 3600000 + 30 * 60000); // Fri, 17 Sep 2021 11:30:00 GMT
        expected = "2021-09-17T11:30:00Z";

        actual = ISO8601Utils.format(date, UTC);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(1633041200001L); // Thu, 16 Sep 2021 10:00:01 GMT
        String expected = "2021-09-16T10:00:01.001Z";

        String actual = ISO8601Utils.format(date, UTC, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithOffset() {
        Date date = new Date(1633041200000L); // Thu, 16 Sep 2021 10:00:00 GMT
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");

        String expected = "2021-09-16T11:00:00+02:00";

        String actual = ISO8601Utils.format(date, timeZone);
        Assert.assertEquals(expected, actual);

        expected = "2021-09-16T10:00:00-01:00";

        actual = ISO8601Utils.format(date, TimeZone.getTimeZone("America/New_York"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        String dateString = "2021-09-16T10:00:00Z";
        Date expected = new Date(1633041200000L); // Thu, 16 Sep 2021 10:00:00 GMT

        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(dateString.length(), pos.getIndex());

        dateString = "2021-09-16T11:00:00+02:00";
        expected = new Date(1633041260000L); // Fri, 17 Sep 2021 11:00:00 CEST

        actual = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(dateString.length(), pos.getIndex());

        dateString = "2021-09-16T10:00:00-01:00";
        expected = new Date(1632954800000L); // Thu, 16 Sep 2021 09:00:00 EDT

        actual = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(dateString.length(), pos.getIndex());

        dateString = "2021-09-16T10:00:01.523+02:00";
        expected = new Date(1633041201523L); // Fri, 17 Sep 2021 11:00:01.523 CEST

        actual = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(dateString.length(), pos.getIndex());

        dateString = "2021-09-16T10:00:01.523-01:00";
        expected = new Date(1632954758523L); // Thu, 16 Sep 2021 09:00:01.523 EDT

        actual = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseWithInvalidFormat() {
        String dateString = "2021-09-16T10:00:00-01";
        ParsePosition pos = new ParsePosition(0);

        try {
            ISO8601Utils.parse(dateString, pos);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        Assert.assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidZone() {
        String dateString = "2021-09-16T10:00:00+02:60";
        ParsePosition pos = new ParsePosition(0);

        try {
            ISO8601Utils.parse(dateString, pos);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        Assert.assertEquals(14, pos.getIndex());
    }

    @Test
    public void testFormatAndParseRoundTrip() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 9, 16, 10, 0, 0);
        ZoneId zoneId = ZoneId.systemDefault();

        String formatted = ISO8601Utils.format(Date.from(dateTime.atZone(zoneId).toInstant()), UTC);

        Date parsed = ISO8601Utils.parse(formatted, new ParsePosition(0));
        LocalDateTime parsedDateTime = LocalDateTime.from(parsed.toInstant().atZone(ZoneId.systemDefault()).toZonedDateTime());

        Assert.assertEquals(dateTime, parsedDateTime);
    }

    @Test
    public void testFormatAndParseRoundTripWithMillis() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 9, 16, 10, 0, 523);
        ZoneId zoneId = ZoneId.systemDefault();

        String formatted = ISO8601Utils.format(Date.from(dateTime.atZone(zoneId).toInstant()), UTC, true);

        ParsePosition pos = new ParsePosition(0);
        Date parsed = ISO8601Utils.parse(formatted, pos);

        Assert.assertEquals(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()), parsed);
        Assert.assertEquals(formatted.length(), pos.getIndex());
    }

    @Test
    public void testFormatAndParseRoundTripWithOffset() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 9, 16, 10, 0, 0);
        ZoneId zoneId = ZoneId.of("Europe/Paris");

        String formatted = ISO8601Utils.format(Date.from(dateTime.atZone(zoneId).toInstant()), TimeZone.getTimeZone("Europe/Paris"));

        ParsePosition pos = new ParsePosition(0);
        Date parsed = ISO8601Utils.parse(formatted, pos);

        Assert.assertEquals(Date.from(dateTime.atZone(ZoneId.of("Europe/Paris")).toInstant()), parsed);
        Assert.assertEquals(formatted.length(), pos.getIndex());
    }

    @Test
    public void testFormatAndParseRoundTripWithOffsetAndMillis() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 9, 16, 10, 0, 523);
        ZoneId zoneId = ZoneId.of("Europe/Paris");

        String formatted = ISO8601Utils.format(Date.from(dateTime.atZone(zoneId).toInstant()), TimeZone.getTimeZone("Europe/Paris"), true);

        ParsePosition pos = new ParsePosition(0);
        Date parsed = ISO8601Utils.parse(formatted, pos);

        Assert.assertEquals(Date.from(dateTime.atZone(ZoneId.of("Europe/Paris")).toInstant()), parsed);
        Assert.assertEquals(formatted.length(), pos.getIndex());
    }
}