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
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("_foo", Util.stripLeadingHyphens("--_foo"));
        assertEquals("bar", Util.stripLeadingHyphens("-bar"));
        assertEquals("--foo-bar", Util.stripLeadingHyphens("--foo--bar"));
        assertEquals("-foo-bar", Util.stripLeadingHyphens("--foo-bar"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo 'bar'", Util.stripLeadingAndTrailingQuotes("\"foo 'bar'\""));
        assertEquals("foo 'bar' baz", Util.stripLeadingAndTrailingQuotes("\"foo 'bar' baz\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("\"\"", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("\"foo\"", Util.stripLeadingAndTrailingQuotes("\"\"foo\"\""));
        assertEquals("foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("foo\" and bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and bar\""));
    }

    @Test
    public void testEmptyStringArray() {
        assertEquals(new String[0], Util.EMPTY_STRING_ARRAY);
        assertEquals(new String[]{""}, Util.EMPTY_STRING_ARRAY);
    }
}