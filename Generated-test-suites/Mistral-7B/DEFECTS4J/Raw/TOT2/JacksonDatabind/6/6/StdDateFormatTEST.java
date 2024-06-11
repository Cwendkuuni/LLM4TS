package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

 // Test constructor
 @Test
 public void testStdDateFormatConstruction() {
 StdDateFormat dateFormat = new StdDateFormat();
 assertNotNull(dateFormat);
 }

 // Test getISO8601Format method with default timezone and locale
 @Test
 public void testGetISO8601FormatDefault() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format();
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", format.toLocaleString());
 }

 // Test getISO8601Format method with custom timezone and locale
 @Test
 public void testGetISO8601FormatCustom() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("Europe/London"), Locale.UK);
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", format.toLocaleString());
 assertEquals("Europe/London", format.getTimeZone().getID());
 }

 // Test parse method with valid ISO-8601 formatted date string
 @Test
 public void testParseValidISO8601DateString() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format();
 Date date = format.parse("2022-12-31T23:59:59.123Z");
 assertEquals(new Date(1671432399123L), date);
 }

 // Test parse method with valid RFC-1123 formatted date string
 @Test
 public void testParseValidRFC1123DateString() throws Exception {
 DateFormat format = StdDateFormat.getRFC1123Format();
 Date date = format.parse("Thu, 29 Dec 2022 23:59:59 GMT");
 assertEquals(new Date(1671432399000L), date);
 }

 // Test parse method with invalid date string
 @Test(expected = ParseException.class)
 public void testParseInvalidDateString() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format();
 format.parse("InvalidDateString");
 }

 // Test parse method with date string containing invalid timezone definition
 @Test(expected = ParseException.class)
 public void testParseInvalidTimezoneDateString() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format();
 format.parse("2022-12-31T23:59:59+XXX");
 }

 // Test format method with valid date object
 @Test
 public void testFormatValidDateObject() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format();
 Date date = new Date(1671432399123L);
 String formattedDate = format.format(date);
 assertEquals("2022-12-31T23:59:59.123Z", formattedDate);
 }

 // Test format method with null date object
 @Test
 public void testFormatNullDateObject() throws Exception {
 DateFormat format = StdDateFormat.getISO8601Format();
 String formattedDate = format.format(null);
 assertNull(formattedDate);
 }
}