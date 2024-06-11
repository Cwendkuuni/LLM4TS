import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime();

        String formattedDate = ISO8601Utils.format(date);
        Assert.assertEquals("2023-01-01T00:00:00Z", formattedDate);

        Date dateWithMilliseconds = new GregorianCalendar(2023, Calendar.JANUARY, 1, 0, 0, 123).getTime();

        String formattedDateWithMilliseconds = ISO8601Utils.format(dateWithMilliseconds, true, TimeZone.getDefault());
        Assert.assertEquals("2023-01-01T00:00:123Z", formattedDateWithMilliseconds);

        Date dateWithTimeZone = new GregorianCalendar(2023, Calendar.JANUARY, 1, 12, 34, 56, 789, TimeZone.getTimeZone("Europe/London")).getTime();

        String formattedDateWithTimeZone = ISO8601Utils.format(dateWithTimeZone, true, TimeZone.getDefault());
        Assert.assertEquals("2023-01-01T11:34:56.789+00:00", formattedDateWithTimeZone);
    }

    @Test
    public void testParse() throws Exception {
        String dateString = "2023-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), parsedDate);
        Assert.assertEquals(0, pos.getIndex());

        String dateStringWithMilliseconds = "2023-01-01T00:00:123Z";
        pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(dateStringWithMilliseconds, pos);
        Assert.assertEquals(new GregorianCalendar(2023, Calendar.JANUARY, 1, 0, 0, 123).getTime(), parsedDate);
        Assert.assertEquals(dateStringWithMilliseconds.length(), pos.getIndex());

        String dateStringWithTimeZone = "2023-01-01T11:34:56.789+01:00";
        pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(dateStringWithTimeZone, pos);
        Calendar expectedCalendar = new GregorianCalendar(2023, Calendar.JANUARY, 1, 12, 34, 56, 789, TimeZone.getTimeZone("Europe/London"));
        Assert.assertEquals(expectedCalendar.getTime(), parsedDate);
        Assert.assertEquals(dateStringWithTimeZone.length(), pos.getIndex());

        String incorrectDateString = "2023-01-01T11:34:56.789A";
        pos = new ParsePosition(0);
        try {
            parsedDate = ISO8601Utils.parse(incorrectDateString, pos);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Text 'A' cannot be parsed at index 13", e.getMessage());
        }
    }
}