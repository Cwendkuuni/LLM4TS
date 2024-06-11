import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test case for null input
        assertNull(Util.stripLeadingHyphens(null));
        
        // Test cases for input starting with --
        assertEquals("f", Util.stripLeadingHyphens("--f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("--foo-"));
        
        // Test cases for input starting with -
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("-foo"));
        
        // Test case for input without hyphens at the beginning
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case for empty input
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        
        // Test cases for input with leading and trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo ", Util.stripLeadingAndTrailingQuotes("\"foo \""));
        assertEquals(" foo", Util.stripLeadingAndTrailingQuotes(" \"foo\""));
        assertEquals("foo\" and \"bar", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        
        // Test cases for input without leading and trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo ", Util.stripLeadingAndTrailingQuotes("foo "));
        assertEquals(" foo", Util.stripLeadingAndTrailingQuotes(" foo"));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("foo and bar"));
    }
}