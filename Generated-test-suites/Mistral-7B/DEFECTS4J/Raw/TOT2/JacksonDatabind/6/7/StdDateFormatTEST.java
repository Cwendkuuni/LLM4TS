package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

 // Test the constructor
 @Test
 public void testStdDateFormatConstruction() {
 StdDateFormat dateFormat = new StdDateFormat();
 assertNotNull(dateFormat);
 }

 // Test getting the standard format instances
 @Test
 public void testGetBlueprintISO8601Format() {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 assertNotNull(dateFormat);
 assertTrue(dateFormat instanceof SimpleDateFormat);
 }

 @Test
 public void testGetISO8601Format() {
 TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
 Locale locale = new Locale("en", "GB");
 DateFormat dateFormat = StdDateFormat.getISO8601Format(timeZone, locale);
 assertNotNull(dateFormat);
 assertTrue(dateFormat instanceof SimpleDateFormat);
 }

 @Test
 public void testGetRFC1123Format() {
 TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
 Locale locale = new Locale("en", "GB");
 DateFormat dateFormat = StdDateFormat.getRFC1123Format(timeZone, locale);
 assertNotNull(dateFormat);
 assertTrue(dateFormat instanceof SimpleDateFormat);
 }

 // Test parsing valid ISO-8601 formatted dates
 @Test
 public void testParseValidISO8601Date() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T12:30:45.123Z";
 Date parsedDate = dateFormat.parse(dateStr);
 assertNotNull(parsedDate);
 assertEquals(2022, parsedDate.getYear());
 assertEquals(4, parsedDate.getMonth());
 assertEquals(27, parsedDate.getDate());
 assertEquals(12, parsedDate.getHours());
 assertEquals(30, parsedDate.getMinutes());
 assertEquals(45, parsedDate.getSeconds());
 assertEquals(123, parsedDate.getMilliseconds());
 }

 @Test
 public void testParseValidISO8601DateWithOffset() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
 String dateStr = "2022-04-27T13:45:01.123+01:00";
 Date parsedDate = dateFormat.parse(dateStr);
 assertNotNull(parsedDate);
 assertEquals(2022, parsedDate.getYear());
 assertEquals(4, parsedDate.getMonth());
 assertEquals(27, parsedDate.getDate());
 assertEquals(13, parsedDate.getHours());
 assertEquals(45, parsedDate.getMinutes());
 assertEquals(1, parsedDate.getMinutes()); // Paris is 1 hour ahead of GMT
 assertEquals(1, parsedDate.getHours());
 assertEquals(1, parsedDate.getHours());
 assertEquals(123, parsedDate.getMilliseconds());
 }

 @Test
 public void testParseValidPlainDate() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getInstance();
 String dateStr = "2022-04-27";
 Date parsedDate = dateFormat.parse(dateStr);
 assertNotNull(parsedDate);
 assertEquals(2022, parsedDate.getYear());
 assertEquals(4, parsedDate.getMonth());
 assertEquals(27, parsedDate.getDate());
 }

 @Test
 public void testParseValidRFC1123Date() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getRFC1123Format();
 String dateStr = "Wed, 27 Apr 2022 12:30:45 +0100";
 Date parsedDate = dateFormat.parse(dateStr);
 assertNotNull(parsedDate);
 assertEquals(2022, parsedDate.getYear());
 assertEquals(4, parsedDate.getMonth());
 assertEquals(27, parsedDate.getDate());
 assertEquals(12, parsedDate.getHours());
 assertEquals(30, parsedDate.getMinutes());
 assertEquals(45, parsedDate.getSeconds());
 assertEquals(1, parsedDate.getHours()); // Paris is 1 hour ahead of GMT
 }

 // Test parsing invalid ISO-8601 formatted dates
 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601Date() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T12:30:45.123A"; // missing Z or '+'/'-' timezone indicator
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithMissingYear() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "04-27T12:30:45.123Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidMonth() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-13-27T12:30:45.123Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidDay() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-32T12:30:45.123Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidHour() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T25:30:45.123Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidMinute() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T12:60:45.123Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidSecond() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T12:30:60.123Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidMillisecond() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T12:30:45.124Z";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601DateWithInvalidTimeZoneOffset() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 String dateStr = "2022-04-27T12:30:45.123+++00:00"; // Invalid offset '+++'
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 // Test parsing a date with an invalid format
 @Test(expected = ParseException.class)
 public void testParseInvalidFormatDate() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getInstance();
 String dateStr = "InvalidDateFormat";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

 // Test setting the timezone and locale
 @Test
 public void testSetTimeZone() {
 StdDateFormat dateFormat = new StdDateFormat();
 TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
 dateFormat.setTimeZone(timeZone);
 TimeZone newTimeZone = dateFormat.getTimeZone();
 assertEquals(timeZone, newTimeZone);
 }

 @Test
 public void testSetLocale() {
 StdDateFormat dateFormat = new StdDateFormat();
 Locale locale = new Locale("fr", "FR");
 dateFormat.setLocale(locale);
 Locale newLocale = dateFormat.getLocale();
 assertEquals(locale, newLocale);
 }

 // Test cloning
 @Test
 public void testClone() {
 StdDateFormat dateFormat = new StdDateFormat();
 StdDateFormat clonedDateFormat = dateFormat.clone();
 assertNotSame(dateFormat, clonedDateFormat);
 assertEquals(dateFormat.getClass(), clonedDateFormat.getClass());
 }

 // Test toString
 @Test
 public void testToString() {
 StdDateFormat dateFormat = new StdDateFormat();
 String expected = "DateFormat com.fasterxml.jackson.databind.util.StdDateFormat";
 String actual = dateFormat.toString();
 assertEquals(expected, actual);
 }

 // Test parsing a date with a missing timezone
 @Test(expected = ParseException.class)
 public void testParseDateWithoutTimezone() throws ParseException {
 DateFormat dateFormat = StdDateFormat.getInstance();
 String dateStr = "2022-04-27";
 dateFormat.parse(dateStr);
 fail("Expected ParseException to be thrown");
 }

}