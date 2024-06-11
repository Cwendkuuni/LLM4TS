package com.fasterxml.jackson.databind.util;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 @Test
 public void testParseISO8601() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-12-31T23:59:59.123Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = dateFormat.parse(dateString, pos);
 assertEquals("Incorrect date parsed from ISO8601 format", new Date(1671321199123L), parsedDate);
 }

 @Test
 public void testParseISO8601WithTimezoneOffset() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-12-31T23:59:59.123+01:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = dateFormat.parse(dateString, pos);
 assertEquals("Incorrect date parsed from ISO8601 format with timezone offset", new Date(1671337799123L), parsedDate);
 }

 @Test
 public void testParseISO8601WithPlainDate() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-12-31";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = dateFormat.parse(dateString, pos);
 assertNotNull("Date should not be null", parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseRFC1123() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "Thu, 31 Dec 2022 23:59:59 GMT";
 ParsePosition pos = new ParsePosition(0);
 dateFormat.parse(dateString, pos);
 fail("ParseException should be thrown for RFC1123 format");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidString() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "invalidString";
 ParsePosition pos = new ParsePosition(0);
 dateFormat.parse(dateString, pos);
 fail("ParseException should be thrown for invalid string");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTimezoneNull() {
 StdDateFormat dateFormat = new StdDateFormat();
 try {
 dateFormat.withTimeZone(null);
 fail("IllegalArgumentException should be thrown for null timezone");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testClone() {
 StdDateFormat original = new StdDateFormat();
 StdDateFormat cloned = original.clone();
 assertNotSame("Cloned instance should not be the same as original", original, cloned);
 }
}