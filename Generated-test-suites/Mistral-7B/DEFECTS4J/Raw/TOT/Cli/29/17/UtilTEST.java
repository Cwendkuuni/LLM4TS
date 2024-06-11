package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util class.
 */
public class UtilTest
{
    /**
     * Test for stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens()
    {
        String str = "testString";
        String expected = "testString";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingHyphens method with leading double hyphens.
     */
    @Test
    public void testStripLeadingHyphensDoubleLeadingHyphens()
    {
        String str = "--testString";
        String expected = "testString";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensSingleLeadingHyphen()
    {
        String str = "-testString";
        String expected = "testString";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingHyphens method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with no quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes()
    {
        String str = "testString";
        String expected = "testString";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes()
    {
        String str = "\"testString\"";
        String expected = "testString";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes()
    {
        String str = "\"testString\"";
        String expected = "testString";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingAndTrailingQuotes()
    {
        String str = "\"testString\"";
        String expected = "testString";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with empty string.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString()
    {
        String str = "";
        String expected = "";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullInput()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}