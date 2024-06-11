package org.apache.commons.cli;
import static org.junit.Assert.*;

import org.apache.commons.cli.Util;

public class UtilTest
{
    @org.junit.Test
    public void testStripLeadingHyphens()
    {
        String input = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "--test--";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = null;
        expected = null;
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "\"test1  test2\"";
        expected = "test1  test2";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "test1  test2";
        expected = "test1  test2";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = null;
        expected = null;
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }
}