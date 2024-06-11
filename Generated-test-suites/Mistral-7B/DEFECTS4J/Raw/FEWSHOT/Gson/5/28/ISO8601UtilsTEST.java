package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(System.currentTimeMillis());
        String formattedDate = ISO8601Utils.format(date);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        assertEquals(expectedFormat, formattedDate.substring(0, expectedFormat.length()));

        String formattedDateWithoutMillis = ISO8601Utils.format(date, false, TimeZone.getDefault());
        expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        assertEquals(expectedFormat, formattedDateWithoutMillis.substring(0, expectedFormat.length()));

        Date dateUTC = new Date(System.currentTimeMillis() + (60 * 60 * 1000)); // 1 hour later in UTC
        String formattedDateUTC = ISO8601Utils.format(dateUTC, false, TimeZone.getTimeZone("UTC"));
        expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        assertEquals(expectedFormat, formattedDateUTC.substring(0, expectedFormat.length()));

        String formattedDateWithTimeZone = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/Los_Angeles"));
        expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'-07:00'";
        assertEquals(expectedFormat, formattedDateWithTimeZone.substring(0, expectedFormat.length()));

        String formattedDateWithMillisAndTimeZone = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
        expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'-04:00'";
        assertEquals(expectedFormat, formattedDateWithMillisAndTimeZone.substring(0, expectedFormat.length()));
    }

    @Test
    public void testParse() throws ParseException {
        String dateString1 = "2023-03-31T15:30:45.056Z";
        Date parsedDate1 = ISO8601Utils.parse(dateString1, new ParsePosition(0));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(parsedDate1);
        int expectedYear1 = calendar1.get(Calendar.YEAR);
        int expectedMonth1 = calendar1.get(Calendar.MONTH) + 1;
        int expectedDayOfMonth1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int expectedHours1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int expectedMinutes1 = calendar1.get(Calendar.MINUTE);
        int expectedSeconds1 = calendar1.get(Calendar.SECOND);
        int expectedMilliseconds1 = calendar1.get(Calendar.MILLISECOND);
        assertEquals(expectedYear1, 2023);
        assertEquals(expectedMonth1, 3);
        assertEquals(expectedDayOfMonth1, 31);
        assertEquals(expectedHours1, 15);
        assertEquals(expectedMinutes1, 30);
        assertEquals(expectedSeconds1, 45);
        assertEquals(expectedMilliseconds1, 56);

        String dateString2 = "2023-03-31T15:30:45";
        Date parsedDate2 = ISO8601Utils.parse(dateString2, new ParsePosition(0));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(parsedDate2);
        int expectedYear2 = calendar2.get(Calendar.YEAR);
        int expectedMonth2 = calendar2.get(Calendar.MONTH) + 1;
        int expectedDayOfMonth2 = calendar2.get(Calendar.DAY_OF_MONTH);
        int expectedHours2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int expectedMinutes2 = calendar2.get(Calendar.MINUTE);
        int expectedSeconds2 = calendar2.get(Calendar.SECOND);
        int expectedMilliseconds2 = 0;
        assertEquals(expectedYear2, 2023);
        assertEquals(expectedMonth2, 3);
        assertEquals(expectedDayOfMonth2, 31);
        assertEquals(expectedHours2, 15);
        assertEquals(expectedMinutes2, 30);
        assertEquals(expectedSeconds2, 45);
        assertEquals(expectedMilliseconds2, 0);

        String dateString3 = "2023-03-31";
        Date parsedDate3 = ISO8601Utils.parse(dateString3, new ParsePosition(0));
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(parsedDate3);
        int expectedYear3 = calendar3.get(Calendar.YEAR);
        int expectedMonth3 = calendar3.get(Calendar.MONTH) + 1;
        int expectedDayOfMonth3 = calendar3.get(Calendar.DAY_OF_MONTH);
        int expectedHours3 = 0;
        int expectedMinutes3 = 0;
        int expectedSeconds3 = 0;
        int expectedMilliseconds3 = 0;
        assertEquals(expectedYear3, 2023);
        assertEquals(expectedMonth3, 3);
        assertEquals(expectedDayOfMonth3, 31);
        assertEquals(expectedHours3, 0);
        assertEquals(expectedMinutes3, 0);
        assertEquals(expectedSeconds3, 0);
        assertEquals(expectedMilliseconds3, 0);

        String dateString4 = "2023-03-31T15:30:45.056+02:00";
        Date parsedDate4 = ISO8601Utils.parse(dateString4, new ParsePosition(0));
        Calendar calendar4 = Calendar.getInstance();
        calendar4.setTime(parsedDate4);
        int expectedYear4 = calendar4.get(Calendar.YEAR);
        int expectedMonth4 = calendar4.get(Calendar.MONTH) + 1;
        int expectedDayOfMonth4 = calendar4.get(Calendar.DAY_OF_MONTH);
        int expectedHours4 = calendar4.get(Calendar.HOUR_OF_DAY);
        int expectedMinutes4 = calendar4.get(Calendar.MINUTE);
        int expectedSeconds4 = calendar4.get(Calendar.SECOND);
        int expectedMilliseconds4 = calendar4.get(Calendar.MILLISECOND);
        int expectedOffsetHours4 = calendar4.get(Calendar.EXTRA);
        assertEquals(expectedYear4, 2023);
        assertEquals(expectedMonth4, 3);
        assertEquals(expectedDayOfMonth4, 31);
        assertEquals(expectedHours4, 17);
        assertEquals(expectedMinutes4, 30);
        assertEquals(expectedSeconds4, 45);
        assertEquals(expectedMilliseconds4, 56);
        assertEquals(expectedOffsetHours4, 2);

        String dateString5 = "2023-03-31T15:30:45.056-02:00";
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString5, new ParsePosition(0)));
    }
}