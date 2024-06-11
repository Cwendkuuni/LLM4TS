package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical Use Cases
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));

        // Edge Cases
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("a", Util.stripLeadingHyphens("a"));
        assertEquals("", Util.stripLeadingHyphens("-"));
        assertEquals("-", Util.stripLeadingHyphens("--"));

        // Error Scenarios
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical Use Cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("'one two'"));

        // Edge Cases
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("a", Util.stripLeadingAndTrailingQuotes("a"));
        assertEquals("\"one two'", Util.stripLeadingAndTrailingQuotes("\"one two'"));
        assertEquals("'one two\"", Util.stripLeadingAndTrailingQuotes("'one two\""));

        // Error Scenarios
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}