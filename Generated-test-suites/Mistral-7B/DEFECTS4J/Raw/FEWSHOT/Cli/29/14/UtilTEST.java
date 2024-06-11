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
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("a", Util.stripLeadingHyphens("a"));
        assertEquals("ab", Util.stripLeadingHyphens("--ab"));
        assertEquals("abc", Util.stripLeadingHyphens("-- --abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("\"one two three\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("a", Util.stripLeadingAndTrailingQuotes("a"));
        assertEquals("ab", Util.stripLeadingAndTrailingQuotes("\"ab\""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("\"abc\""));
        assertEquals("abcd", Util.stripLeadingAndTrailingQuotes("\"abcd\" ef"));
        assertEquals("abcd ef", Util.stripLeadingAndTrailingQuotes("\"abcd\" ef"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("", Util.stripLeadingHyphens("-- --"));
        assertEquals("", Util.stripLeadingHyphens("-- -- --"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("a", Util.stripLeadingHyphens("--a"));
        assertEquals("a", Util.stripLeadingHyphens("-a"));
        assertEquals("ab", Util.stripLeadingHyphens("--ab"));
        assertEquals("ab", Util.stripLeadingHyphens("-ab"));
        assertEquals("abcd", Util.stripLeadingHyphens("-- --abcd"));
        assertEquals("abcd", Util.stripLeadingHyphens("- --abcd"));
        assertEquals("\"abcd\" ef", Util.stripLeadingAndTrailingQuotes("\"\"\"abcd\" ef\""));
        assertEquals("\"abcd\" ef", Util.stripLeadingAndTrailingQuotes("\"\"\"\"abcd\" ef\""));
    }

    @Test
    public void testNullInput() {
        assertNull(Util.stripLeadingHyphens(null));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}