package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        assertEquals("test", Util.stripLeadingHyphens("--test"));
        
        // Edge case
        assertEquals("", Util.stripLeadingHyphens("-"));
        
        // Error scenario
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        
        // Edge case
        assertEquals("test", Util.stripLeadingAndTrailingQuotes("\"test"));
        
        // Error scenario
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}