package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util class.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(str, result);

        str = null;
        result = Util.stripLeadingHyphens(str);
        assertEquals(null, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "test";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(str, result);

        str = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(str, result);

        str = "\"test\"\"";
        expected = "test";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullStringThrowsException()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testStripLeadingHyphensWithEmptyStringThrowsException()
    {
        String str = "";
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingQuotesWithNullStringThrowsException()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingQuotesWithEmptyStringThrowsException()
    {
        String str = "";
        Util.stripLeadingAndTrailingQuotes(str);
    }
}