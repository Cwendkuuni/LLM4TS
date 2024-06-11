import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        assertNotNull(FastDateFormat.getInstance());
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss"));
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT")));
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.US));
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US));
    }

    @Test
    public void testGetDateInstance() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.MEDIUM));
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US));
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT")));
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US));
    }

    @Test
    public void testGetTimeInstance() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM));
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US));
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT")));
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US));
    }

    @Test
    public void testGetDateTimeInstance() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT));
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US));
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT")));
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US));
    }

    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertEquals("2021-01-01 12:00:00", format.format(new Date(1609478400000))); // Typical use case
    }

    // Add more specific test cases for other methods
    
}