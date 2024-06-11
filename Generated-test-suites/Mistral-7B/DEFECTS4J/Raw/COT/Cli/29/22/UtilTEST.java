package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for Util class.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--Test-Method";
        String expected = "Test-Method";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "Test-Method";
        expected = "Test-Method";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = null;
        expected = null;
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        // Edge case: empty string
        input = "";
        expected = "";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInputThrowsException()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"Test-Method\"";
        String expected = "Test-Method";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "Test-Method";
        expected = "Test-Method";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        // Edge case: string with only leading quotes
        input = "\"Test-Method";
        expected = "Test-Method";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        // Edge case: string with only trailing quotes
        input = "Test-Method\"";
        expected = "Test-Method";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyStringThrowsException()
    {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}