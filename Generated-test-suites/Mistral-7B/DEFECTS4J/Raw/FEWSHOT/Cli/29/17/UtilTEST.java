package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
        assertNull(Util.stripLeadingHyphens(""));
        assertNull(Util.stripLeadingHyphens("--"));
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("--bar--", Util.stripLeadingHyphens("--bar--"));
        assertEquals("-a--b--c--", Util.stripLeadingHyphens("-a--b--c--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"" + "foo " + "bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"" + "foo\" " + "bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"" + "foo\" and \"" + "bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
    }

    @Test
    public void testStripLeadingHyphensWithQuotes() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("--foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("--\"foo\"", Util.stripLeadingHyphens("--\"foo\""));
        assertEquals("-foo", Util.stripLeadingHyphens("-foo"));
        assertEquals("-\"foo\"", Util.stripLeadingHyphens("-\"foo\""));
        assertEquals("--foo--", Util.stripLeadingHyphens("--foo--"));
        assertEquals("--\"foo\"--", Util.stripLeadingHyphens("--\"foo\"--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithHyphens() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"" + "foo " + "bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"" + "foo\" " + "bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"" + "foo\" and \"" + "bar\""));
        assertEquals("--foo--", Util.stripLeadingAndTrailingQuotes("--\"foo\""));
        assertEquals("--foo--", Util.stripLeadingAndTrailingQuotes("--foo"));
        assertEquals("--foo--", Util.stripLeadingAndTrailingQuotes("--foo--"));
    }
}