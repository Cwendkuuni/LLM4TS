package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // Test case for null input
        String nullInput = null;
        String nullOutput = null;
        String expectedNullOutput = null;
        String outputNull = Util.stripLeadingHyphens(nullInput);
        assertEquals(expectedNullOutput, outputNull);

        // Test case for empty string
        String emptyString = "";
        String outputEmpty = Util.stripLeadingHyphens(emptyString);
        assertEquals(emptyString, outputEmpty);

        // Test case for string without leading hyphens
        String noLeadingHyphens = "test";
        String outputNoLeadingHyphens = Util.stripLeadingHyphens(noLeadingHyphens);
        assertEquals(noLeadingHyphens, outputNoLeadingHyphens);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "'test'";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // Test case for null input
        String nullInput = null;
        String nullOutput = null;
        String expectedNullOutput = null;
        String outputNull = Util.stripLeadingAndTrailingQuotes(nullInput);
        assertEquals(expectedNullOutput, outputNull);

        // Test case for empty string
        String emptyString = "";
        String outputEmpty = Util.stripLeadingAndTrailingQuotes(emptyString);
        assertEquals(emptyString, outputEmpty);

        // Test case for string with only leading quotes
        String leadingQuotes = "'test'";
        String outputLeadingQuotes = Util.stripLeadingAndTrailingQuotes(leadingQuotes);
        assertEquals("test", outputLeadingQuotes);

        // Test case for string with only trailing quotes
        String trailingQuotes = "test'";
        String outputTrailingQuotes = Util.stripLeadingAndTrailingQuotes(trailingQuotes);
        assertEquals("test", outputTrailingQuotes);

        // Test case for string with leading and trailing quotes
        String leadingAndTrailingQuotes = "'test'";
        String outputLeadingAndTrailingQuotes = Util.stripLeadingAndTrailingQuotes(leadingAndTrailingQuotes);
        assertEquals("test", outputLeadingAndTrailingQuotes);
    }

}