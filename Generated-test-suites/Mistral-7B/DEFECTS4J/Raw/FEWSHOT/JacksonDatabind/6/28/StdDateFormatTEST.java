package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        for (String dateString : new String[]{"2022-12-31", "2022-12-31T23:59:59.000Z", "2022-12-31T23:59:59.000+01:00"}) {
            Date date = null;
            ParsePosition parsePosition = new ParsePosition(0);
            try {
                date = dateFormat.parse(dateString, parsePosition);
                assertEquals("Expected parse to succeed for: " + dateString, date, new Date(dateString.substring(0, 19).replace("-", "/").replaceFirst("T|\\+|-", " ").getBytes()));
            } catch (ParseException e) {
                fail("Expected parse to succeed for: " + dateString);
            }
        }
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        for (String dateString : new String[]{"Mon, 31 Dec 2022 23:59:59 GMT"}) {
            Date date = null;
            ParsePosition parsePosition = new ParsePosition(0);
            try {
                date = dateFormat.parse(dateString, parsePosition);
                assertEquals("Expected parse to succeed for: " + dateString, date, new Date(dateString.substring(0, 19).replace(",", "").replaceFirst(" ", "T").replace("GMT", "Z").getBytes()));
            } catch (ParseException e) {
                fail("Expected parse to succeed for: " + dateString);
            }
        }
    }

    @Test
    public void testParsePlain() {
        StdDateFormat dateFormat = new StdDateFormat();
        for (String dateString : new String[]{"2022-12-31"}) {
            Date date = null;
            ParsePosition parsePosition = new ParsePosition(0);
            try {
                date = dateFormat.parse(dateString, parsePosition);
                assertEquals("Expected parse to succeed for: " + dateString, date, new Date(dateString.getBytes()));
            } catch (ParseException e) {
                fail("Expected parse to succeed for: " + dateString);
            }
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalid() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "InvalidDate";
        Date date = null;
        ParsePosition parsePosition = new ParsePosition(0);
        dateFormat.parse(dateString, parsePosition);
        fail("Expected parse to fail for: " + dateString);
    }
}