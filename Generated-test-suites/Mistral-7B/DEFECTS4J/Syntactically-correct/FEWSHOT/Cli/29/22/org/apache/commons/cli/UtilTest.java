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
        assertEquals("123", Util.stripLeadingHyphens("123"));
        assertEquals("--123", Util.stripLeadingHyphens("--123"));
        assertEquals("--123--", Util.stripLeadingHyphens("--123--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"\"foo\" bar\""));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
    }

    @Test
    public void testStripLeadingHyphensWithQuotes() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("\"f\"", Util.stripLeadingHyphens("--\"f\""));
        assertEquals("\"--foo\"", Util.stripLeadingHyphens("---\"--foo\""));
        assertEquals("\"---\"foo\"", Util.stripLeadingHyphens("----\"---\"foo\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithHyphens() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"\"foo\" bar\""));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("--123", Util.stripLeadingAndTrailingQuotes("\"--123\""));
        assertEquals("--123--", Util.stripLeadingAndTrailingQuotes("\"--123--\""));
        assertEquals("\"--123\"", Util.stripLeadingAndTrailingQuotes("\"--123\""));
        assertEquals("\"---\"123\"", Util.stripLeadingAndTrailingQuotes("----\"---\"123\""));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
}