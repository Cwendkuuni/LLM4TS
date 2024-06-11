package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("--bar--baz", Util.stripLeadingHyphens("--bar--baz"));
        assertEquals("--foo-bar", Util.stripLeadingHyphens("--foo-bar"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("\"one two three\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("--foo", Util.stripLeadingAndTrailingQuotes("--\"foo\""));
    }
}