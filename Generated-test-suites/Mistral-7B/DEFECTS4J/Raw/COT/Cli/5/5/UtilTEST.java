package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "--";
        expected = "";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "test";
        expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "test";
        expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "";
        expected = "";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"\"";
        expected = "";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}