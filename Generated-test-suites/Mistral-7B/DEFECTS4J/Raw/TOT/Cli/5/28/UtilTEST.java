package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Util class.
 */
public class UtilTest {

    /**
     * Test for stripLeadingHyphens method.
     */
    @Test
    public void testStripLeadingHyphens() {
        String input = "--some-option";
        String expected = "some-option";
        String result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        input = "-some-option";
        expected = "some-option";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        input = "some-option";
        expected = "some-option";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        input = "--";
        expected = "";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method.
     */
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "'one two'";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        input = "\"one two\"";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        input = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        input = "'one'";
        expected = "one";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        input = "\"one\"";
        expected = "one";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);
    }
}