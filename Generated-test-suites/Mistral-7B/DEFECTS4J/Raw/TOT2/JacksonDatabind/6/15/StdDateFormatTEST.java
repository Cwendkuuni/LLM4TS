import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 private static final String ISO_8601 = "2022-12-31T23:59:59.000Z";
 private static final String RFC_1123 = "Fri, 31 Dec 2022 23:59:59 GMT";
 private static final String PLAIN_DATE = "2022-12-31";
 private static final String ISO_8601_WITH_TIMEZONE = "2022-12-31T23:59:59.000+01:00";
 private static final String ISO_8601_WITH_MISSING_COLON = "2022-12-31T235959.000Z";
 private static final String RFC_1123_WITH_MISSING_COLON = "Fri, 31 Dec 2022 2359 GMT";
 private static final String INVALID_DATE = "invalid_date";
 private static final String NULL_DATE_STRING = null;

 @Test
 public void testParseISO8601() throws ParseException {
 Date date = StdDateFormat.getInstance().parse(ISO_8601);
 assertNotNull(date);
 assertEquals(new Date(Long.parseLong("1671139999000")), date);
 }

 @Test
 public void testParseRFC1123() throws ParseException {
 Date date = StdDateFormat.getInstance().parse(RFC_1123);
 assertNotNull(date);
 assertEquals(new Date(Long.parseLong("1262304000000")), date);
 }

 @Test
 public void testParsePlainDate() throws ParseException {
 Date date = StdDateFormat.getInstance().parse(PLAIN_DATE);
 assertNotNull(date);
 assertEquals(new Date(Long.parseLong("1234567200000")), date);
 }

 @Test(expected = ParseException.class)
 public void testParseISO8601WithMissingColon() throws ParseException {
 StdDateFormat.getInstance().parse(ISO_8601_WITH_MISSING_COLON);
 fail("Expected ParseException but did not receive one.");
 }

 @Test(expected = ParseException.class)
 public void testParseRFC1123WithMissingColon() throws ParseException {
 StdDateFormat.getInstance().parse(RFC_1123_WITH_MISSING_COLON);
 fail("Expected ParseException but did not receive one.");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 StdDateFormat.getInstance().parse(INVALID_DATE);
 fail("Expected ParseException but did not receive one.");
 }

 @Test(expected = ParseException.class)
 public void testParseNullDateString() throws ParseException {
 try {
 StdDateFormat.getInstance().parse(NULL_DATE_STRING);
 fail("Expected ParseException but did not receive one.");
 } catch (NullPointerException e) {
 // expected
 }
}

 @Test
 public void testFormatISO8601() throws ParseException {
 Date date = StdDateFormat.getInstance().parse(ISO_8601);
 String formattedDate = StdDateFormat.getInstance().format(date);
 assertEquals(ISO_8601, formattedDate);
 }

 @Test
 public void testFormatRFC1123() throws ParseException {
 Date date = StdDateFormat.getInstance().parse(RFC_1123);
 String formattedDate = StdDateFormat.getInstance().format(date);
 assertEquals(RFC_1123, formattedDate);
 }

 @Test
 public void testFormatPlainDate() throws ParseException {
 Date date = StdDateFormat.getInstance().parse(PLAIN_DATE);
 String formattedDate = StdDateFormat.getInstance().format(date);
 assertEquals(PLAIN_DATE, formattedDate);
 }

 @Test
 public void testFormatISO8601WithTimezone() throws ParseException {
 TimeZone cest = TimeZone.getTimeZone("CEST");
 StdDateFormat formatWithTimezone = new StdDateFormat(cest, Locale.US);
 Date date = formatWithTimezone.parse(ISO_8601_WITH_TIMEZONE);
 String formattedDate = formatWithTimezone.format(date);
 assertEquals("2022-12-31T23:59:59.000+01:00", formattedDate);
 }

}