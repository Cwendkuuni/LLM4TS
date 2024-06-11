package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the Util class.
 */
public class UtilTest
{
    /**
     * Test for the stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens()
    {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingHyphens method with one leading hyphen.
     */
    @Test
    public void testStripLeadingHyphensOneLeadingHyphen()
    {
        String input = "-someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingHyphens method with multiple leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens()
    {
        String input = "--someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingHyphens method with null input.
     */
    @Test
    public void testStripLeadingHyphensNull()
    {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingAndTrailingQuotes method with no quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes()
    {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingAndTrailingQuotes method with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes()
    {
        String input = "'someString'";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingAndTrailingQuotes method with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes()
    {
        String input = "someString'";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingAndTrailingQuotes method with both leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesBothLeadingAndTrailingQuotes()
    {
        String input = "'someString'";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for the stripLeadingAndTrailingQuotes method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNull()
    {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}