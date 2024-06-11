import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertNotNull(instance);
    }

    @Test
    public void testFormatObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        fastDateFormat.format(new Date(), buffer, null);
        assertEquals("2021-12-31", buffer.toString());
    }

    @Test
    public void testParseObject() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Date date = fastDateFormat.parse("2021-12-31");
        assertEquals("Fri Dec 31 00:00:00 UTC 2021", date.toString());
    }

    // Add more test cases for the remaining methods

}