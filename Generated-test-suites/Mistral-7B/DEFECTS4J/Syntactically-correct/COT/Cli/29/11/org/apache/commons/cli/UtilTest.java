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
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

        input = "test";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

        input = "test--";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

        input = null;
        assertNull(Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test one two\"";
        String expectedOutput = "test one two";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        input = "test one two";
        expectedOutput = "test one two";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"test\"";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        input = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString()
    {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}