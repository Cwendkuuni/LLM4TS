package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() throws Exception {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date);
        String expected = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
        String regex = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
                "(19|20)\\d{2}", // year
                "(0[1-9]|1[0-2])", // month
                "(0[1-9]|[12][0-9]|3[01])", // day
                "T",
                "(0[0-9]|1[0-9]|2[0-3])", // hour
                ":",
                "(0[0-5]|[0-5][0-9])", // minute
                ":",
                "(0[0-5]|[0-5][0-9])", // second
                ".",
                "(0[0-9]|[0-5][0-9]|[0-9]{3})", // millisecond
                "Z" // timezone
        );
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expected);
        matcher.find();
        for (int i = 0; i < matcher.groupCount(); i++) {
            String group = matcher.group(i);
            int value = Integer.parseInt(ISO8601Utils.format(date, false, TimeZone.getDefault()).substring(
                    ISO8601Utils.format(date, false, TimeZone.getDefault()).indexOf(group),
                    ISO8601Utils.format(date, false, TimeZone.getDefault()).indexOf(group) + group.length()).replaceAll("\\D", ""));
            assertEquals(value, Integer.parseInt(group));
        }
        assertEquals(formatted, expected);
    }

    @Test
    public void testFormatDateWithMillis() throws Exception {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true);
        String expected = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
        String regex = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
                "(19|20)\\d{2}", // year
                "(0[1-9]|1[0-2])", // month
                "(0[1-9]|[12][0-9]|3[01])", // day
                "T",
                "(0[0-9]|1[0-9]|2[0-3])", // hour
                ":",
                "(0[0-5]|[0-5][0-9])", // minute
                ":",
                "(0[0-5]|[0-5][0-9])", // second
                ".",
                "(0[0-9]|[0-5][0-9]|[0-9]{3})", // millisecond
                "Z" // timezone
        );
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expected);
        matcher.find();
        for (int i = 0; i < matcher.groupCount(); i++) {
            String group = matcher.group(i);
            int value;
            if (group.length() == 3) {
                value = Integer.parseInt(ISO8601Utils.format(date, true).substring(
                        ISO8601Utils.format(date, true).indexOf(group),
                        ISO8601Utils.format(date, true).indexOf(group) + group.length()).replaceAll("\\D", ""));
                assertEquals(value, Integer.parseInt(group) * 1000);
            } else {
                value = Integer.parseInt(ISO8601Utils.format(date, true).substring(
                        ISO8601Utils.format(date, true).indexOf(group),
                        ISO8601Utils.format(date, true).indexOf(group) + group.length()).replaceAll("\\D", ""));
                assertEquals(value, Integer.parseInt(group));
            }
        }
        assertEquals(formatted, expected);
    }

    @Test
    public void testFormatDateWithTz() throws Exception {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String expected = "yyyy-MM-dd'T'hh:mm:ss'Z'";
        String regex = String.format("%s%s%s%s%s%s%s%s%s%s%s%s",
                "(19|20)\\d{2}", // year
                "(0[1-9]|1[0-2])", // month
                "(0[1-9]|[12][0-9]|3[01])", // day
                "T",
                "(0[0-9]|1[0-9]|2[0-3])", // hour
                ":",
                "(0[0-5]|[0-5][0-9])", // minute
                ":",
                "(0[0-5]|[0-5][0-9])", // second
                "'Z'" // timezone
        );
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expected);
        matcher.find();
        for (int i = 0; i < matcher.groupCount(); i++) {
            String group = matcher.group(i);
            int value;
            if (group.equals("'Z'")) {
                assertEquals("UTC", TimeZone.getDefault().getID());
            } else {
                value = Integer.parseInt(ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC")).substring(
                        ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC")).indexOf(group),
                        ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC")).indexOf(group) + group.length()).replaceAll("\\D", ""));
                assertEquals(value, Integer.parseInt(group));
            }
        }
        assertEquals(formatted, expected);
    }

    @Test
    public void testFormatDateWithOffsetTz() throws Exception {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 60);
        TimeZone timeZone = new TimeZone() {
            @Override
            public String getID() {
                return "CustomTimeZone";
            }

            @Override
            public String getDisplayName(boolean isShort, Locale locale) {
                return null;
            }

            @Override
            public int getRawOffset() {
                return 60 * 60 * 1000;
            }

            @Override
            public boolean useDaylightTime() {
                return false;
            }
        };
        String formatted = ISO8601Utils.format(date, false, timeZone);
        String expected = "yyyy-MM-dd'T'hh:mm:ss'CustomTimeZone'";
        String regex = String.format("%s%s%s%s%s%s%s%s%s%s%s%s",
                "(19|20)\\d{2}", // year
                "(0[1-9]|1[0-2])", // month
                "(0[1-9]|[12][0-9]|3[01])", // day
                "T",
                "(0[0-9]|1[0-9]|2[0-3])", // hour
                ":",
                "(0[0-5]|[0-5][0-9])", // minute
                ":",
                "(0[0-5]|[0-5][0-9])", // second
                "'CustomTimeZone'" // timezone
        );
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expected);
        matcher.find();
        for (int i = 0; i < matcher.groupCount(); i++) {
            String group = matcher.group(i);
            int value;
            if (group.equals("'CustomTimeZone'")) {
                assertEquals(timeZone.getID(), "CustomTimeZone");
            } else {
                value = Integer.parseInt(ISO8601Utils.format(date, false, timeZone).substring(
                        ISO8601Utils.format(date, false, timeZone).indexOf(group),
                        ISO8601Utils.format(date, false, timeZone).indexOf(group) + group.length()).replaceAll("\\D", ""));
                assertEquals(value, Integer.parseInt(group));
            }
        }
        assertEquals(formatted, expected);
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2022-08-23T14:30:45.123Z";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        assertEquals(2022, year);
        assertEquals(8, month);
        assertEquals(23, dayOfMonth);
        assertEquals(14, hourOfDay);
        assertEquals(30, minute);
        assertEquals(45, second);
        assertEquals(123, millisecond);
    }

    @Test
    public void testParseDateWithOffsetTz() throws ParseException {
        String dateString = "2022-08-23T14:30:45+02:00";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        assertEquals(2022, year);
        assertEquals(8, month);
        assertEquals(23, dayOfMonth);
        assertEquals(16, hourOfDay);
        assertEquals(30, minute);
        assertEquals(45, second);
        assertEquals(0, millisecond);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        String dateString = "2022-08-23T14:30:45.123 invalid";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingTimeZone() throws ParseException {
        String dateString = "2022-08-23T14:30:45";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimeZone() throws ParseException {
        String dateString = "2022-08-23T14:30:45invalid";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingYear() throws ParseException {
        String dateString = "-08-23T14:30:45.123Z";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingMonth() throws ParseException {
        String dateString = "2022-08-03T14:30:45.123Z";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingDay() throws ParseException {
        String dateString = "2022-08-03T14:30:45.123Z";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingHour() throws ParseException {
        String dateString = "2022-08-23T.123Z";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingMinute() throws ParseException {
        String dateString = "2022-08-23T14.";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingSecond() throws ParseException {
        String dateString = "2022-08-23T14:.";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingMillisecond() throws ParseException {
        String dateString = "2022-08-23T14:30.";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }
}