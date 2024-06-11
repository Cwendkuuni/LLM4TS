import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        
        assertTrue(dateFormat.looksLikeISO8601("2021-05-25"));
        assertFalse(dateFormat.looksLikeISO8601("05-25-2021"));
    }

    @Test
    public void testParseAsISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);

        assertNotNull(dateFormat.parseAsISO8601("2021-05-25T09:30:00.000Z", pos));
    }

    @Test
    public void testParseAsRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);

        assertNotNull(dateFormat.parseAsRFC1123("Tue, 25 May 2021 09:30:00 GMT", pos));
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = new StdDateFormat();

        try {
            dateFormat.parse("2021/05/25");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Can not parse date \"2021/05/25\": not compatible with any of standard forms (yyyy-MM-dd'T'HH:mm:ss.SSSZ, \"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\", EEE, dd MMM yyyy HH:mm:ss zzz, yyyy-MM-dd)", e.getMessage());
        }
    }
}