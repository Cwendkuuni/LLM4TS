package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--foo";
        String expected1 = "foo";
        String result1 = Util.stripLeadingHyphens(input1);
        Assert.assertEquals(expected1, result1);

        String input2 = "foo--bar";
        String expected2 = "foo bar";
        String result2 = Util.stripLeadingHyphens(input2);
        Assert.assertEquals(expected2, result2);

        String input3 = "foo--";
        String expected3 = "foo";
        String result3 = Util.stripLeadingHyphens(input3);
        Assert.assertEquals(expected3, result3);

        String input4 = "foo";
        String expected4 = "foo";
        String result4 = Util.stripLeadingHyphens(input4);
        Assert.assertEquals(expected4, result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"--foo\"";
        String expected1 = "foo";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        Assert.assertEquals(expected1, result1);

        String input2 = "\"--foo--bar\"";
        String expected2 = "foo bar";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        Assert.assertEquals(expected2, result2);

        String input3 = "\"foo--\"";
        String expected3 = "foo";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        Assert.assertEquals(expected3, result3);

        String input4 = "foo";
        String expected4 = "foo";
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);
        Assert.assertEquals(expected4, result4);

        String input5 = "\"foo\"";
        String expected5 = "foo";
        String result5 = Util.stripLeadingAndTrailingQuotes(input5);
        Assert.assertEquals(expected5, result5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}