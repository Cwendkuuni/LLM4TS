import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDate() {
 Date date = new GregorianCalendar(2022, 11, 12, 15, 30, 45, 678).getTime();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("2022-12-12T15:30:45.678Z", formattedDate);
 }

 @Test
 public void testFormatDateWithTimeZone() {
 Date date = new GregorianCalendar(2022, 11, 12, 15, 30, 45, 678, TimeZone.getTimeZone("America/New_York")).getTime();
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
 assertEquals("2022-12-12T15:30:45.678-05:00", formattedDate);
 }

 @Test
 public void testFormatDateWithoutMilliseconds() {
 Date date = new GregorianCalendar(2022, 11, 12, 15, 30).getTime();
 String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
 assertEquals("2022-12-12T15:30:00Z", formattedDate);
 }

 @Test
 public void testParseDate() {
 String dateString = "2022-12-12T15:30:45.678Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 12, 15, 30, 45, 678).getTime(), parsedDate);
 assertEquals(dateString.length(), pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() {
 String dateString = "2022-12-12T15:30:45.678W"; // Invalid timezone indicator
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateFormat() {
 String dateString = "2022-12-12T15:30:45Z"; // Missing milliseconds
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateFormatWithMillis() {
 String dateString = "2022-12-12T15:30:45"; // Missing timezone indicator
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test
 public void testParseDateWithTimeZone() {
 String dateString = "2022-12-12T15:30:45.678-05:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 12, 15, 30, 45, 678, TimeZone.getTimeZone("America/New_York")).getTime(), parsedDate);
 assertEquals(dateString.length(), pos.getIndex());
 }

}