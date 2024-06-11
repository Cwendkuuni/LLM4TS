package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("'foo bar'"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }

    @Test
    public void testEmptyStringArray() {
        assertEquals(new String[0], Util.EMPTY_STRING_ARRAY);
    }
}