package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

 @Test
 public void testParseISO8601() throws ParseException {
 DateFormat dateFormat = new StdDateFormat();

 // Test parsing a date with time and timezone
 String isoDate = "2022-01-01T12:34:56.789Z";
 ParsePosition pos = new ParsePosition(0);
 Date date = dateFormat.parse(isoDate, pos);
 assertEquals(new Date(1641343496000L), date);
 assertEquals(0, pos.getIndex());

 // Test parsing a date with time but no timezone
 isoDate = "2022-01-01T12:34:56.789";
 date = dateFormat.parse(isoDate, pos);
 assertEquals(new Date(1641343496000L), date.getTime());
 assertEquals(0, pos.getIndex());

 // Test parsing a plain date
 String plainDate = "2022-01-01";
 date = dateFormat.parse(plainDate, pos);
 assertEquals(new Date(1642246400000L), date.getTime());
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseRFC1123() throws ParseException {
 DateFormat dateFormat = new StdDateFormat();

 // Test parsing a date with time and timezone
 String rfcDate = "Thu, 01 Jan 2022 12:34:56 GMT";
 ParsePosition pos = new ParsePosition(0);
 Date date = dateFormat.parse(rfcDate, pos);
 assertEquals(new Date(1641343496000L), date);
 assertEquals(0, pos.getIndex());

 // Test parsing a date with time but no timezone
 rfcDate = "Thu, 01 Jan 2022 12:34:56";
 date = dateFormat.parse(rfcDate, pos);
 assertEquals(new Date(1641343496000L), date.getTime());
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseInvalidFormat() throws ParseException {
 DateFormat dateFormat = new StdDateFormat();

 String invalidDate = "invalidDate";
 ParsePosition pos = new ParsePosition(0);
 Date date = dateFormat.parse(invalidDate, pos);
 assertNull(date);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseNullInput() throws ParseException {
 DateFormat dateFormat = new StdDateFormat();

 String nullInput = null;
 ParsePosition pos = new ParsePosition(0);
 Date date = dateFormat.parse(nullInput, pos);
 assertNull(date);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testFormat() {
 DateFormat dateFormat = new StdDateFormat();

 // Test formatting a date with time and timezone
 Date date = new Date(1641343496000L);
 String isoDate = dateFormat.format(date);
 assertEquals("2022-01-01T12:34:56.789Z", isoDate);

 // Test formatting a date with time but no timezone
 date.setTime(date.getTime() - (60 * 60 * 1000));
 isoDate = dateFormat.format(date);
 assertEquals("2022-01-01T11:34:56.789", isoDate);

 // Test formatting a plain date
 date.setTime(date.getTime() - (10 * 60 * 60 * 24 * 7 * 30 + 10 * 60 * 60));
 isoDate = dateFormat.format(date);
 assertEquals("1999-12-31", isoDate);
 }

 @Test
 public void testParseWithCustomTimezone() throws ParseException {
 DateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Moscow"), Locale.US);

 // Test parsing a date with time and timezone
 String isoDate = "2022-01-01T12:34:56.789+02:00";
 ParsePosition pos = new ParsePosition(0);
 Date date = dateFormat.parse(isoDate, pos);
 assertEquals(new Date(1641360096000L), date);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testFormatWithCustomTimezone() {
 DateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Moscow"), Locale.US);

 // Test formatting a date with time and timezone
 Date date = new Date(1641360096000L);
 String isoDate = dateFormat.format(date);
 assertEquals("2022-01-01T15:34:56.789+02:00", isoDate);
 }
}