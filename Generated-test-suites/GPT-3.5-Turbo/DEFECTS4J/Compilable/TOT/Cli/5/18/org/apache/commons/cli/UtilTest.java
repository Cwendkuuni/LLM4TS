package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use cases
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("bcd", Util.stripLeadingHyphens("-bcd"));

        // Edge cases
        assertEquals("test", Util.stripLeadingHyphens("test"));

        // Error scenarios
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("", Util.stripLeadingHyphens("-"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("123", Util.stripLeadingAndTrailingQuotes("\"123\""));

        // Edge cases
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));

        // Error scenarios
        assertEquals("test", Util.stripLeadingAndTrailingQuotes("test"));
    }
}