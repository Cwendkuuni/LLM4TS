import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

 // Test for format method with no arguments
 @Test
 public void testFormatWithNoArguments() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
 assertTrue(formattedDate.matches(expectedFormat));
 }

 // Test for format method with milliseconds argument
 @Test
 public void testFormatWithMilliseconds() {
 Date date = new Date(System.currentTimeMillis());
 String formattedDate = ISO8601Utils.format(date, true);
 String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 assertTrue(formattedDate.matches(expectedFormat));
 }

 // Test for format method with custom timezone argument
 @Test
 public void testFormatWithCustomTimezone() {
 Date date = new Date();
 Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
 calendar.setTime(date);
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
 String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'-04:00'";
 assertTrue(formattedDate.matches(expectedFormat));
 }

 // Test for parse method with valid ISO-8601 formatted string
 @Test
 public void testParseValidISODate() throws ParseException {
 String isoDate = "2022-12-31T23:59:59.999Z";
 Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
 assertEquals("Wrong parsed date", new Date(1671459999000L), parsedDate);
 }

 // Test for parse method with valid ISO-8601 formatted string with timezone
 @Test
 public void testParseValidISODateWithTimezone() throws ParseException {
 String isoDate = "2022-12-31T23:59:59.999+01:00";
 Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
 assertEquals("Wrong parsed date", new Date(1671463599000L), parsedDate);
 }

 // Test for parse method with invalid ISO-8601 formatted string
 @Test(expected = ParseException.class)
 public void testParseInvalidISODate() throws ParseException {
 String invalidIsoDate = "2022-12-31T23:59:59.999Z-";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(invalidIsoDate, pos);
 fail("No ParseException was thrown");
 }

 // Test for parse method with invalid timezone offset
 @Test(expected = ParseException.class)
 public void testParseInvalidTimezoneOffset() throws ParseException {
 String invalidTimezoneOffset = "2022-12-31T23:59:59.999+60:61";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(invalidTimezoneOffset, pos);
 fail("No ParseException was thrown");
 }

 // Test for parse method with null argument
 @Test(expected = NullPointerException.class)
 public void testParseNullArgument() {
 try {
 ISO8601Utils.parse(null, new ParsePosition(0));
 fail("No NullPointerException was thrown");
 } catch (NullPointerException e) {
 }
 }
}