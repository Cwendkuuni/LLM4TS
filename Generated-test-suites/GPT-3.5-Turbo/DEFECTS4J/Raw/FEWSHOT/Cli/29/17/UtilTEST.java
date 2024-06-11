import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str is null
        assertNull(Util.stripLeadingHyphens(null));
        
        // Test case 2: str starts with "--"
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        
        // Test case 3: str starts with "-"
        assertEquals("bar", Util.stripLeadingHyphens("-bar"));
        
        // Test case 4: str does not start with hyphen but contains hyphen
        assertEquals("foo-bar", Util.stripLeadingHyphens("foo-bar"));
        
        // Test case 5: str does not start with hyphen and does not contain hyphen
        assertEquals("foobar", Util.stripLeadingHyphens("foobar"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts and ends with quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        
        // Test case 2: str starts with quotes but does not end with quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        
        // Test case 3: str does not start with quotes but ends with quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        
        // Test case 4: str does not start and end with quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}