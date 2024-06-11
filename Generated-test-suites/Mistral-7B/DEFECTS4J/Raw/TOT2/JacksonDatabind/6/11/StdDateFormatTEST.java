package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

 // Test constructor and getters
 @Test
 public void testStdDateFormatConstruction() {
 StdDateFormat instance = StdDateFormat.instance;
 assertEquals("Default locale and timezone", DEFAULT_LOCALE, instance.getLocale());
 assertEquals("Default timezone", DEFAULT_TIMEZONE, instance.getTimeZone());
 }

 @Test
 public void testStdDateFormatWithTimeZone() {
 StdDateFormat instanceWithTimeZone = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRANCE);
 assertEquals("Expected locale: France", Locale.FRANCE, instanceWithTimeZone.getLocale());
 assertEquals("Expected timezone: Paris", TimeZone.getTimeZone("Europe/Paris"), instanceWithTimeZone.getTimeZone());
 }

 @Test
 public void testStdDateFormatWithLocale() {
 StdDateFormat instanceWithLocale = new StdDateFormat(Locale.SIMPLIFIED_CHINESE);
 assertEquals("Expected locale: Simplified Chinese", Locale.SIMPPLIFIED_CHINESE, instanceWithLocale.getLocale());
 assertEquals("Default timezone", DEFAULT_TIMEZONE, instanceWithLocale.getTimeZone());
 }

 @Test
 public void testStdDateFormatClone() {
 StdDateFormat instance = StdDateFormat.instance;
 StdDateFormat clonedInstance = instance.clone();
 assertEquals("Clone should have the same locale and timezone", instance.getLocale(), clonedInstance.getLocale());
 assertEquals("Clone should have the same timezone", instance.getTimeZone(), clonedInstance.getTimeZone());
 }

 // Test getters for different date formats
 @Test
 public void testGetISO8601Format() {
 DateFormat iso8601Format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
 assertEquals("Expected format: yyyy-MM-dd'T'HH:mm:ss.SSSZ", DATE_FORMAT_STR_ISO8601, iso8601Format.toPattern());
 }

 @Test
 public void testGetRFC1123Format() {
 DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
 assertEquals("Expected format: EEE, dd MMM yyyy HH:mm:ss zzz", DATE_FORMAT_STR_RFC1123, rfc1123Format.toPattern());
 }

 // Test parse method with valid input
 @Test
 public void testParseValidDate() throws ParseException {
 DateFormat instance = StdDateFormat.instance;
 Date date = instance.parse("2022-12-31T23:59:59.123Z");
 assertEquals("Expected date: Fri Dec 31 23:59:59 UTC 2022", date, new Date(1671415999123L));
 }

 // Test parse method with different date formats
 @Test
 public void testParseDifferentDateFormats() throws ParseException {
 StdDateFormat instance = StdDateFormat.instance;
 Date dateRFC1123 = instance.parse("Sun, 22 Jan 2023 15:45:30 GMT");
 Date dateISO8601_Z = instance.parse("2023-01-22T15:45:30.000Z");
 Date dateISO8601 = instance.parse("2023-01-22");
 Date datePlain = instance.parse("2023-01-22");

 assertEquals("Expected date from RFC1123 format: Sun Jan 22 15:45:30 GMT 2023", dateRFC1123, new Date(1673819130000L));
 assertEquals("Expected date from ISO8601_Z format: Sun Jan 22 15:45:30 UTC 2023", dateISO8601_Z, new Date(1673819130000L));
 assertEquals("Expected date from ISO8601 format: Sun Jan 22 00:00:00 UTC 2023", dateISO8601, new Date(1673846400000L));
 assertEquals("Expected date from plain format: Sun Jan 22 00:00:00 UTC 2023", datePlain, new Date(1673846400000L));
 }

 // Test parse method with invalid input
 @Test
 public void testParseInvalidDate() {
 StdDateFormat instance = StdDateFormat.instance;
 assertThrows(ParseException.class, () -> instance.parse("Invalid"));
 assertThrows(ParseException.class, () -> instance.parse("Invalid date format"));
 assertNull(instance.parse(""));
 }

 // Test format method with valid input
 @Test
 public void testFormatValidDate() {
 StdDateFormat instance = StdDateFormat.instance;
 String iso8601FormatStr = instance.format(new Date(1673819130000L), new StringBuffer(), null);
 assertEquals("Expected format string: 2023-12-31T23:59:59.123Z", DATE_FORMAT_STR_ISO8601, iso8601FormatStr);
 }

 // Test format method with different date formats
 @Test
 public void testFormatDifferentDateFormats() {
 StdDateFormat instance = StdDateFormat.instance;
 String rfc1123FormatStr = instance.format(new Date(1673819130000L), new StringBuffer(), null);
 String iso8601_ZFormatStr = instance.format(new Date(1673819130000L), null, TimeZone.getTimeZone("UTC"));
 String iso8601FormatStr = instance.format(new Date(1673846400000L), null, null);
 String plainFormatStr = instance.format(new Date(1673846400000L), null, null);

 assertEquals("Expected format string from RFC1123 format: Sun, 22 Jan 2023 15:45:30 GMT", DATE_FORMAT_STR_RFC1123, rfc1123FormatStr);
 assertEquals("Expected format string from ISO8601_Z format: Sun, 22 Jan 2023 15:45:30 UTC", DATE_FORMAT_STR_ISO8601_Z, iso8601_ZFormatStr);
 assertEquals("Expected format string from ISO8601 format: Sun Jan 22 00:00:00 UTC 2023", DATE_FORMAT_STR_ISO8601, iso8601FormatStr);
 assertEquals("Expected format string from plain format: Sun Jan 22 00:00:00 UTC 2023", DATE_FORMAT_STR_PLAIN, plainFormatStr);
 }

 // Test format method with invalid input
 @Test
 public void testFormatInvalidDate() {
 StdDateFormat instance = StdDateFormat.instance;
 String formatStr = instance.format(null, new StringBuffer(), null);
 assertNull(formatStr);
 }
}