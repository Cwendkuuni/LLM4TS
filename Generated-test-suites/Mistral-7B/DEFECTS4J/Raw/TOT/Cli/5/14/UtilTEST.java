package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--two--three";
        String expected = "two--three";
        String actual = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, actual);

        input = "--";
        expected = "";
        actual = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, actual);

        input = "two--three";
        expected = "two--three";
        actual = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"one two\"";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);

        input = "\"\"";
        expected = "";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);

        input = "one two";
        expected = "one two";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullInput() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}