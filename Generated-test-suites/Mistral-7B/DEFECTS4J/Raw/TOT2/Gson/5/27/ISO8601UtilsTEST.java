import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

 // Test case for the format method with default timezone and no milliseconds precision
 @Test
 public void testFormatDefaultTimezone() {
 Date date = new GregorianCalendar(2021, 11, 15).getTime();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("2021-11-15T00:00:00Z", formattedDate);
 }

 // Test case for the format method with GMT timezone and milliseconds precision
 @Test
 public void testFormatGMTWithMillis() {
 Date date = new GregorianCalendar(2021, 11, 15, 14, 30, 45).getTime();
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
 assertEquals("2021-11-15T14:30:45.045Z", formattedDate);
 }

 // Test case for the format method with custom timezone
 @Test
 public void testFormatCustomTimezone() {
 Date date = new GregorianCalendar(2021, 11, 15).getTime();
 String timeZoneId = "America/New_York";
 TimeZone customTimeZone = TimeZone.getTimeZone(timeZoneId);
 String formattedDate = ISO8601Utils.format(date, false, customTimeZone);
 assertEquals("2021-11-15T05:00:00-05:00", formattedDate);
 }

 // Test case for the parse method with default timezone
 @Test
 public void testParseDefaultTimezone() throws ParseException {
 String dateString = "2021-11-15T00:00:00Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Mon Nov 15 00:00:00 GMT", parsedDate.toString());
 }

 // Test case for the parse method with GMT timezone and milliseconds precision
 @Test
 public void testParseGMTWithMillis() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Mon Nov 15 14:30:45 GMT", parsedDate.toString());
 }

 // Test case for the parse method with custom timezone
 @Test
 public void testParseCustomTimezone() throws ParseException {
 String dateString = "2021-11-15T05:00:00-05:00";
 ParsePosition pos = new ParsePosition(0);
 TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
 Date parsedDate = ISO8601Utils.parse(dateString, pos, customTimeZone);
 assertEquals("Mon Nov 15 10:00:00 EST", parsedDate.toString());
 }

 // Test case for the parse method with invalid format
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidFormat() throws ParseException {
 String dateString = "2021-11-15T14:30:45Z-05:00"; // missing T
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown an IllegalArgumentException for the input: " + dateString);
 }

 // Test case for the parse method with missing year
 @Test(expected = ParseException.class)
 public void testParseMissingYear() throws ParseException {
 String dateString = "-05:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing month
 @Test(expected = ParseException.class)
 public void testParseMissingMonth() throws ParseException {
 String dateString = "2021-0-15T14:30:45Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing day
 @Test(expected = ParseException.class)
 public void testParseMissingDay() throws ParseException {
 String dateString = "2021-11-0T14:30:45Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing hour
 @Test(expected = ParseException.class)
 public void testParseMissingHour() throws ParseException {
 String dateString = "2021-11-15T:30:45.045Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing minute
 @Test(expected = ParseException.class)
 public void testParseMissingMinute() throws ParseException {
 String dateString = "2021-11-15T14:.045Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing second
 @Test(expected = ParseException.class)
 public void testParseMissingSecond() throws ParseException {
 String dateString = "2021-11-15T14:30.";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing millisecond
 @Test(expected = ParseException.class)
 public void testParseMissingMillisecond() throws ParseException {
 String dateString = "2021-11-15T14:30:45.";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("GMT"));
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with missing timezone
 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingTimezone() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 fail("Parse should have thrown an IllegalArgumentException for the input: " + dateString);
 }

 // Test case for the parse method with invalid timezone
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTimezone() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045InvalidTimezone";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 fail("Parse should have thrown an IllegalArgumentException for the input: " + dateString);
 }

 // Test case for the parse method with invalid timezone format
 @Test(expected = ParseException.class)
 public void testParseInvalidTimezoneFormat() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045+InvalidTimezone";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with invalid timezone offset
 @Test(expected = ParseException.class)
 public void testParseInvalidTimezoneOffset() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045+0:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with timezone offset longer than 24 hours
 @Test(expected = ParseException.class)
 public void testParseTimezoneOffsetLongerThan24Hours() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045+25:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

 // Test case for the parse method with timezone offset shorter than -24 hours
 @Test(expected = ParseException.class)
 public void testParseTimezoneOffsetShorterThanNegative24Hours() throws ParseException {
 String dateString = "2021-11-15T14:30:45.045-25:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 fail("Parse should have thrown a ParseException for the input: " + dateString);
 }

}