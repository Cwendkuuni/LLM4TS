import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

    @Test
    public void testFormatValidDate() {
        Date validDate = new GregorianCalendar(2022, 11, 30, 15, 30, 0).getTime();
        String formattedDate = ISO8601Utils.format(validDate);
        String expectedFormat = "2022-12-30T15:30:00Z";
        assertEquals(expectedFormat, formattedDate);
    }

    @Test
    public void testFormatValidDateWithMillis() {
        Date validDate = new GregorianCalendar(2022, 11, 30, 15, 30, 567).getTime();
        String formattedDate = ISO8601Utils.format(validDate, true);
        String expectedFormat = "2022-12-30T15:30:567Z";
        assertEquals(expectedFormat, formattedDate);
    }

    @Test
    public void testFormatValidDateWithTimezone() {
        Date validDate = new GregorianCalendar(2022, 11, 30, 15, 30, 0, 1, TimeZone.getTimeZone("America/New_York")).getTime();
        String formattedDate = ISO8601Utils.format(validDate, true, TimeZone.getTimeZone("America/New_York"));
        String expectedFormat = "2022-12-30T15:30:00-05:00";
        assertEquals(expectedFormat, formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatNullDate() {
        String result = ISO8601Utils.format(null);
        fail("Should throw IllegalArgumentException for null date");
    }

    @Test
    public void testParseValidDate() {
        String dateString = "2022-12-30T15:30:00Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        int expectedYear = 2022;
        int expectedMonth = 12;
        int expectedDay = 30;
        int expectedHour = 15;
        int expectedMinute = 30;
        int expectedSecond = 0;
        int expectedMillisecond = 0;
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
        assertEquals(expectedMillisecond, calendar.get(Calendar.MILLISECOND));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidDateFormat() {
        String dateString = "2022-12-30T15:30:00AAA";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw IllegalArgumentException for invalid date format");
    }

    @Test
    public void testParseDateWithTimezone() {
        String dateString = "2022-12-30T15:30:00-05:00";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        int expectedYear = 2022;
        int expectedMonth = 12;
        int expectedDay = 30;
        int expectedHour = 15;
        int expectedMinute = 30;
        int expectedSecond = 0;
        int expectedMillisecond = 0;
        int expectedOffset = -18000000; // 5 hours * 60 minutes * 60000 milliseconds
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
        assertEquals(expectedMillisecond, calendar.get(Calendar.MILLISECOND));
        assertEquals(expectedOffset, calendar.get(Calendar.ZONE_OFFSET));
    }
}