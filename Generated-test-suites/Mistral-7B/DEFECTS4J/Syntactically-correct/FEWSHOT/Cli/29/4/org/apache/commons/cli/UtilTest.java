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
        assertEquals("--foo--bar--baz", Util.stripLeadingHyphens("--foo--bar--baz"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("'one two'", Util.stripLeadingAndTrailingQuotes("'one two'"));
        assertEquals("'one two'", Util.stripLeadingAndTrailingQuotes("'one two''"));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput_stripLeadingAndTrailingQuotes() {
        Util.stripLeadingAndTrailingQuotes(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput_stripLeadingHyphens() {
        Util.stripLeadingHyphens(null);
    }
}