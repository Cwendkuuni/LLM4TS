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
 assertEquals(Locale.US, dateFormat.getLocale());
 assertEquals(TimeZone.getTimeZone("GMT"), dateFormat.getTimeZone());
 }

 // Test setTimeZone and getTimeZone methods
 @Test
 public void testSetTimeZoneAndGetTimeZoneMethods() {
 StdDateFormat dateFormat = new StdDateFormat();
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 dateFormat.setTimeZone(timeZone);
 assertEquals(timeZone, dateFormat.getTimeZone());
 }

 // Test setLocale and getLocale methods
 @Test
 public void testSetLocaleAndGetLocaleMethods() {
 StdDateFormat dateFormat = new StdDateFormat();
 Locale locale = new Locale("fr", "FR");
 dateFormat.setLocale(locale);
 assertEquals(locale, dateFormat.getLocale());
 }

 // Test getISO8601Format method with default timezone
 @Test
 public void testGetISO8601FormatWithDefaultTimeZone() {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", isoFormat.toPattern());
 }

 // Test getISO8601Format method with custom timezone
 @Test
 public void testGetISO8601FormatWithCustomTimeZone() {
 DateFormat isoFormat = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("Europe/Paris"));
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", isoFormat.toPattern());
 assertEquals(TimeZone.getTimeZone("Europe/Paris"), isoFormat.getTimeZone());
 }

 // Test getRFC1123Format method with default timezone
 @Test
 public void testGetRFC1123FormatWithDefaultTimeZone() {
 DateFormat rfcFormat = StdDateFormat.getRFC1123Format();
 assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", rfcFormat.toPattern());
 }

 // Test getRFC1123Format method with custom timezone
 @Test
 public void testGetRFC1123FormatWithCustomTimeZone() {
 DateFormat rfcFormat = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("Asia/Seoul"));
 assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", rfcFormat.toPattern());
 assertEquals(TimeZone.getTimeZone("Asia/Seoul"), rfcFormat.getTimeZone());
 }

 // Test parse method with valid ISO-8601 date
 @Test
 public void testParseValidISO8601Date() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 Date date = isoFormat.parse("2019-12-31T23:59:59.999Z");
 assertNotNull(date);
 }

 // Test parse method with valid RFC-1123 date
 @Test
 public void testParseValidRFC1123Date() throws ParseException {
 DateFormat rfcFormat = StdDateFormat.getRFC1123Format();
 Date date = rfcFormat.parse("Wed, 31 Dec 1997 23:59:59 GMT");
 assertNotNull(date);
 }

 // Test parse method with invalid date (wrong format)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateWrongFormat() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:59:59.999A");
 }

 // Test parse method with invalid date (missing year)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingYear() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("12-31-2019T23:59:59.999Z");
 }

 // Test parse method with invalid date (missing month)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingMonth() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-01-31T23:59:59.999Z");
 }

 // Test parse method with invalid date (missing day)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingDay() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:59:59.999Z");
 }

 // Test parse method with invalid date (missing hour)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingHour() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T00:59:59.999Z");
 }

 // Test parse method with invalid date (missing minute)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingMinute() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:00:00.999Z");
 }

 // Test parse method with invalid date (missing second)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingSecond() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:59:00.999Z");
 }

 // Test parse method with invalid date (missing millisecond)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateMissingMillisecond() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:59:59.000Z");
 }

 // Test parse method with invalid date (wrong timezone format)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateWrongTimezoneFormat() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:59:59.999+02:00");
 }

 // Test parse method with invalid date (wrong timezone value)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateWrongTimezoneValue() throws ParseException {
 DateFormat isoFormat = StdDateFormat.getISO8601Format();
 isoFormat.parse("2019-12-31T23:59:59.999Zzzz");
 }

}