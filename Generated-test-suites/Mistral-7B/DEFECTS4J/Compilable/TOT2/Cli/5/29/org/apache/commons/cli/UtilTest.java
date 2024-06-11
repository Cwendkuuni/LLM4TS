package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NoLeadingHyphens() {
        String input = "testString";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingHyphens_OnlyLeadingHyphens() {
        String input = "--testString";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingHyphens_BothLeadingAndTrailingHyphens() {
        String input = "--testString--";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testStripLeadingHyphens_NullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String input = "testString";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyLeadingQuotes() {
        String input = "\"testString";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyTrailingQuotes() {
        String input = "testString\"";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_BothLeadingAndTrailingQuotes() {
        String input = "\"testString\"";
        String expectedOutput = "testString";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}