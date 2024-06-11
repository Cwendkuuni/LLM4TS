package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        // Typical use cases
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        
        // Edge cases
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("", Util.stripLeadingHyphens("-"));

        // Error scenarios
        assertEquals(null, Util.stripLeadingHyphens(null));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        assertEquals("", Util.stripLeadingHyphens(""));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("string", Util.stripLeadingAndTrailingQuotes("\"string\""));

        // Edge cases
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("a", Util.stripLeadingAndTrailingQuotes("\"a\""));

        // Error scenarios
        assertEquals("quote", Util.stripLeadingAndTrailingQuotes("quote"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals(null, Util.stripLeadingAndTrailingQuotes(null));
    }
}