import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.util.SimpleDateFormat;

public class ISO8601UtilsTest {

 private static final String UTC_ID = "UTC";
 private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

 @Test
 public void testFormat_BasicUseCase() {
 Date date = new GregorianCalendar(2023, 11, 12, 13, 14, 15, 167).getTime();
 String formattedDate = ISO8601Utils.format(date, true, TIMEZONE_UTC);
 assertEquals("2023-12-12T13:14:15.167Z", formattedDate);
 }

 @Test
 public void testFormat_NoMillis() {
 Date date = new GregorianCalendar(2023, 11, 12, 13, 14, 0, 0).getTime();
 String formattedDate = ISO8601Utils.format(date, false, TIMEZON_UTC);
 assertEquals("2023-12-12T13:14:00Z", formattedDate);
 }

 @Test
 public void testFormat_DifferentTimeZones() {
 Date date = new GregorianCalendar(2023, 11, 12, 13, 14, 15, 167).getTime();
 String formattedDateUTC = ISO8601Utils.format(date, true, TIMEZONE_UTC);
 String formattedDateEastern = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
 assertEquals("2023-12-12T19:14:15.167-05:00", formattedDateEastern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidDateFormat() {
 String invalidDate = "2023-12-12T13:14:15ZABC";
 ParsePosition pos = new ParsePosition(0);
 try {
 ISO8601Utils.parse(invalidDate, pos);
 fail("Expected an IllegalArgumentException, but got none.");
 } catch (ParseException e) {
 // pass
 }
 assertEquals("Failed to parse date [2023-12-12T13:14:15ZABC]: (java.text.ParseException)", e.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testParse_MissingYear() {
 String missingYear = "12-12-12T13:14:15Z";
 ParsePosition pos = new ParsePosition(0);
 try {
 ISO8601Utils.parse(missingYear, pos);
 fail("Expected a ParseException, but got none.");
 } catch (ParseException e) {
 // pass
 }
 assertEquals("Failed to parse date [" + missingYear + "]: (java.text.ParseException)", e.getMessage());
 }

 @Test
 public void testParse_DefaultTimeZone() {
 String dateString = "2023-12-12T13:14:15.167Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString), parsedDate);
 }

 @Test
 public void testParse_CustomTimeZone() {
 String dateString = "2023-12-12T13:14:15.167Z";
 TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0), customTimeZone);
 assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'ZZ'").parse(dateString), parsedDate);
 }
}