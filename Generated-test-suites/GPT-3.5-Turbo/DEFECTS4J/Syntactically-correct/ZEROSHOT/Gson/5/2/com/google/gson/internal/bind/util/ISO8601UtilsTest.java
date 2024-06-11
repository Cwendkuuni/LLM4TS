import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        // Add assertions to test the correctness of the formatted date
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        // Add assertions to test the correctness of the formatted date with milliseconds
    }

    @Test
    public void testFormatWithTimezone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
        // Add assertions to test the correctness of the formatted date with specified timezone
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        // Add assertions to test the correctness of the parsed date
    }
}