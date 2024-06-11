import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test case with empty string
        assertNull(Util.stripLeadingHyphens(null));
        
        // Test cases with different number of leading hyphens
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("", Util.stripLeadingHyphens("-----"));
        
        // Test case with additional characters before and after hyphens
        assertEquals("prefix_foo", Util.stripLeadingHyphens("prefix_-foo"));
        assertEquals("foo_suffix", Util.stripLeadingHyphens("foo-_-suffix"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case with empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        
        // Test cases with no leading or trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo bar"));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("foo and bar"));
        assertEquals("\"foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar"));
        assertEquals("foo and bar\"", Util.stripLeadingAndTrailingQuotes("foo and bar\""));
        
        // Test cases with leading and trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("foo\" and bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
    }
}