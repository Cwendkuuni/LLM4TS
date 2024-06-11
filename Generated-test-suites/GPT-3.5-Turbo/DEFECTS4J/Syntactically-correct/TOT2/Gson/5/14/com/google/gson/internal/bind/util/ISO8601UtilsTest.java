import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date(1630659301000L); // 2021-09-03T15:08:21Z
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-09-03T15:08:21Z", formattedDate);
    }

    @Test
    public void testFormatDateWithMillis() {
        Date date = new Date(1630659301234L); // 2021-09-03T15:08:21.234Z
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("2021-09-03T15:08:21.234Z", formattedDate);
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date(1630659301000L); // 2021-09-03T15:08:21Z
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT+03:00"));
        assertEquals("2021-09-03T18:08:21.000+03:00", formattedDate);
    }

    @Test
    public void testParseValidDate() throws ParseException {
        String isoDate = "2021-09-03T15:08:21Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String isoDate = "2021/09/03T15:08:21Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String isoDate = "2021-09-03T15:08:21GMT+03:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, pos);
    }

}