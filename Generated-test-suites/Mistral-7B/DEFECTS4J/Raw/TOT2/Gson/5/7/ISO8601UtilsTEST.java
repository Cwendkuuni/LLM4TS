import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ISO8601UtilsTest {

 private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

 @Test
 public void testFormat() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
 Date parsedDate = null;
 try {
 parsedDate = sdf.parse(formattedDate);
 } catch (ParseException e) {
 fail("Failed to parse formatted date: " + formattedDate);
 }
 assertEquals("Formatted and parsed date should be the same", date, parsedDate);
 }

 @Test
 public void testFormatWithMilliseconds() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date, true);
 SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT + ".SSS");
 Date parsedDate = null;
 try {
 parsedDate = sdf.parse(formattedDate);
 } catch (ParseException e) {
 fail("Failed to parse formatted date with milliseconds: " + formattedDate);
 }
 assertEquals("Formatted and parsed date with milliseconds should be the same", date, parsedDate);
 }

 @Test
 public void testFormatWithUTCTimezone() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
 SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT + "Z");
 Date parsedDate = null;
 try {
 parsedDate = sdf.parse(formattedDate);
 } catch (ParseException e) {
 fail("Failed to parse formatted date with UTC timezone: " + formattedDate);
 }
 assertEquals("Formatted and parsed date with UTC timezone should be the same", date, parsedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithInvalidDate() {
 Date date = null;
 String formattedDate = ISO8601Utils.format(date);
 fail("Expected IllegalArgumentException when formatting null date");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeYear() {
 Date date = new Date(0);
 String formattedDate = ISO8601Utils.format(date);
 fail("Expected IllegalArgumentException when formatting date with negative year");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeMonth() {
 Date date = new Date();
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 calendar.setTime(date);
 calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
 Date invalidDate = calendar.getTime();
 String formattedDate = ISO8601Utils.format(invalidDate);
 fail("Expected IllegalArgumentException when formatting date with negative month");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeDayOfMonth() {
 Date date = new Date();
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 calendar.setTime(date);
 calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
 Date invalidDate = calendar.getTime();
 String formattedDate = ISO8601Utils.format(invalidDate);
 fail("Expected IllegalArgumentException when formatting date with negative day of month");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeHour() {
 Date date = new Date();
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 calendar.setTime(date);
 calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
 Date invalidDate = calendar.getTime();
 String formattedDate = ISO8601Utils.format(invalidDate);
 fail("Expected IllegalArgumentException when formatting date with negative hour");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeMinute() {
 Date date = new Date();
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 calendar.setTime(date);
 calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
 Date invalidDate = calendar.getTime();
 String formattedDate = ISO8601Utils.format(invalidDate);
 fail("Expected IllegalArgumentException when formatting date with negative minute");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeSecond() {
 Date date = new Date();
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 calendar.setTime(date);
 calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
 Date invalidDate = calendar.getTime();
 String formattedDate = ISO8601Utils.format(invalidDate);
 fail("Expected IllegalArgumentException when formatting date with negative second");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNegativeMillisecond() {
 Date date = new Date();
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
 calendar.setTime(date);
 calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) - 1);
 Date invalidDate = calendar.getTime();
 String formattedDate = ISO8601Utils.format(invalidDate);
 fail("Expected IllegalArgumentException when formatting date with negative millisecond");
 }

 @Test
 public void testParse() {
 String isoDate = "2015-12-31T23:59:59.999Z";
 Date date = null;
 ParsePosition pos = new ParsePosition(0);
 try {
 date = ISO8601Utils.parse(isoDate, pos);
 } catch (ParseException e) {
 fail("Failed to parse valid ISO date: " + isoDate);
 }
 assertEquals("Parsed date should match the original", isoDate, ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC")));
 }

 @Test
 public void testParseWithOffset() {
 String isoDate = "2015-12-31T23:59:59.999-01:00";
 Date date = null;
 ParsePosition pos = new ParsePosition(0);
 try {
 date = ISO8601Utils.parse(isoDate, pos);
 } catch (ParseException e) {
 fail("Failed to parse ISO date with offset: " + isoDate);
 }
 assertEquals("Parsed date with offset should match the original", isoDate, ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/New_York")));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidDateFormat() {
 String isoDate = "2015-12-31T23:59:59.999A";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing invalid ISO date format: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidYear() {
 String isoDate = "0-12-31T23:59:59.999Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid year: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidMonth() {
 String isoDate = "2015-00-31T23:59:59.999Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid month: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidDayOfMonth() {
 String isoDate = "2015-12-32T23:59:59.999Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid day of month: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidHour() {
 String isoDate = "2015-12-31T-1:59:59.999Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid hour: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidMinute() {
 String isoDate = "2015-12-31T23:60:59.999Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid minute: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidSecond() {
 String isoDate = "2015-12-31T23:59:60.999Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid second: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidMillisecond() {
 String isoDate = "2015-12-31T23:59:59.10001Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid millisecond: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOffset() {
 String isoDate = "2015-12-31T23:59:59.999X";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid offset: " + isoDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidTimezoneID() {
 String isoDate = "2015-12-31T23:59:59.999XXX";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected ParseException when parsing ISO date with invalid timezone ID: " + isoDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullString() {
 String isoDate = null;
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(isoDate, pos);
 fail("Expected IllegalArgumentException when parsing null ISO date string");
 }
}