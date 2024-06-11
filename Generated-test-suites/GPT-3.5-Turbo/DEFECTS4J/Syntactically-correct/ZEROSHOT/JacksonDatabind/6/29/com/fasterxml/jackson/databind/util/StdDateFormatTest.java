import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        
        // Test cases that should return true
        assertTrue(dateFormat.looksLikeISO8601("2000-01-01"));
        assertTrue(dateFormat.looksLikeISO8601("2000-01-01T12:00:00Z"));
        
        // Test cases that should return false
        assertFalse(dateFormat.looksLikeISO8601("2000/01/01"));
        assertFalse(dateFormat.looksLikeISO8601("2000-01-01T12:00:00"));
    }
    
    @Test
    public void testParse() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        
        // Test cases for ISO8601 format
        assertNotNull(dateFormat.parse("2000-01-01"));
        
        // Test cases for RFC1123 format
        assertNotNull(dateFormat.parse("Sat, 1 Jan 2000 12:00:00 GMT"));
    }
    
    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        
        // Test cases for ISO8601 format
        assertEquals("2000-01-01T12:00:00Z", dateFormat.format(new Date()));
        
        // Test cases for RFC1123 format
        assertEquals("Sat, 1 Jan 2000 12:00:00 GMT", dateFormat.format(new Date()));
    }
}