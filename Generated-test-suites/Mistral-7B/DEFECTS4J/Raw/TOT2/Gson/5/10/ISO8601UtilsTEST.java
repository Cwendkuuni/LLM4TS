import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.*;

public class ISO8601UtilsTest {

 private static final String UTC_ID = "UTC";
 private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

 @Test
 public void testFormatDateWithDefaultTimeZone() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("Expected date to be formatted as yyyy-MM-ddThh:mm:ssZ", "yyyy-MM-ddThh:mm:ssZ", formattedDate.substring(0, 23));
 }

 @Test
 public void testFormatDateWithGMTTimeZoneAndMilliseconds() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date, true, TIMEZONE_UTC);
 assertEquals("Expected date to be formatted as yyyy-MM-ddThh:mm:ss.sssZ", "yyyy-MM-ddThh:mm:ss.sssZ", formattedDate);
 }

 @Test
 public void testFormatDateWithSpecifiedTimeZone() {
 Date date = new Date();
 TimeZone tz = ZoneId.of("America/Los_Angeles").toZone();
 String formattedDate = ISO8601Utils.format(date, false, tz);
 assertEquals("Expected date to be formatted as yyyy-MM-ddThh:mm:ss[+|-]hh:mm", "yyyy-MM-ddThh:mm:ss[+-]hh:mm", formattedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDateWithInvalidInput() {
 String invalidInput = "abc";
 try {
 ISO8601Utils.format(null, false, TIMEZONE_UTC);
 fail("Should have thrown an IllegalArgumentException with message 'Null date passed'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 ISO8601Utils.format(new Date(0), false, TIMEZONE_UTC);
 fail("Should have thrown an IllegalArgumentException with message 'Invalid date passed'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 ISO8601Utils.format(new Date(), false, null);
 fail("Should have thrown an IllegalArgumentException with message 'Null timezone passed'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 ISO8601Utils.format(new Date(), false, TIMEZONE_UTC, "InvalidFormat");
 fail("Should have thrown an IllegalArgumentException with message 'Invalid format string'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 ISO8601Utils.format(new Date(), false, TIMEZONE_UTC, "yyyy-MM-ddThh:mm:ss.sssZZZ");
 fail("Should have thrown an IllegalArgumentException with message 'Invalid format string'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 String invalidInputFormatted = ISO8601Utils.format(new Date(), false, TIMEZONE_UTC);
 try {
 ISO8601Utils.format(new Date(), false, TIMEZONE_UTC, invalidInputFormatted);
 fail("Should have thrown an IllegalArgumentException with message 'Invalid format string'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseDate() {
 Date date = null;
 ParsePosition pos = new ParsePosition(0);
 String dateString = "2022-12-31T23:59:59.123Z";
 try {
 date = ISO8601Utils.parse(dateString, pos);
 assertEquals("Expected date to be parsed successfully", new Date(1673485999123L), date);
 assertEquals("Expected parse position to be at end of string after parsing", dateString.length(), pos.getIndex());
 } catch (ParseException e) {
 fail("Should have parsed the date successfully");
 }
 String dateWithOffsetString = "2022-12-31T23:59:59.123+01:00";
 try {
 date = ISO8601Utils.parse(dateWithOffsetString, pos);
 assertEquals("Expected date to be parsed successfully", new Date(1673488599123L), date);
 assertEquals("Expected parse position to be at end of string after parsing", dateWithOffsetString.length(), pos.getIndex());
 } catch (ParseException e) {
 fail("Should have parsed the date successfully");
 }
 String dateWithInvalidOffsetString = "2022-12-31T23:59:59.123invalidOffset";
 try {
 date = ISO8601Utils.parse(dateWithInvalidOffsetString, pos);
 fail("Should have thrown a ParseException with message 'Invalid format: Invalid time zone offset'");
 } catch (ParseException e) {
 // expected
 }
 String dateWithInvalidFormatString = "2022-12-31T23:59:59.123ZinvalidFormat";
 try {
 date = ISO8601Utils.parse(dateWithInvalidFormatString, pos);
 fail("Should have thrown a ParseException with message 'Invalid format: Invalid format string'");
 } catch (ParseException e) {
 // expected
 }
 }

}