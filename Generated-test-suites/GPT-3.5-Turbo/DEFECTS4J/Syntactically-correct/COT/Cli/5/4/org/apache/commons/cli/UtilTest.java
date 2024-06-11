import org.junit.*;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Basic test cases
        assertEquals("string", Util.stripLeadingHyphens("-string"));
        assertEquals("string", Util.stripLeadingHyphens("--string"));

        // Additional test cases for edge cases and exceptions
        assertEquals("", Util.stripLeadingHyphens("--")); // Empty string
        assertEquals("-", Util.stripLeadingHyphens("---")); // Single hyphen
        assertEquals("", Util.stripLeadingHyphens("")); // Empty string
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Basic test cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("'one two'"));

        // Additional test cases for edge cases and exceptions
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"")); // Single leading quote
        assertEquals("", Util.stripLeadingAndTrailingQuotes("'")); // Single leading quote
        assertEquals("", Util.stripLeadingAndTrailingQuotes("")); // Empty string
    }
    
    // 
    // Test cases for other methods go here
    // 
}