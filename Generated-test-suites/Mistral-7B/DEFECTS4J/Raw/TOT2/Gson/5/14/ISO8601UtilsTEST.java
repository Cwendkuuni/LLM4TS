import org.junit.Test;
import java.text.ParseException;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ISO8601UtilsTest {

 private static final String TEST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

 @Test
 public void testFormat_DateWithMilliseconds_ReturnsCorrectFormat() {
 Date date = new Date(System.currentTimeMillis() - 1000L);
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
 assertEquals("Expected format to match: yyyy-MM-dd'T'HH:mm:ss.SSSZ", TEST_DATE_FORMAT, formattedDate);
 }

 @Test
 public void testFormat_DateWithoutMilliseconds_ReturnsCorrectFormat() {
 Date date = new Date(System.currentTimeMillis() - 1000L);
 String formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
 assertEquals("Expected format to match: yyyy-MM-dd'T'HH:mm:ssZ", TEST_DATE_FORMAT.substring(0, TEST_DATE_FORMAT.length() - 3), formattedDate);
 }

 @Test
 public void testFormat_DateWithUTCTimezone_ReturnsCorrectFormat() {
 Date date = new Date(System.currentTimeMillis() - 1000L);
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone(ISO8601Utils.UTC_ID));
 assertEquals("Expected format to match: yyyy-MM-dd'T'HH:mm:ss.SSSZ", TEST_DATE_FORMAT, formattedDate);
 }

 @Test
 public void testFormat_NullDate_ThrowsNullPointerException() {
 try {
 ISO8601Utils.format(null, true, TimeZone.getDefault());
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testParse_ValidDateString_ReturnsCorrectDate() throws ParseException {
 String dateString = "2023-03-15T14:30:22.123Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertNotNull("Expected parsed date to not be null", parsedDate);
 assertEquals("Expected parsed date to match: Wed Mar 15 14:30:22 CET 2023", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(parsedDate), new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(new Date()));
 }

 @Test
 public void testParse_InvalidDateString_ThrowsParseException() throws ParseException {
 try {
 ISO8601Utils.parse("InvalidDateString", new ParsePosition(0));
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParse_DateWithOffsetTimezone_ReturnsCorrectDate() throws ParseException {
 String dateString = "2023-03-15T14:30:22.123+01:00";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals("Expected parsed date to match: Wed Mar 15 15:30:22 CEST 2023", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(parsedDate), new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(new Date(System.currentTimeMillis() - 3600000L)));
 }

 @Test
 public void testParse_DateWithUTCAndMilliseconds_ReturnsCorrectDate() throws ParseException {
 String dateString = "2023-03-15T14:30:22.123Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals("Expected parsed date to match: Wed Mar 15 14:30:22 UTC 2023", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(parsedDate), new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(new Date()));
 }

 @Test
 public void testParse_DateWithMissingTimezone_ReturnsCorrectDateUsingDefaultTimezone() throws ParseException {
 String dateString = "2023-03-15T14:30:22";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals("Expected parsed date to match: Wed Mar 15 14:30:22 CEST 2023", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(parsedDate), new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(new Date()));
 }

 @Test
 public void testParse_DateWithMissingTimeAndTimezone_ThrowsParseException() throws ParseException {
 try {
 ISO8601Utils.parse("2023-03-15", new ParsePosition(0));
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }
}