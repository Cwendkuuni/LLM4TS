import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test when the input starts with "--"
        assertEquals("f", Util.stripLeadingHyphens("--f"));
        
        // Test when the input starts with "-"
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        
        // Test when the input does not start with "-" or "--"
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
        
        // Test when the input is null
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test when the input starts and ends with double quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        
        // Test when the input starts and ends with double quotes with spaces in between
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        
        // Test when the input starts with double quotes and ends without double quotes
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar"));
        
        // Test when the input starts without double quotes and ends with double quotes
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo bar\""));
        
        // Test when the input does not start or end with double quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        
        // Test when the input is an empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
}