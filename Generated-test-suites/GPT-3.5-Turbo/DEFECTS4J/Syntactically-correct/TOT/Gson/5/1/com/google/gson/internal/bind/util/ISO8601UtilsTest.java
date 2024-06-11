package com.google.gson.internal.bind.util;

import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    private Date date;
    private String dateString;

    @Before
    public void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.MARCH, 15, 12, 30, 45);
        date = calendar.getTime();
        dateString = "2022-03-15T12:30:45Z";
    }

    @Test
    public void testFormat_defaultTimezone_noMilliseconds() {
        String expected = "2022-03-15T12:30:45Z";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_GMT_Timezone_includeMilliseconds() {
        String expected = "2022-03-15T12:30:45.000Z";
        String result = ISO8601Utils.format(date, true);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_validDateString() throws ParseException {
        ParsePosition position = new ParsePosition(0);
        Date expected = date;
        Date result = ISO8601Utils.parse(dateString, position);
        assertEquals(expected, result);
        assertEquals(dateString.length(), position.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidDateString() throws ParseException {
        String invalidDateString = "2022-03-15T12:30:45";
        ParsePosition position = new ParsePosition(0);
        ISO8601Utils.parse(invalidDateString, position);
    }

}