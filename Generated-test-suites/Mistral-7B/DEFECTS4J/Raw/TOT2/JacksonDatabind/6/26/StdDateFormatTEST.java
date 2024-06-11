import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 @Test
 public void testParseISO8601() throws Exception {
 String dateString = "2022-12-31T23:59:59.123Z";
 Date expectedDate = new Date(1671375999123L); // Milliseconds since 1970-01-01 00:00:00.000Z
 Date actualDate = StdDateFormat.instance.parse(dateString);
 assertEquals("Incorrect date parsing for ISO8601 format", expectedDate, actualDate);
 }

 @Test
 public void testParseISO8601WithTimeZone() throws Exception {
 String dateString = "2022-12-31T23:59:59.123+01:00";
 Date expectedDate = new Date(1671378599123L); // Milliseconds since 1970-01-01 00:00:00.000+01:00
 TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
 Date actualDate = StdDateFormat.instance.parse(dateString, timeZone);
 assertEquals("Incorrect date parsing for ISO8601 format with timezone", expectedDate, actualDate);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601() throws Exception {
 String dateString = "2022-12-31T23:59:59.123A";
 Date actualDate = null;
 try {
 actualDate = StdDateFormat.instance.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 assertEquals("Unexpected parse result for invalid ISO8601 format", null, actualDate);
 }

 @Test
 public void testParseRFC1123() throws Exception {
 String dateString = "Sat, 31 Dec 2022 23:59:59 GMT";
 Date expectedDate = new Date(1671375999000L); // Milliseconds since 1970-01-01 00:00:00.000 GMT
 Date actualDate = StdDateFormat.instance.parse(dateString);
 assertEquals("Incorrect date parsing for RFC1123 format", expectedDate, actualDate);
 }

 @Test
 public void testParseRFC1123WithTimeZone() throws Exception {
 String dateString = "Sat, 31 Dec 2022 23:59:59 EST";
 Date expectedDate = new Date(1671372399000L); // Milliseconds since 1970-01-01 00:00:00.000 EST (Eastern Standard Time)
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 Date actualDate = StdDateFormat.instance.parse(dateString, timeZone);
 assertEquals("Incorrect date parsing for RFC1123 format with timezone", expectedDate, actualDate);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidRFC1123() throws Exception {
 String dateString = "Sat, 31 Dec 2022 23:59:59 WST";
 Date actualDate = null;
 try {
 actualDate = StdDateFormat.instance.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 assertEquals("Unexpected parse result for invalid RFC1123 format", null, actualDate);
 }

 @Test
 public void testFormat() throws Exception {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date(1671375999123L)); // Milliseconds since 1970-01-01 00:00:00.000Z
 Date date = calendar.getTime();
 String expectedDateString = "2022-12-31T23:59:59.123Z";
 String actualDateString = StdDateFormat.instance.format(date);
 assertEquals("Incorrect date formatting", expectedDateString, actualDateString);
 }

 @Test
 public void testFormatWithTimeZone() throws Exception {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date(1671378599123L)); // Milliseconds since 1970-01-01 00:00:00.000+01:00
 Date date = calendar.getTime();
 TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
 String expectedDateString = "2022-12-31T23:59:59.123+01:00";
 String actualDateString = StdDateFormat.instance.format(date, timeZone);
 assertEquals("Incorrect date formatting with timezone", expectedDateString, actualDateString);
 }
}