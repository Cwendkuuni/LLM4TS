package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util class in org.apache.commons.cli package.
 */
public class UtilTest {

    /**
     * Test for stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingHyphens method with one leading hyphen.
     */
    @Test
    public void testStripLeadingHyphensOneLeadingHyphen() {
        String input = "-someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingHyphens method with multiple leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String input = "--someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with no quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String input = "someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
        String input = "'someString";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
        String input = "someString'";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with quotes at both ends.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesQuotesAtBothEnds() {
        String input = "'someString'";
        String expectedOutput = "someString";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }
}