package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test empty string
        assertEquals("", Util.stripLeadingHyphens(""));

        // Test strings without leading hyphens
        assertEquals("f", Util.stripLeadingHyphens("f"));
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
        assertEquals("--foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("-foo"));
        assertEquals("---foo", Util.stripLeadingHyphens("---foo"));

        // Test strings with leading hyphens
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("baz", Util.stripLeadingHyphens("--baz"));
        assertEquals("qux", Util.stripLeadingHyphens("--qux"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // Test strings with leading quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("bar", Util.stripLeadingAndTrailingQuotes("\"bar\""));
        assertEquals("qux", Util.stripLeadingAndTrailingQuotes("\"qux\""));

        // Test strings with trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));
        assertEquals("bar", Util.stripLeadingAndTrailingQuotes("foo bar\""));
        assertEquals("qux", Util.stripLeadingAndTrailingQuotes("foo bar qux\""));

        // Test strings with leading and trailing quotes
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));

        // Test strings with quotes inside
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
    }

    @Test
    public void testStripLeadingHyphensWithNullString() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}