package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util class methods.
 */
public class UtilTest
{
    /**
     * Test method for {@link Util#stripLeadingHyphens(String)}.
     */
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--foo";
        String expected = "foo";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "foo";
        expected = "foo";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "foo";
        expected = "foo";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = null;
        expected = null;
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)}.
     */
    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"foo bar\"";
        String expected = "foo bar";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "foo bar";
        expected = "foo bar";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "\"foo bar\"";
        expected = "foo bar";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = null;
        expected = null;
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "";
        expected = "";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Util#stripLeadingHyphens(String)} with edge cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensEdgeCases()
    {
        String str = "";
        Util.stripLeadingHyphens(str);
    }

    /**
     * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with edge cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesEdgeCases()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }

    /**
     * Test method for {@link Util#stripLeadingHyphens(String)} with empty string.
     */
    @Test
    public void testStripLeadingHyphensEmptyString()
    {
        String str = "";
        String expected = "";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Util#stripLeadingHyphens(String)} with empty string.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString()
    {
        String str = "";
        String expected = "";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }
}