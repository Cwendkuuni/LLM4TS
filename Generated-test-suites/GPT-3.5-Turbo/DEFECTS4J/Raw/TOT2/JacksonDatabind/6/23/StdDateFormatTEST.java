import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;

public class StdDateFormatTest {

    @Test
    public void testSetTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        assertEquals(TimeZone.getTimeZone("GMT+1"), sdf.getTimeZone());
    }

    @Test
    public void testParse_ISO8601() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("2022-01-01T12:00:00.000Z");
        assertEquals(1641024000000L, date.getTime());
    }

    @Test
    public void testParse_RFC1123() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("Sat, 01 Jan 2022 12:00:00 GMT");
        assertEquals(1641024000000L, date.getTime());
    }

    @Test
    public void testFormat_ISO8601() {
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date(1641024000000L);
        assertEquals("2022-01-01T12:00:00.000Z", sdf.format(date));
    }

    @Test
    public void testFormat_RFC1123() {
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date(1641024000000L);
        assertEquals("Sat, 01 Jan 2022 12:00:00 GMT", sdf.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDate() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        sdf.parse("2022-01-01T12:00:00.000");
    }
}