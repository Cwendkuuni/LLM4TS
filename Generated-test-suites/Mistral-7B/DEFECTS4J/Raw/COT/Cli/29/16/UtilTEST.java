package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        // Basic test case
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // Test with empty string
        input = "";
        expectedOutput = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // Test with no leading hyphens
        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // Test with multiple leading hyphens
        input = "----test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        // Basic test case
        String input = "'test'";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // Test with no quotes
        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // Test with leading quotes only
        input = "'test'";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // Test with trailing quotes only
        input = "test'";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // Test with quotes at both ends
        input = "'test'";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullString()
    {
        // Test for NullPointerException when input is null
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullString()
    {
        // Test for NullPointerException when input is null
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}