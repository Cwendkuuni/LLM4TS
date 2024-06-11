package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

 // Test constructor
 @Test
 public void testStdDateFormatConstruction() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 StdDateFormat stdDateFormatWithTimeZone = new StdDateFormat(TimeZone.getDefault(), Locale.getDefault());
 StdDateFormat stdDateFormatWithTimeZoneAndLocale = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);

 assertNotNull(stdDateFormat);
 assertNotNull(stdDateFormatWithTimeZone);
 assertNotNull(stdDateFormatWithTimeZoneAndLocale);
 }

 // Test getInstance method
 @Test
 public void testStdDateFormatInstance() {
 StdDateFormat stdDateFormat = StdDateFormat.getInstance();
 StdDateFormat stdDateFormatWithTimeZone = StdDateFormat.getInstance(TimeZone.getDefault());
 StdDateFormat stdDateFormatWithTimeZoneAndLocale = StdDateFormat.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);

 assertEquals(StdDateFormat.class, stdDateFormat.getClass());
 assertEquals(StdDateFormat.class, stdDateFormatWithTimeZone.getClass());
 assertEquals(StdDateFormat.class, stdDateFormatWithTimeZoneAndLocale.getClass());
 }

 // Test parse method with valid ISO8601 date strings
 @Test
 public void testParseValidISODateStrings() throws ParseException {
 StdDateFormat stdDateFormat = new StdDateFormat();

 Date date1 = stdDateFormat.parse("2022-03-15T14:25:30.123Z");
 assertEquals(2022, date1.getYear());
 assertEquals(3, date1.getMonth());
 assertEquals(15, date1.getDate());
 assertEquals(14, date1.getHours());
 assertEquals(25, date1.getMinutes());
 assertEquals(30, date1.getSeconds());
 assertEquals(123, date1.getMilliseconds());
 assertEquals(0, date1.getTimezoneOffset());

 Date date2 = stdDateFormat.parse("2022-03-15T14:25:30.123+01:00");
 assertEquals(2022, date2.getYear());
 assertEquals(3, date2.getMonth());
 assertEquals(15, date2.getDate());
 assertEquals(14, date2.getHours());
 assertEquals(25, date2.getMinutes());
 assertEquals(30, date2.getSeconds());
 assertEquals(123, date2.getMilliseconds());
 assertEquals(3600000, date2.getTimezoneOffset());
 }

 // Test parse method with valid RFC1123 date strings
 @Test
 public void testParseValidRFCDateStrings() throws ParseException {
 StdDateFormat stdDateFormat = new StdDateFormat();

 Date date1 = stdDateFormat.parse("Tue, 15 Mar 2022 14:25:30 GMT");
 assertEquals(2022, date1.getYear());
 assertEquals(3, date1.getMonth());
 assertEquals(15, date1.getDate());
 assertEquals(14, date1.getHours());
 assertEquals(25, date1.getMinutes());
 assertEquals(30, date1.getSeconds());
 assertEquals(0, date1.getMilliseconds());
 assertEquals(0, date1.getTimezoneOffset());

 Date date2 = stdDateFormat.parse("Tue, 15 Mar 2022 14:25:30 +0100");
 assertEquals(2022, date2.getYear());
 assertEquals(3, date2.getMonth());
 assertEquals(15, date2.getDate());
 assertEquals(14, date2.getHours());
 assertEquals(25, date2.getMinutes());
 assertEquals(30, date2.getSeconds());
 assertEquals(0, date2.getMilliseconds());
 assertEquals(3600000, date2.getTimezoneOffset());
 }

 // Test parse method with valid plain date strings
 @Test
 public void testParseValidPlainDateStrings() throws ParseException {
 StdDateFormat stdDateFormat = new StdDateFormat();

 Date date1 = stdDateFormat.parse("2022-03-15");
 assertEquals(2022, date1.getYear());
 assertEquals(3, date1.getMonth());
 assertEquals(15, date1.getDate());
 assertEquals(0, date1.getHours());
 assertEquals(0, date1.getMinutes());
 assertEquals(0, date1.getSeconds());
 assertEquals(0, date1.getMilliseconds());
 assertEquals(0, date1.getTimezoneOffset());
 }

 // Test parse method with invalid date strings
 @Test(expected = ParseException.class)
 public void testParseInvalidDateStrings() throws ParseException {
 StdDateFormat stdDateFormat = new StdDateFormat();

 stdDateFormat.parse("InvalidDateString");
 }

 // Test parse method with date strings containing non-supported formats
 @Test(expected = ParseException.class)
 public void testParseUnsupportedDateStrings() throws ParseException {
 StdDateFormat stdDateFormat = new StdDateFormat();

 stdDateFormat.parse("Wed, 15 Mar 2022 14:25:30 EDT");
 }

 // Test format method with valid dates
 @Test
 public void testFormatValidDates() {
 StdDateFormat stdDateFormat = new StdDateFormat();

 Date date1 = new Date(1647110400000L);
 String formattedDate1 = stdDateFormat.format(date1);
 assertEquals("2022-03-15T12:00:00Z", formattedDate1);

 Date date2 = new Date(1647110400000L + 3600000); // +1 hour
 String formattedDate2 = stdDateFormat.format(date2);
 assertEquals("2022-03-15T13:00:00Z", formattedDate2);
 }

 // Test format method with null Date object
 @Test
 public void testFormatNullDate() {
 StdDateFormat stdDateFormat = new StdDateFormat();

 String formattedDate = stdDateFormat.format(null);
 assertNull(formattedDate);
 }

 // Test format method with invalid Date object
 @Test(expected = IllegalArgumentException.class)
 public void testFormatInvalidDate() {
 StdDateFormat stdDateFormat = new StdDateFormat();

 Date invalidDate = new Date(-1);
 stdDateFormat.format(invalidDate);
 }

 // Test setTimeZone method
 @Test
 public void testSetTimeZone() {
 StdDateFormat stdDateFormat = new StdDateFormat();

 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 stdDateFormat.setTimeZone(timeZone);
 assertEquals(timeZone, stdDateFormat.getTimeZone());
 }

 // Test setLocale method
 @Test
 public void testSetLocale() {
 StdDateFormat stdDateFormat = new StdDateFormat();

 Locale locale = Locale.US;
 stdDateFormat.setLocale(locale);
 assertEquals(locale, stdDateFormat.getLocale());
 }

 // Test clone method
 @Test
 public void testClone() {
 StdDateFormat stdDateFormat = new StdDateFormat();

 StdDateFormat clonedStdDateFormat = stdDateFormat.clone();
 assertNotSame(stdDateFormat, clonedStdDateFormat);
 assertEquals(stdDateFormat.getClass(), clonedStdDateFormat.getClass());
 }
}