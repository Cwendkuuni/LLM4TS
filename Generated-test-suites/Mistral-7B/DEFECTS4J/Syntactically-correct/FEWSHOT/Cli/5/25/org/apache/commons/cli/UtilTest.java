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
        assertEquals("", Util.stripLeadingHyphens(""));
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("_bar", Util.stripLeadingHyphens("--_bar"));
        assertEquals("_", Util.stripLeadingHyphens("--"));
        assertEquals("--option", Util.stripLeadingHyphens("--option-with-value=value"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("'foo bar'"));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithEscapedQuotes() {
        assertEquals("foo\\ bar", Util.stripLeadingAndTrailingQuotes("\"foo\\ bar\""));
        assertEquals("foo\\ and bar\\", Util.stripLeadingAndTrailingQuotes("\"foo\\ and bar\\\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithDoubleQuotesInside() {
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo \"bar\"\""));
        assertEquals("foo 'bar''", Util.stripLeadingAndTrailingQuotes("\"foo 'bar''\""));
    }
}