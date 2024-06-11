package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo";
        String expectedOutput = "foo";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "-foo";
        expectedOutput = "foo";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "foo";
        expectedOutput = "foo";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullInput() {
        Util.stripLeadingHyphens(null);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"one two\"";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "one two";
        expectedOutput = "one two";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "";
        expectedOutput = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}