import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateUTC() {
        Date date = new GregorianCalendar(2022, 11, 30, 16, 30, 23, 500).getTime();
        String formattedDate = ISO8601Utils.format(date);
        String expected = "2022-12-01T16:30:23.5Z";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatDateWithMillisecondsAndTimeZone() {
        Date date = new GregorianCalendar(2022, 11, 30, 16, 30, 23, 500).getTime();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        String formattedDate = ISO8601Utils.format(date, true, timeZone);
        String expected = "2022-12-01T17:30:23.500+01:00";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatDateNoTimeZone() {
        Date date = new GregorianCalendar(2022, 11, 30, 16, 30, 23, 500).getTime();
        String formattedDate = ISO8601Utils.format(date, false, null);
        String expected = "2022-12-01T16:30:23Z";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test
    public void testParseDateValidFormat() throws ParseException {
        String dateString = "2022-12-01T16:30:23.5Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 11, 1, 16, 30, 23, 500);
        Assert.assertEquals(expectedCalendar.getTime(), parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() throws ParseException {
        String dateString = "2022-12-01T16:30:23.5Z2";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingTimeZone() throws ParseException {
        String dateString = "2022-12-01T16:30:23.5";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidTimeZone() throws ParseException {
        String dateString = "2022-12-01T16:30:23.5+13:30";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseDateOffsetOutOfRange() throws ParseException {
        String dateString = "2022-12-01T16:30:23.5+13:60";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateInvalidTimeZoneId() {
        String dateString = ISO8601Utils.format(new Date(), true, new SimpleDateFormat("GMT+00:00"));
        Assert.fail();
    }

}