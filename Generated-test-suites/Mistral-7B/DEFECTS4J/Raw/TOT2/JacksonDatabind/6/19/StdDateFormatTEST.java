import org.junit.Assert;import org.junit.Test;import java.text.DateFormat;import java.text.ParseException;import java.util.Date;import java.util.TimeZone;

public class StdDateFormatTest {

 private static final StdDateFormat stdDateFormat = StdDateFormat.instance;

 @Test
 public void testParseISO8601Date() {
 String iso8601Date = "2022-12-31T23:59:59.123Z";
 Date parsedDate = stdDateFormat.parse(iso8601Date);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseISO8601DateWithTimezone() {
 String iso8601Date = "2022-12-31T23:59:59.123+01:00";
 Date parsedDate = stdDateFormat.parse(iso8601Date);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals("Europe/Paris", parsedDate.getTimezone().getID());
 }

 @Test
 public void testParseISO8601DateWithoutTimezone() {
 String iso8601Date = "2022-12-31T23:59:59.123";
 Date parsedDate = stdDateFormat.parse(iso8601Date);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals(0, parsedDate.getTimezone().getOffset(parsedDate.getTime()));
 }

 @Test
 public void testParseRFC1123Date() {
 String rfc1123Date = "Sat, 31 Dec 2022 23:59:59 GMT";
 Date parsedDate = stdDateFormat.parse(rfc1123Date);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals(0, parsedDate.getTimezone().getOffset(parsedDate.getTime()));
 }

 @Test
 public void testParseInvalidDate() {
 String invalidDate = "2022-13-31";
 TryCatch.call(() -> stdDateFormat.parse(invalidDate), ParseException.class);
 }

 @Test
 public void testParseDateWithLeadingZeros() {
 String dateWithLeadingZeros = "2022-01-01";
 Date parsedDate = stdDateFormat.parse(dateWithLeadingZeros);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseDateWithNegativeYear() {
 String dateWithNegativeYear = "-2022-12-31";
 TryCatch.call(() -> stdDateFormat.parse(dateWithNegativeYear), ParseException.class);
 }

 @Test
 public void testParseDateWithLongerFormat() {
 String longerFormat = "Sat, 31 Dec 2022 23:59:59 GMT";
 TryCatch.call(() -> stdDateFormat.parse(longerFormat), ParseException.class);
 }

 @Test
 public void testFormatDate() {
 Date date = new Date(1670259200000L);
 String formattedDate = stdDateFormat.format(date);
 Assert.assertEquals("Sat Dec 31 00:00:00 GMT 2022", formattedDate);
 }

 @Test
 public void testFormatDateWithCustomLocale() {
 Locale customLocale = new Locale("fr", "FR");
 Date date = new Date(1670259200000L);
 String formattedDate = stdDateFormat.format(date, new StringBuffer(), customLocale);
 Assert.assertEquals("samedi 31 déc. 2022 00:00:00 GMT", formattedDate.toString());
 }

 @Test
 public void testFormatDateWithCustomTimezone() {
 Date date = new Date(1670259200000L);
 TimeZone customTimezone = TimeZone.getTimeZone("Europe/Moscow");
 String formattedDate = stdDateFormat.format(date, new StringBuffer(), null).toString();
 stdDateFormat.setTimeZone(customTimezone);
 String formattedDateWithCustomTimezone = stdDateFormat.format(date, new StringBuffer(), null).toString();
 Assert.assertEquals("samedi 31 déc. 2022 01:22:00 MSK", formattedDateWithCustomTimezone);
 }
}