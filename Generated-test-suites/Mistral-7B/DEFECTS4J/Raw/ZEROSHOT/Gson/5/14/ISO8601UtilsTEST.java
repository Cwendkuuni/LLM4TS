package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 @Test
 public void testFormat() throws Exception {
 Date date = new Date(1523365200000L); // Tue Aug 14 00:00:00 UTC 2018
 String expected = "2018-08-14T00:00:00Z";
 String result = ISO8601Utils.format(date);
 assertEquals(expected, result);

 date = new Date(1523365200000L + 3600 * 1000 * 2); // Tue Aug 14 01:00:00 UTC 2018
 expected = "2018-08-14T01:00:00Z";
 result = ISO8601Utils.format(date);
 assertEquals(expected, result);

 date = new Date(1523365200000L + 3600 * 1000 * 24 + 30 * 60 * 1000); // Wed Aug 15 00:00:00 UTC 2018
 expected = "2018-08-15T00:00:00Z";
 result = ISO8601Utils.format(date);
 assertEquals(expected, result);

 date = new Date(1523365200000L + 3600 * 1000 * 24 * 2 + 15 * 60 * 1000); // Thu Aug 16 15:15:00 UTC 2018
 expected = "2018-08-16T15:15:00Z";
 result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
 assertEquals(expected, result);
 }

 @Test
 public void testParse() throws Exception {
 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

 // UTC
 Date date = ISO8601Utils.parse("2018-08-14T00:00:00Z", new ParsePosition(0));
 assertEquals(new Date(1523365200000L), date);

 date = ISO8601Utils.parse("2018-08-14T01:00:00Z", new ParsePosition(0));
 assertEquals(new Date(1523365200000L + 3600 * 1000 * 2), date);

 date = ISO8601Utils.parse("2018-08-15T00:00:00Z", new ParsePosition(0));
 assertEquals(new Date(1523365200000L + 3600 * 1000 * 24), date);

 date = ISO8601Utils.parse("2018-08-16T15:15:00Z", new ParsePosition(0));
 assertEquals(new Date(1523365200000L + 3600 * 1000 * 24 * 2 + 15 * 60 * 1000), date);

 // +01:00
 Date datePlusOneHour = ISO8601Utils.parse("2018-08-14T00:00:00+01:00", new ParsePosition(0));
 assertEquals(new Date(1523365200000L + 3600 * 1000), datePlusOneHour);

 // +02:30
 Date datePlusTwoThirtyMinutes = ISO8601Utils.parse("2018-08-14T00:00:00+02:30", new ParsePosition(0));
 assertEquals(new Date(1523365200000L + 3600 * 1000 * 2 + 15 * 60 * 1000), datePlusTwoThirtyMinutes);

 // Invalid format
 try {
 ISO8601Utils.parse("2018-08-14T00:00:00A", new ParsePosition(0));
 fail("Should not parse this date string");
 } catch (ParseException e) {
 // Expected
 }

 // Invalid format with milliseconds
 try {
 ISO8601Utils.parse("2018-08-14T00:00:00.000A", new ParsePosition(0));
 fail("Should not parse this date string");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testFormatAndParseRoundtrip() throws Exception {
 Date date = new Date(1523365200000L); // Tue Aug 14 00:00:00 UTC 2018
 String expected = "2018-08-14T00:00:00Z";
 String result = ISO8601Utils.format(date);
 assertEquals(expected, result);

 date = ISO8601Utils.parse(result, new ParsePosition(0));
 assertEquals(date.getTime(), 1523365200000L);
 }

 @Test
 public void testFormatAndParseRoundtripWithMillis() throws Exception {
 Date date = new Date(1523365200000L + 1234); // Tue Aug 14 00:00:12.343 UTC 2018
 String expected = "2018-08-14T00:00:12.343Z";
 String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
 assertEquals(expected, result);

 date = ISO8601Utils.parse(result, new ParsePosition(0));
 assertEquals(date.getTime(), (1523365200000L + 1234));
 }

 @Test
 public void testFormatAndParseRoundtripWithOffset() throws Exception {
 Date date = new Date(1523365200000L + 10 * 60 * 60 * 1000); // Wed Aug 15 01:00:00 CET 2018
 String expected = "2018-08-15T01:00:00+02:00";
 String result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/Berlin"));
 assertEquals(expected, result);

 date = ISO8601Utils.parse(result, new ParsePosition(0));
 assertEquals(date.getTime(), (1523368800000L));
 }
}