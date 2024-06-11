package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test when input starts with "--"
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("f", Util.stripLeadingHyphens("--f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("---foo", Util.stripLeadingHyphens("---foo"));

        // Test when input starts with "-"
        assertEquals("", Util.stripLeadingHyphens("-"));
        assertEquals("", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("-foo"));
        assertEquals("--foo", Util.stripLeadingHyphens("--foo"));

        // Test when input does not start with "-" or "--"
        assertEquals("f", Util.stripLeadingHyphens("f"));
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("-foo"));
        assertEquals("--foo", Util.stripLeadingHyphens("--foo"));

        // Test for null input
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test when input starts and ends with double quotes
        assertEquals("one", Util.stripLeadingAndTrailingQuotes("\"one\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));

        // Test when input starts with double quotes but does not end with double quotes
        assertEquals("\"one", Util.stripLeadingAndTrailingQuotes("\"one"));
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two"));

        // Test when input ends with double quotes but does not start with double quotes
        assertEquals("one\"", Util.stripLeadingAndTrailingQuotes("one\""));
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("one two\""));

        // Test when input does not start or end with double quotes
        assertEquals("one", Util.stripLeadingAndTrailingQuotes("one"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // Test for null input
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}