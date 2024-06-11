package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Util;
import org.junit.Test;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        // test empty string
        assertEquals("", Util.stripLeadingHyphens(""));

        // test null string
        assertNull(Util.stripLeadingHyphens(null));

        // test no leading hyphens
        assertEquals("foo", Util.stripLeadingHyphens("foo"));

        // test one leading hyphen
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));

        // test multiple leading hyphens
        assertEquals("baz", Util.stripLeadingHyphens("--foo--bar--baz"));

        // test leading hyphen followed by a space
        assertEquals("qux", Util.stripLeadingHyphens("-- qux"));

        // test string with no hyphens
        assertEquals("quux", Util.stripLeadingHyphens("quux"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        // test empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // test null string
        assertNull(Util.stripLeadingAndTrailingQuotes(null));

        // test string with leading quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // test string with trailing quotes
        assertEquals("three four", Util.stripLeadingAndTrailingQuotes("three four\""));

        // test string with both leading and trailing quotes
        assertEquals("five six", Util.stripLeadingAndTrailingQuotes("\"five six\""));

        // test string with no quotes
        assertEquals("seven eight", Util.stripLeadingAndTrailingQuotes("seven eight"));
    }
}