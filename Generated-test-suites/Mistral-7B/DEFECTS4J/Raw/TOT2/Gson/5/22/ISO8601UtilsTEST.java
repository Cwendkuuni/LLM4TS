import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_ValidDate_ReturnsExpectedString() {
        Date date = new GregorianCalendar(2022, 11, 28, 15, 30, 45).getTime();
        String formattedDate = ISO8601Utils.format(date);
        String expected = "2022-12-28T15:30:45Z";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_DateWithMillis_ReturnsExpectedString() {
        Date date = new GregorianCalendar(2022, 11, 28, 15, 30, 45, 123).getTime();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        String expected = "2022-12-28T15:30:45.123Z";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_DateWithCustomTimeZone_ReturnsExpectedString() {
        Date date = new GregorianCalendar(2022, 11, 28, 15, 30, 45).getTime();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/London"));
        String expected = "2022-12-28T14:30:45Z"; // London is GMT+0, so it should be the same as Zulu time but with different time zone identifier
        Assert.assertEquals(expected, formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormat_NullDate_ThrowsIllegalArgumentException() {
        String formattedDate = ISO8601Utils.format(null);
    }

    @Test
    public void testParse_ValidDateString_ReturnsExpectedDate() throws ParseException {
        String dateString = "2022-12-28T15:30:45Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        int expectedYear = calendar.get(Calendar.YEAR);
        int expectedMonth = calendar.get(Calendar.MONTH) + 1;
        int expectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int expectedHours = calendar.get(Calendar.HOUR_OF_DAY);
        int expectedMinutes = calendar.get(Calendar.MINUTE);
        int expectedSeconds = calendar.get(Calendar.SECOND);
        Assert.assertEquals(expectedYear, 2022);
        Assert.assertEquals(expectedMonth, 12);
        Assert.assertEquals(expectedDayOfMonth, 28);
        Assert.assertEquals(expectedHours, 15);
        Assert.assertEquals(expectedMinutes, 30);
        Assert.assertEquals(expectedSeconds, 45);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDateString_ThrowsParseException() throws ParseException {
        String invalidDateString = "2022-12-31T15:30:45Z"; // This date is invalid as it corresponds to a non-existent month (February has 28 days)
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidDateString, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullDateString_ThrowsIllegalArgumentException() {
        String nullDateString = null;
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(nullDateString, pos);
    }

}