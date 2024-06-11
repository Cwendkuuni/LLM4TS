package com.fasterxml.jackson.databind.util;
import org.junit.Test;
import java.text.*;
import java.util.*;

public class StdDateFormatTest {

    @Test
    public void testParse() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        String dateStr = "2021-01-01T12:00:00.000Z";
        Date expected = format.parse(dateStr);
        Date actual = new Date(1609502400000L);
        assert(actual.equals(expected));
    }

    @Test
    public void testFormat() {
        StdDateFormat format = new StdDateFormat();
        Date date = new Date(1609502400000L);
        StringBuffer expected = new StringBuffer("2021-01-01T12:00:00.000Z");
        StringBuffer actual = format.format(date, new StringBuffer(), new FieldPosition(0));
        assert(expected.toString().equals(actual.toString()));
    }
}