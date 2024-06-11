package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Basic test cases
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));

        // Edge case: empty string
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("", Util.stripLeadingHyphens(""));

        // Edge case: no leading hyphens
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("abc", Util.stripLeadingHyphens("----abc"));

        // Edge case: only leading hyphens
        assertEquals("", Util.stripLeadingHyphens("------"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Basic test cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"\"one two\"\""));

        // Edge case: empty string
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // Edge case: no leading or trailing quotes
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"\"\"one two\"\"\""));

        // Edge case: only leading quotes
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\"\"\"\""));

        // Edge case: only trailing quotes
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\"\"\""));
    }
    
}
