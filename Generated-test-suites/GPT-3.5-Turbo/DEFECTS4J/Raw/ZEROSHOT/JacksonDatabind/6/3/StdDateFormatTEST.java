import org.junit.Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        String dateString = "2021-01-01T10:30:00.000Z";
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        String dateString = "Fri, 01 Jan 2021 10:30:00 GMT";
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test
    public void testFormatISO8601() {
        Date date = new Date();
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = dateFormat.format(date);
        assertNotNull(dateString);
    }

    @Test
    public void testFormatRFC1123() {
        Date date = new Date();
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateFormat.setLocale(Locale.US);
        String dateString = dateFormat.format(date);
        assertNotNull(dateString);
    }
}