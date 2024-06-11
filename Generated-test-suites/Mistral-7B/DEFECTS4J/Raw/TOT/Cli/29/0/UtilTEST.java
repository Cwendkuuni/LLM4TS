package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--foo";
        String expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "foo--";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "--";
        assertEquals(input, Util.stripLeadingHyphens(input));

        input = null;
        assertNull(Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"--foo\"";
        String expected = "--foo";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"--foo--\"";
        expected = "--foo--";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"\"";
        assertEmpty(Util.stripLeadingAndTrailingQuotes(input));

        input = null;
        assertNull(Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingHyphensWithEmptyString()
    {
        String input = "";
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingHyphensWithNullInput()
    {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString()
    {
        Util.stripLeadingAndTrailingQuotes("");
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput()
    {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}