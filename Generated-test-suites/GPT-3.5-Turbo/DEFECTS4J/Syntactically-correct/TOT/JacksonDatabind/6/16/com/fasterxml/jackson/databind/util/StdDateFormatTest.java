import org.junit.Test;
import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testParse() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        
        // Typical use case
        String dateStr = "2019-12-01T10:30:00.000Z";
        assertEquals(dateFormat.parse(dateStr).getTime(), 1575172200000L);
        
        // Edge case: date with no time
        dateStr = "2019-12-01";
        assertEquals(dateFormat.parse(dateStr).getTime(), 1575148800000L);
        
        // Error scenario: invalid date format
        dateStr = "2019/12/01";
        try {
            dateFormat.parse(dateStr);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
            assertEquals(e.getMessage(), "Unparseable date: \"2019/12/01\"");
        }
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        
        // Typical use case
        long timestamp = 1575172200000L;
        String expected = "2019-12-01T10:30:00.000Z";
        assertEquals(dateFormat.format(new Date(timestamp)), expected);
        
        // Edge case: date with no time
        timestamp = 1575148800000L;
        expected = "2019-12-01";
        assertEquals(dateFormat.format(new Date(timestamp)), expected);
    }
}