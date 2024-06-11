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
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
        assertEquals("'foo bar baz'", Util.stripLeadingAndTrailingQuotes("'foo bar baz'"));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testEmptyStringArray() {
        assertEquals(0, Util.EMPTY_STRING_ARRAY.length);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringInStripLeadingHyphens() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringInStripLeadingAndTrailingQuotes() {
        Util.stripLeadingAndTrailingQuotes(null);
    }

    @Test
    public void testEmptyStringInStripLeadingHyphens() {
        assertEquals("", Util.stripLeadingHyphens("--"));
    }

    @Test
    public void testEmptyStringInStripLeadingAndTrailingQuotes() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }
}