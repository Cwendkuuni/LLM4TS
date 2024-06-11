import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        // test when input starts with two hyphens
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        
        // test when input starts with one hyphen
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        
        // test when input doesn't start with a hyphen
        assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
        
        // test when input is null
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // test when input starts and ends with quotes
        assertEquals("hello", Util.stripLeadingAndTrailingQuotes("\"hello\""));
        
        // test when input starts with quote and doesn't end with quote
        assertEquals("world\"", Util.stripLeadingAndTrailingQuotes("\"world\""));
        
        // test when input doesn't start with quote but ends with quote
        assertEquals("\"hi", Util.stripLeadingAndTrailingQuotes("\"hi"));
        
        // test when input doesn't start or end with quote
        assertEquals("how are you", Util.stripLeadingAndTrailingQuotes("how are you"));
    }
}