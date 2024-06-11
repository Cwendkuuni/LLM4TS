package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util class.
 */
public class UtilTest
{
    /**
     * Test for stripLeadingHyphens method.
     * Test case 1: Normal case: String without leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNormalCase()
    {
        String input = "testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method.
     * Test case 2: Edge case: String with leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensEdgeCase()
    {
        String input = "--testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method.
     * Test case 3: Edge case: String with multiple leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens()
    {
        String input = "---testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method.
     * Test case 4: Edge case: String with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens()
    {
        String input = "testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method.
     * Test case 5: Exception: Null input.
     */
    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method.
     * Test case 1: Normal case: String without leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNormalCase()
    {
        String input = "testString";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method.
     * Test case 2: Edge case: String with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes()
    {
        String input = "\"testString\"";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method.
     * Test case 3: Edge case: String with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes()
    {
        String input = "\"testString\"";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method.
     * Test case 4: Edge case: String with both leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesBothLeadingAndTrailingQuotes()
    {
        String input = "\"testString\"";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method.
     * Test case 5: Exception: Null input.
     */
    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullInput()
    {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}