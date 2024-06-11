package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        assertEquals("--abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("--ab-c", Util.stripLeadingHyphens("--ab-c"));
        assertEquals("-ab-c", Util.stripLeadingHyphens("-ab-c"));
        assertEquals("---ab-c", Util.stripLeadingHyphens("---ab-c"));
        assertEquals("--ab-c--", Util.stripLeadingHyphens("--ab-c--"));
        assertEquals("-ab-c-", Util.stripLeadingHyphens("-ab-c-"));
        assertEquals("--ab-c--", Util.stripLeadingHyphens("--ab-c--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"\"foo\" bar\""));
        assertEquals("\"\"foo\" and \"bar\"\"", Util.stripLeadingAndTrailingQuotes("\"\"\"foo\" and \"bar\"\""));
        assertEquals("\"foo bar\"", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo bar \"baz\"\"", Util.stripLeadingAndTrailingQuotes("\"foo bar \"baz\""));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullString() {
        Util.stripLeadingHyphens(null);
        Util.stripLeadingAndTrailingQuotes(null);
    }
}