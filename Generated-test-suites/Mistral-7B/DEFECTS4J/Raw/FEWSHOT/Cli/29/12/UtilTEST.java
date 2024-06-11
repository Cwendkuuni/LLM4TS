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
        assertEquals("_foo", Util.stripLeadingHyphens("--_foo")); // edge case with underscore
        assertEquals("", Util.stripLeadingHyphens("--")); // empty string when double hyphen is the only character
        assertEquals("abc", Util.stripLeadingHyphens("abc")); // no leading hyphens
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("'one two'", Util.stripLeadingAndTrailingQuotes("'one two'"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("'one two'")); // single quote as trailing character
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\"")); // empty string with quotes as starting and ending characters
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo bar")); // no quotes at beginning or end
    }
}