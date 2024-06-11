package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // typical use case
        assertEquals("string", Util.stripLeadingHyphens("-string"));
        
        // edge cases
        assertEquals("string", Util.stripLeadingHyphens("string"));
        assertEquals("", Util.stripLeadingHyphens("-"));
        
        // error scenarios
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // typical use case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        
        // edge cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
        
        // error scenarios
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}