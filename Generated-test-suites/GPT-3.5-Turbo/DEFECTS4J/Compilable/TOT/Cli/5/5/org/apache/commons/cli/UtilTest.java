package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        assertEquals("string", Util.stripLeadingHyphens("-string"));
        
        // Edge case - no leading hyphens
        assertEquals("string", Util.stripLeadingHyphens("string"));
        
        // Edge case - double hyphens
        assertEquals("string", Util.stripLeadingHyphens("--string"));
        
        // Error scenario - null input
        try {
            Util.stripLeadingHyphens(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        
        // Edge case - no leading or trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        
        // Edge case - empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        
        // Error scenario - null input
        try {
            Util.stripLeadingAndTrailingQuotes(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}