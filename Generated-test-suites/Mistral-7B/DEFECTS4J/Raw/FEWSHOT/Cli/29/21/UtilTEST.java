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
        assertEquals("def--ghi", Util.stripLeadingHyphens("--def--ghi"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"\"foo\" bar\""));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
        assertEquals("def\"ghi", Util.stripLeadingAndTrailingQuotes("def\"ghi"));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForStripLeadingAndTrailingQuotes() {
        Util.stripLeadingAndTrailingQuotes(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForStripLeadingHyphens() {
        Util.stripLeadingHyphens(null);
    }
}