import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        String isoDate = "2021-03-15T10:30:00.000Z";
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(isoDate), dateFormat.parse(isoDate));
    }

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        assertTrue(dateFormat.looksLikeISO8601("2021-03-15"));
    }

    @Test
    public void testHasTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        assertTrue(dateFormat.hasTimeZone("2021-03-15T10:30:00.000+03:00"));
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        String rfcDate = "Tue, 15 Mar 2021 10:30:00 GMT";
        assertEquals(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(rfcDate), dateFormat.parse(rfcDate));
    }

}