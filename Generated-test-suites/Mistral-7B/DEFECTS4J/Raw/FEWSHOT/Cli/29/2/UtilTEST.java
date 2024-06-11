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
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        assertEquals("ab", Util.stripLeadingHyphens("--ab"));
        assertEquals("a-b-c", Util.stripLeadingHyphens("--a-b-c"));
        assertEquals("--a--b--c--", Util.stripLeadingHyphens("--a--b--c--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo bar\"", Util.stripLeadingAndTrailingQuotes("\"\"foo bar\"\""));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"\"foo\" and \"bar\"\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
        assertEquals("one", Util.stripLeadingAndTrailingQuotes("\"one\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"\"one two\"\""));
        assertEquals("\"one two\" and \"three four\"", Util.stripLeadingAndTrailingQuotes("\"\"one two\" and \"three four\"\""));
    }
}