package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));

        // Test empty string
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("", Util.stripLeadingHyphens("--"));

        // Test string with multiple leading hyphens
        assertEquals("bar", Util.stripLeadingHyphens("---bar"));
        assertEquals("baz", Util.stripLeadingHyphens("----baz"));

        // Test string with a single leading hyphen followed by other characters
        assertEquals("a", Util.stripLeadingHyphens("-a"));
        assertEquals("ab", Util.stripLeadingHyphens("-a-b"));
        assertEquals("abc", Util.stripLeadingHyphens("-a--b-c"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));

        // Test string with multiple leading and trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("\"one two three\""));

        // Test string with leading quotes only
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));

        // Test string with trailing quotes only
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));
    }
}