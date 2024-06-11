package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse("2022-12-31T23:59:59Z", pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse("2022-12", pos);
    }
}