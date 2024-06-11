import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        assertTrue(dateFormat.looksLikeISO8601("2019-08-15T13:30:00.000Z"));
        assertFalse(dateFormat.looksLikeISO8601("15-08-2019"));
    }

    @Test
    public void testParseAsISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        String isoDate = "2019-08-15T13:30:00.000Z";
        assertEquals(isoDate, dateFormat.format(dateFormat.parse(isoDate)));
    }

    @Test
    public void testParseAsRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        String rfcDate = "Thu, 15 Aug 2019 13:30:00 GMT";
        assertEquals(rfcDate, dateFormat.format(dateFormat.parse(rfcDate)));
    }

    @Test
    public void testDateFormatException() {
        StdDateFormat dateFormat = new StdDateFormat();
        try {
            dateFormat.parse("2019-08-15T13:30:00.000"); // missing timezone
            fail("ParseException expected");
        } catch (ParseException e) {
            // success
        }
    }

    @Test
    public void testTimeZoneChange() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        assertEquals("PST", dateFormat.getTimeZone().getID());
    }

}