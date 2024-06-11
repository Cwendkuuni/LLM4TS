package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Util;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method with a null input.
     */
    @Test
    public void testStripLeadingHyphensNullInput()
    {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method with an empty string input.
     */
    @Test
    public void testStripLeadingHyphensEmptyStringInput()
    {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method with a string containing leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensLeadingHyphens()
    {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method with a string having no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens()
    {
        String input = "test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with a null input.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNullInput()
    {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with an empty string input.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesEmptyStringInput()
    {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with a string having leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with a string having trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with a string having both leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesBothLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with a string having no quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes()
    {
        String input = "test";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }
}