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
        assertEquals("--", Util.stripLeadingHyphens("--"));
        assertEquals("-", Util.stripLeadingHyphens("-"));
        assertEquals("--a--b--", Util.stripLeadingHyphens("--a--b--"));
        assertEquals("-a-b-", Util.stripLeadingHyphens("-a-b-"));
        assertEquals("--a--b--c", Util.stripLeadingHyphens("--a--b--c"));
        assertEquals("-a-b-c", Util.stripLeadingHyphens("-a-b-c"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
        assertEquals("\"\"", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("\"foo\"", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("\"foo bar\"", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("'foo'", Util.stripLeadingAndTrailingQuotes("'foo'"));
        assertEquals("'foo bar'", Util.stripLeadingAndTrailingQuotes("'foo bar'"));
        assertEquals("'foo' bar", Util.stripLeadingAndTrailingQuotes("'foo' bar"));
        assertEquals("'foo' and 'bar'", Util.stripLeadingAndTrailingQuotes("'foo' and 'bar'"));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput_stripLeadingHyphens() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput_stripLeadingAndTrailingQuotes() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}