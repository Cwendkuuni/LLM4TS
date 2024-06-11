import org.junit.*;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        assertEquals("string", Util.stripLeadingHyphens("-string"));
        
        // Edge case: empty string
        assertEquals("", Util.stripLeadingHyphens(""));
        
        // Error scenario: null input
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        
        // Edge case: empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        
        // Error scenario: null input
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}