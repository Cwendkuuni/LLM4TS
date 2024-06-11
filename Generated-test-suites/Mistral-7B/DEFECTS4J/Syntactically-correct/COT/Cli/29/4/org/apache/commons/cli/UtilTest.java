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
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "--";
        expectedOutput = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = null;
        expectedOutput = null;
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput()
    {
        Util.stripLeadingHyphens(null);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\"test\"--";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "";
        expectedOutput = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = null;
        expectedOutput = null;
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullInput()
    {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}