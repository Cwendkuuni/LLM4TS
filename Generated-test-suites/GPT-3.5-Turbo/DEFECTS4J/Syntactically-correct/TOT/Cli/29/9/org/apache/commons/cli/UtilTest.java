import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use cases
        assertEquals("string", Util.stripLeadingHyphens("-string"));
        assertEquals("string", Util.stripLeadingHyphens("--string"));
        
        // Edge cases
        assertEquals("", Util.stripLeadingHyphens("-"));
        assertEquals("", Util.stripLeadingHyphens("--"));
        
        // Error scenarios
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("string-", Util.stripLeadingHyphens("string-"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        
        // Edge cases
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        
        // Error scenarios
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("&one two", Util.stripLeadingAndTrailingQuotes("&one two"));
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
    }
}