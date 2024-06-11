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
        String input = "--foo";
        String expected = "foo";
        String result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        input = "-foo";
        expected = "foo";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        input = "foo";
        expected = "foo";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        input = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        input = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);
    }
}