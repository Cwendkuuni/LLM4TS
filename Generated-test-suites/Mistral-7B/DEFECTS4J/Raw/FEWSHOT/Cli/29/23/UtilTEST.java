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
        assertEquals("bar", Util.stripLeadingHyphens("bar"));
        assertEquals("--bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("--", Util.stripLeadingHyphens("--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput_stripLeadingHyphens() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput_stripLeadingAndTrailingQuotes() {
        Util.stripLeadingAndTrailingQuotes(null);
    }

    @Test
    public void testStringWithSingleQuote_stripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
    }

    @Test
    public void testStringWithMultipleQuotes_stripLeadingAndTrailingQuotes() {
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
    }
}