package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the Util class.
 */
public class UtilTest
{
    /**
     * Test for stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens()
    {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingHyphens method with leading double hyphens.
     */
    @Test
    public void testStripLeadingHyphensDoubleLeadingHyphens()
    {
        String input = "--someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingHyphens method with no leading hyphens or quotes.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphensOrQuotes()
    {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with no quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes()
    {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes()
    {
        String input = "\"someString\"";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes()
    {
        String input = "\"someString\"";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with both leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesBothLeadingAndTrailingQuotes()
    {
        String input = "\"someString\"";
        String expectedOutput = "someString";
        String output = Util. stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }
}