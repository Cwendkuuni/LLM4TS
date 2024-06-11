package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--example";
        String expectedOutput = "example";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String input = "-example";
        String expectedOutput = "example";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"example\"";
        String expectedOutput = "example";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithSingleQuote() {
        String input = "\"example";
        String expectedOutput = "example";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }
}