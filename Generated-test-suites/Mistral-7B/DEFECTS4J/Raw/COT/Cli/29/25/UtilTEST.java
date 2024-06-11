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
        testStripLeadingHyphens_NormalCase();
        testStripLeadingHyphens_NullInput();
        testStripLeadingHyphens_EmptyString();
        testStripLeadingHyphens_LeadingTwoHyphens();
        testStripLeadingHyphens_LeadingOneHyphen();
        testStripLeadingHyphens_NoLeadingHyphens();
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        testStripLeadingAndTrailingQuotes_NormalCase();
        testStripLeadingAndTrailingQuotes_NullInput();
        testStripLeadingAndTrailingQuotes_EmptyString();
        testStripLeadingAndTrailingQuotes_StringWithLeadingQuote();
        testStripLeadingAndTrailingQuotes_StringWithTrailingQuote();
        testStripLeadingAndTrailingQuotes_StringWithBothLeadingAndTrailingQuotes();
    }

    // Test methods for stripLeadingHyphens() go here

    private void testStripLeadingHyphens_NormalCase()
    {
        String input = "--test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    private void testStripLeadingHyphens_NullInput()
    {
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    private void testStripLeadingHyphens_EmptyString()
    {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    private void testStripLeadingHyphens_LeadingTwoHyphens()
    {
        String input = "---test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    private void testStripLeadingHyphens_LeadingOneHyphen()
    {
        String input = "--test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    private void testStripLeadingHyphens_NoLeadingHyphens()
    {
        String input = "test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    // Test methods for stripLeadingAndTrailingQuotes() go here

    private void testStripLeadingAndTrailingQuotes_NormalCase()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }

    private void testStripLeadingAndTrailingQuotes_NullInput()
    {
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }

    private void testStripLeadingAndTrailingQuotes_EmptyString()
    {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }

    private void testStripLeadingAndTrailingQuotes_StringWithLeadingQuote()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }

    private void testStripLeadingAndTrailingQuotes_StringWithTrailingQuote()
    {
        String input = "test\"";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }

    private void testStripLeadingAndTrailingQuotes_StringWithBothLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }
}