package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: input starts with double hyphen
        String input1 = "--test";
        String expected1 = "test";
        Assert.assertEquals(expected1, Util.stripLeadingHyphens(input1));

        // Test case 2: input starts with single hyphen
        String input2 = "-test";
        String expected2 = "test";
        Assert.assertEquals(expected2, Util.stripLeadingHyphens(input2));

        // Test case 3: input does not start with hyphen
        String input3 = "test";
        String expected3 = "test";
        Assert.assertEquals(expected3, Util.stripLeadingHyphens(input3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: input starts and ends with quotes
        String input1 = "\"test\"";
        String expected1 = "test";
        Assert.assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(input1));

        // Test case 2: input starts with quote
        String input2 = "\"test";
        String expected2 = "\"test";
        Assert.assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(input2));

        // Test case 3: input ends with quote
        String input3 = "test\"";
        String expected3 = "test\"";
        Assert.assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(input3));

        // Test case 4: input does not start or end with quote
        String input4 = "test";
        String expected4 = "test";
        Assert.assertEquals(expected4, Util.stripLeadingAndTrailingQuotes(input4));
    }
}