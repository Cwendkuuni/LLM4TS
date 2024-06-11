import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        assertNotNull(FastDateFormat.getInstance());
    }

    @Test
    public void testGetInstanceWithPattern() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd"));
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT")));
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", Locale.US));
    }

    // Additional test cases for getInstance methods can be added here

    @Test
    public void testGetDateInstance() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.SHORT));
    }

    @Test
    public void testGetTimeInstance() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.SHORT));
    }

    @Test
    public void testGetDateTimeInstance() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT));
    }

    // Additional test cases for getDateInstance, getTimeInstance, and getDateTimeInstance methods can be added here

    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("2021-10-25", format.format(1635158400000L)); // Edge case
        // Add more typical and edge cases for format methods here
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(1635158400000L, format.parse("2021-10-25").getTime()); // Typical case
        // Add more typical and edge cases for parse methods here
    }

    // Additional test cases for other public methods in FastDateFormat class can be added here

}