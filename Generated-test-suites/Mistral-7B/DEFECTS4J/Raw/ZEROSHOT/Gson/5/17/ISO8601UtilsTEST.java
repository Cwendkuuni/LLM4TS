package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

 private static final String UTC_ID = "UTC";
 private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

 @Test
 public void testFormatEmptyDate() throws Exception {
 String result = ISO8601Utils.format(null, false, TIMEZONE_UTC);
 assertEquals("", result);
 }

 @Test
 public void testFormatDate() throws Exception {
 Calendar calendar = new GregorianCalendar(TIMEZONE_UTC, Locale.US, 2023, 1, 15);
 String result = ISO8601Utils.format(calendar.getTime(), false, TIMEZONE_UTC);
 assertEquals("2023-01-15T00:00:00Z", result);
 }

 @Test
 public void testFormatDateWithMilliseconds() throws Exception {
 Calendar calendar = new GregorianCalendar(TIMEZONE_UTC, Locale.US, 2023, 1, 15);
 calendar.set(Calendar.MILLISECOND, 123);
 String result = ISO8601Utils.format(calendar.getTime(), true, TIMEZONE_UTC);
 assertEquals("2023-01-15T00:00:12.123Z", result);
 }

 @Test
 public void testFormatDateWithTimeZone() throws Exception {
 Calendar calendar = new GregorianCalendar(TIMEZONE_UTC, Locale.US, 2023, 1, 15, 1, 1, 1, 123, 45, 60, 123, 456);
 TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
 String result = ISO8601Utils.format(calendar.getTime(), false, timeZone);
 assertEquals("2023-01-15T07:01:12.456-08:00", result);
 }

 @Test
 public void testParseEmptyString() throws Exception {
 ParsePosition pos = new ParsePosition(0);
 assertNull(ISO8601Utils.parse(null, pos));
 }

 @Test
 public void testParseInvalidFormat() throws Exception {
 String dateString = "2023-01-15T07:01:12Zabc";
 ParsePosition pos = new ParsePosition(0);
 assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
 }

 @Test
 public void testParseValidFormat() throws Exception {
 String dateString = "2023-01-15T07:01:12.456Z";
 ParsePosition pos = new ParsePosition(0);
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 Date result = ISO8601Utils.parse(dateString, pos);
 assertEquals("Sun Jan 15 07:01:12 UTC 2023", result.toString());
 }

 @Test
 public void testParseValidFormatWithTimeZoneOffset() throws Exception {
 String dateString = "2023-01-15T07:01:12-08:00";
 ParsePosition pos = new ParsePosition(0);
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
 Date result = ISO8601Utils.parse(dateString, pos);
 assertEquals("Sun Jan 15 15:01:12 PST 2023", result.toString());
 }

 @Test
 public void testParseValidFormatWithTimeZoneID() throws Exception {
 String dateString = "2023-01-15T07:01:12Z";
 ParsePosition pos = new ParsePosition(0);
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 Date result = ISO8601Utils.parse(dateString, pos);
 assertEquals("Sun Jan 15 07:01:12 UTC 2023", result.toString());
 }

}