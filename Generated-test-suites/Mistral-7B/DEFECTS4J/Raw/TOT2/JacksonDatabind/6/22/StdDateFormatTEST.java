import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 private static final StdDateFormat dateFormat = StdDateFormat.instance;

 @Test
 public void testParseISO8601Date() throws ParseException {
 String dateString = "2022-01-01T12:34:56.789Z";
 Date date = dateFormat.parse(dateString);
 assertEquals("2022-01-01 12:34:56.789", dateFormat.format(date));
 }

 @Test
 public void testParseISO8601DateWithTimezoneOffset() throws ParseException {
 String dateString = "2022-01-01T12:34:56.789-04:00";
 Date date = dateFormat.parse(dateString);
 assertEquals("2022-01-01 16:34:56.789", dateFormat.format(date));
 }

 @Test
 public void testParseISO8601DatePlain() throws ParseException {
 String dateString = "2022-01-01";
 Date date = dateFormat.parse(dateString);
 assertEquals("2022-01-01", dateFormat.format(date));
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 String dateString = "2022-01-02-03";
 dateFormat.parse(dateString);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseDateOutOfRange() throws ParseException {
 String dateString = "1900-02-30";
 dateFormat.parse(dateString);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidFormat() throws ParseException {
 String dateString = "2022/01/01";
 dateFormat.parse(dateString);
 fail("Expected ParseException");
 }

 @Test
 public void testFormatDate() {
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testSetTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC+1");
 StdDateFormat timeZoneDateFormat = new StdDateFormat(timeZone, Locale.US);
 String dateString = "2022-01-01T12:34:56.789Z";
 Date date = timeZoneDateFormat.parse(dateString);
 assertEquals("2022-01-01 13:34:56.789", timeZoneDateFormat.format(date));
 }

 @Test
 public void testSetLocale() {
 Locale locale = new Locale("fr", "FR");
 StdDateFormat localeDateFormat = new StdDateFormat(null, locale);
 String dateString = "2022-01-01T12:34:56.789Z";
 Date date = localeDateFormat.parse(dateString);
 assertEquals("01-jan.-2022 12:34:56.789", localeDateFormat.format(date));
 }
}