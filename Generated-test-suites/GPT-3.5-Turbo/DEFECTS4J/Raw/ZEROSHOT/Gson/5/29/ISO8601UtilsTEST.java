package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test case 1: Date formatted as 'yyyy-MM-ddThh:mm:ssZ'
        Date date1 = createDate(2022, 1, 1, 12, 0, 0);
        String expected1 = "2022-01-01T12:00:00Z";
        String result1 = ISO8601Utils.format(date1);
        Assert.assertEquals(expected1, result1);

        // Test case 2: Date formatted as 'yyyy-MM-ddThh:mm:ss.sssZ'
        Date date2 = createDate(2022, 1, 1, 12, 0, 0);
        String expected2 = "2022-01-01T12:00:00.000Z";
        String result2 = ISO8601Utils.format(date2, true);
        Assert.assertEquals(expected2, result2);

        // Test case 3: Date formatted as 'yyyy-MM-ddThh:mm:ss[.sss]Z'
        Date date3 = createDate(2022, 1, 1, 12, 0, 0);
        String expected3 = "2022-01-01T12:00:00Z";
        String result3 = ISO8601Utils.format(date3, false);
        Assert.assertEquals(expected3, result3);
    }

    @Test
    public void testParse() throws ParseException {
        // Test case 1: Parsing date with format 'yyyy-MM-ddThh:mm:ssZ'
        String dateString1 = "2022-01-01T12:00:00Z";
        Date expected1 = createDate(2022, 1, 1, 12, 0, 0);
        ParsePosition pos1 = new ParsePosition(0);
        Date result1 = ISO8601Utils.parse(dateString1, pos1);
        Assert.assertEquals(expected1, result1);

        // Test case 2: Parsing date with format 'yyyy-MM-ddThh:mm:ss.sssZ'
        String dateString2 = "2022-01-01T12:00:00.000Z";
        Date expected2 = createDate(2022, 1, 1, 12, 0, 0);
        ParsePosition pos2 = new ParsePosition(0);
        Date result2 = ISO8601Utils.parse(dateString2, pos2);
        Assert.assertEquals(expected2, result2);

        // Test case 3: Parsing date with format 'yyyy-MM-ddThh:mm:ssZ' with milliseconds
        String dateString3 = "2022-01-01T12:00:00.765Z";
        Date expected3 = createDate(2022, 1, 1, 12, 0, 0);
        ParsePosition pos3 = new ParsePosition(0);
        Date result3 = ISO8601Utils.parse(dateString3, pos3);
        Assert.assertEquals(expected3, result3);
    }

    private Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}