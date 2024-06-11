package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo";
        String expected = "foo";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "-foo";
        expected = "foo";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "foo";
        expected = "foo";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"foo bar\"";
        String expected = "foo bar";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "\"foo\"";
        expected = "foo";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "foo";
        expected = "foo";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}