import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Assert.assertTrue("Incorrect date format", formattedDate.matches(expectedFormat));
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Assert.assertTrue("Incorrect date format with milliseconds", formattedDate.matches(expectedFormat));
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'%+0330'"; // IST timezone offset is +03:30
        Assert.assertTrue("Incorrect date format with timezone", formattedDate.matches(expectedFormat));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithInvalidFormat() {
        String dateString = "2022-01-31-12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithMissingTimeZone() {
        String dateString = "2022-12-31T23:59:59";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithInvalidTimeZoneFormat() {
        String dateString = "2022-12-31T23:59:59+ABC";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseDateWithValidFormat() {
        String dateString = "2022-12-31T23:59:59.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        long expectedTime = 1672514399123L;
        long actualTime = parsedDate.getTime();
        Assert.assertEquals("Incorrect parsed date", expectedTime, actualTime);
    }
}