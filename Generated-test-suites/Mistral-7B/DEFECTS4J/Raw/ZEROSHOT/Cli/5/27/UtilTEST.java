package org.apache.commons.cli.test; // adjust the package name if necessary

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo";
        String expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "----foo";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "--foo--";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "foo";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "-foo";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "";
        expected = "";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"one two\"";
        String expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"one two\"\"";
        expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"one two\"";
        expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "";
        expected = "";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "one two";
        expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }
}