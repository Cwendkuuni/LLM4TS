package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test input with double hyphens
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        // Test input with single hyphen
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        // Test input with no hyphens
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        // Test null input
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test input with leading and trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Test input with leading quotes
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
        // Test input with trailing quotes
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"\"one two\""));
        // Test input with no quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        // Test input with empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
}