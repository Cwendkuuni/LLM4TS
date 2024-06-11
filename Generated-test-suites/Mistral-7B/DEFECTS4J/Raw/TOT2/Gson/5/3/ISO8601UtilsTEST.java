import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDateWithDefaultTimezone() {
 Date date = new Date(1234567890123L); // Fri, 26 Mar 2010 01:47:10 GMT
 String expectedResult = "2010-03-26T01:47:10Z";
 String actualResult = ISO8601Utils.format(date);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatDateWithMillisecondsAndCustomTimezone() {
 Date date = new Date(1234567890123L); // Fri, 26 Mar 2010 01:47:10 GMT
 TimeZone customTimezone = TimeZone.getTimeZone("Europe/Paris");
 String expectedResult = "2010-03-26T02:47:10.000+01:00";
 String actualResult = ISO8601Utils.format(date, true, customTimezone);
 assertEquals(expectedResult, actualResult);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFormatDateWithInvalidDate() {
 String invalidDate = "2020-02-31";
 try {
 ISO8601Utils.format(new Date(), false, TimeZone.getTimeZone("UTC"));
 } catch (ParseException e) {
 // expected, we're only interested in the IllegalArgumentException
 throw e.getCause();
 }
 }

 @Test
 public void testParseDateWithDefaultTimezone() throws ParseException {
 String dateString = "2010-03-26T01:47:10Z";
 ParsePosition pos = new ParsePosition(0);
 Date expectedDate = new Date(1234567890123L);
 Date actualDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(expectedDate.getTime(), actualDate.getTime());
 }

 @Test (expected = ParseException.class)
 public void testParseDateWithInvalidFormat() throws ParseException {
 String invalidDateString = "2010-03-26T01:47:10W";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(invalidDateString, pos);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseDateWithInvalidTimezoneIndicator() throws ParseException {
 String timezoneIndicator = "XXX";
 String dateString = "2010-03-26T01:47:10" + timezoneIndicator;
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test (expected = IndexOutOfBoundsException.class)
 public void testParseDateWithTruncatedString() throws ParseException {
 String truncatedString = "2010-03-26T01:47";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(truncatedString, pos);
 }

 @Test
 public void testParseDateWithLongTimezoneOffset() throws ParseException {
 String dateString = "2010-03-26T01:47:10.000+14:00";
 ParsePosition pos = new ParsePosition(0);
 Date expectedDate = new Date(1234567890123L + (14 * 60 * 60 * 1000));
 Date actualDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(expectedDate.getTime(), actualDate.getTime());
 }
}