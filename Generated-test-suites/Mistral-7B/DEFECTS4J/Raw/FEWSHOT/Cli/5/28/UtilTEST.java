package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("ab", Util.stripLeadingHyphens("--ab"));
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("--abc--def", Util.stripLeadingHyphens("--abc--def"));
        assertEquals("--abc--def", Util.stripLeadingHyphens("-- --abc--def"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"\"one two\"\""));
        assertEquals("\"one two\" three", Util.stripLeadingAndTrailingQuotes("\"\"one two\" three\""));
    }

    @Test
    public void testEmptyArray() {
        assertEquals(Util.EMPTY_STRING_ARRAY, Util.stripLeadingAndTrailingQuotes(Util.EMPTY_STRING_ARRAY));
    }
}