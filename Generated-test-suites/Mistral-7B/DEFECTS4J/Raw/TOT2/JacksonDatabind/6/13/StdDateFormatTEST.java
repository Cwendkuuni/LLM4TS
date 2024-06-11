package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

 // Basic test for the default constructor
 @Test
 public void testDefaultConstructor() {
 StdDateFormat dateFormat = new StdDateFormat();
 assertNotNull(dateFormat);
 }

 // Test getInstance method
 @Test
 public void testGetInstance() {
 StdDateFormat dateFormat = StdDateFormat.getInstance();
 assertNotNull(dateFormat);
 }

 // Test getInstance method with timezone
 @Test
 public void testGetInstanceWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
 StdDateFormat dateFormat = StdDateFormat.getInstance(timeZone);
 assertNotNull(dateFormat);
 assertNotEquals(TimeZone.getDefault(), timeZone);
 }

 // Test getInstance method with locale
 @Test
 public void testGetInstanceWithLocale() {
 Locale locale = Locale.FRENCH;
 StdDateFormat dateFormat = StdDateFormat.getInstance(locale);
 assertNotNull(dateFormat);
 assertEquals(locale, dateFormat.getLocale());
 }

 // Test getInstance method with timezone and locale
 @Test
 public void testGetInstanceWithTimeZoneAndLocale() {
 TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
 Locale locale = Locale.FRENCH;
 StdDateFormat dateFormat = StdDateFormat.getInstance(timeZone, locale);
 assertNotNull(dateFormat);
 assertNotEquals(TimeZone.getDefault(), timeZone);
 assertEquals(locale, dateFormat.getLocale());
 }

 // Test parse method with valid date strings
 @Test
 public void testParseWithValidDateStrings() {
 StdDateFormat dateFormat = new StdDateFormat();
 String[] dateStrings = {
 "2019-12-31T12:34:56.789+05:30",
 "Thu, 31 Dec 2019 12:34:56 GMT",
 "2019-12-31",
 "Wed, 30 Dec 2019 12:34:56 GMT",
 "2019-12-31 12:34:56"
 };
 Date expectedDate;
 for (String dateString : dateStrings) {
 expectedDate = new GregorianCalendar(2019, 11, 31, 12, 34, 56, 789, Calendar.DEC_LONG).getTime();
 Date parsedDate = dateFormat.parse(dateString);
 assertEquals(expectedDate.getTime(), parsedDate.getTime());
 }
 }

 // Test parse method with invalid date strings
 @Test(expected = ParseException.class)
 public void testParseWithInvalidDateStrings() {
 StdDateFormat dateFormat = new StdDateFormat();
 String[] invalidDateStrings = {
 "2019-12-32", // Invalid month
 "2019-12-31T12:34:56.789", // No timezone
 "Thu, 31 Dec 2019 12:34:56 GMT0", // Extra character in timezone
 "2019-12-31 12:34:56.789:55" // Invalid format
 };
 for (String invalidDateString : invalidDateStrings) {
 dateFormat.parse(invalidDateString);
 }
 }

 // Test format method with valid date
 @Test
 public void testFormatWithValidDate() {
 StdDateFormat dateFormat = new StdDateFormat();
 Date date = new GregorianCalendar(2019, 11, 31, 12, 34, 56, 789, Calendar.DEC_LONG).getTime();
 String expectedDateString = "Mon, 02 Dec 2019 12:34:56 GMT";
 String formattedDateString = dateFormat.format(date);
 assertEquals(expectedDateString, formattedDateString);
 }

 // Test format method with null date
 @Test(expected = NullPointerException.class)
 public void testFormatWithNullDate() {
 StdDateFormat dateFormat = new StdDateFormat();
 dateFormat.format(null);
 }

 // Test clone method
 @Test
 public void testCloneMethod() {
 StdDateFormat original = new StdDateFormat();
 StdDateFormat cloned = original.clone();
 assertNotSame(original, cloned);
 assertEquals(original.getLocale(), cloned.getLocale());
 assertEquals(original.getTimeZone(), cloned.getTimeZone());
 }
}