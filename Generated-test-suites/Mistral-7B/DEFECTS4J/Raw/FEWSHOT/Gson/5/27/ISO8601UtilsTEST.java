package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 12).getTime();

        // Test with default timezone and no milliseconds precision
        assertEquals("2023-01-12T00:00:00Z", ISO8601Utils.format(date));

        // Test with GMT timezone and milliseconds precision
        assertEquals("2023-01-12T00:00:12.345Z", ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT")));

        // Test with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        assertEquals("2023-01-12T05:00:12.345-05:00", ISO8601Utils.format(date, true, customTimeZone));
    }

    @Test
    public void testParse() {
        String validDate = "2023-01-12T05:00:12.345-05:00";
        ParsePosition pos = new ParsePosition(0);

        // Test parsing with default timezone and no milliseconds precision
        Date parsedDate = ISO8601Utils.parse(validDate, pos);
        assertEquals(new GregorianCalendar(2023, Calendar.JANUARY, 12).getTime(), parsedDate);
        assertEquals(0, pos.getIndex());

        // Test parsing with GMT timezone and milliseconds precision
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(validDate, pos);
        assertEquals(new GregorianCalendar(2023, Calendar.JANUARY, 12, 5, 0, 12, 345).getTime(), parsedDate);
        assertEquals(validDate.length(), pos.getIndex());

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(validDate, pos, customTimeZone);
        Calendar calendar = new GregorianCalendar(customTimeZone);
        calendar.setTime(parsedDate);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(5, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(12, calendar.get(Calendar.SECOND));
        assertEquals(345, calendar.get(Calendar.MILLISECOND));
        assertEquals(validDate.length(), pos.getIndex());

        // Test parsing invalid date
        String invalidDate = "2023-01-13T25:60:00"; // Invalid hour and minute
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDate, pos));
        assertEquals("Text '2023-01-13T25:60:00' could not be parsed at index 16", exception.getMessage());

        // Test parsing invalid date with custom timezone
        invalidDate = "2023-01-13T25:60:00-05:00"; // Invalid hour and minute but with timezone
        exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDate, pos, customTimeZone));
        assertEquals("Text '2023-01-13T25:60:00-05:00' could not be parsed at index 16", exception.getMessage());

        // Test parsing invalid date with no time component and no time zone
        invalidDate = "2023-01-13"; // Invalid date with no time component and no time zone
        exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDate, pos));
        assertEquals("Text '2023-01-13' could not be parsed at index 6", exception.getMessage());

        // Test parsing date without year
        String noYearDate = "01-12-2023T05:00:12.345-05:00"; // Missing year
        exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(noYearDate, pos, customTimeZone));
        assertEquals("Text '01-12-2023T05:00:12.345-05:00' could not be parsed at index 4", exception.getMessage());
    }

    @Test
    public void testFormatAndParseRoundTrip() {
        for (int i = 0; i < 10; i++) {
            String randomDateString = generateRandomISO8601DateString();
            Date date = ISO8601Utils.parse(randomDateString, new ParsePosition(0));
            String formattedDate = ISO8601Utils.format(date);
            assertEquals(randomDateString, formattedDate);
        }
    }

    @Test
    public void testCheckOffset() {
        String dateString = "2023-01-12T05:00:12.345-05:00";

        // Test with expected character
        assertTrue(ISO8601Utils.checkOffset(dateString, 0, 'T'));
        assertTrue(ISO8601Utils.checkOffset(dateString, 13, '-'));
        assertTrue(ISO8601Utils.checkOffset(dateString, 23, ':'));

        // Test with unexpected character
        assertFalse(ISO8601Utils.checkOffset(dateString, 2, 'A'));

        // Test with invalid offset
        assertFalse(ISO8601Utils.checkOffset(dateString, dateString.length(), 'Z'));
    }

    @Test
    public void testParseInt() {
        String dateString = "2023-01-12";

        // Test valid integer
        assertEquals(2023, ISO8601Utils.parseInt(dateString, 0, 6));

        // Test invalid integer length
        assertThrows(NumberFormatException.class, () -> ISO8601Utils.parseInt(dateString, 0, 5));

        // Test invalid integer index
        assertThrows(NumberFormatException.class, () -> ISO8601Utils.parseInt(dateString, 7, 6));
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, 123, 4);
        assertEquals("123", buffer.toString());

        ISO8601Utils.padInt(buffer, 123, 5);
        assertEquals("0123", buffer.toString());

        ISO8601Utils.padInt(buffer, 123, 3);
        assertEquals("123", buffer.toString());

        ISO8601Utils.padInt(buffer, -123, 5);
        assertEquals("-00123", buffer.toString());
    }

    @Test
    public void testIndexOfNonDigit() {
        String nonDigitDate = "2023-01-12T05:00:12.345-05:00a"; // Invalid character at the end
        int index = ISO8601Utils.indexOfNonDigit(nonDigitDate, 0);
        assertEquals(nonDigitDate.length(), index);

        index = ISO8601Utils.indexOfNonDigit(nonDigitDate, 1);
        assertEquals(5, index);
    }

    private String generateRandomISO8601DateString() {
        String[] years = {"2022", "2023", "2024", "2025", "2026"};
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] days = {"01", "12", "23", "31"};
        String[] hours = {"00", "23"};
        String[] minutes = {"00", "59"};
        String[] seconds = {"00", "59", "59"};
        String[] milliseconds = {"000", "999", "999"};
        String[] timeZones = {"Z", "+01:00", "-05:00"};

        int yearIndex = (int) (Math.random() * years.length);
        int monthIndex = (int) (Math.random() * months.length);
        int dayIndex = (int) (Math.random() * days.length);
        int hourIndex = (int) (Math.random() * hours.length);
        int minuteIndex = (int) (Math.random() * minutes.length);
        int secondIndex = (int) (Math.random() * seconds.length);
        int millisecondIndex = (int) (Math.random() * milliseconds.length);
        int timeZoneIndex = (int) (Math.random() * timeZones.length);

        StringBuilder dateBuilder = new StringBuilder();
        dateBuilder.append(years[yearIndex]);
        dateBuilder.append("-");
        dateBuilder.append(months[monthIndex]);
        dateBuilder.append("-");
        dateBuilder.append(days[dayIndex]);
        dateBuilder.append("T");
        dateBuilder.append(hours[hourIndex]);
        dateBuilder.append(":");
        dateBuilder.append(minutes[minuteIndex]);
        dateBuilder.append(":");
        dateBuilder.append(seconds[secondIndex]);
        if (millisecondIndex > 0) {
            dateBuilder.append(".");
            dateBuilder.append(milliseconds[millisecondIndex]);
        }
        dateBuilder.append(timeZones[timeZoneIndex]);

        return dateBuilder.toString();
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            fail();
        }
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            fail();
        }
    }
}