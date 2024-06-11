package org.apache.commons.cli;
import static org.apache.commons.cli.Util.*;
import static org.junit.Assert.assertEquals;

public class UtilTest
{
    @org.junit.Test
    public void testStripLeadingHyphens()
    {
        String input = "--test";
        String expectedOutput = "test";
        String output = stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @org.junit.Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }
}