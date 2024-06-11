package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case: hyphens at the beginning of the string
        String input1 = "--example";
        String expectedOutput1 = "example";
        assertEquals(expectedOutput1, Util.stripLeadingHyphens(input1));

        // Edge case: single hyphen at the beginning of the string
        String input2 = "-example";
        String expectedOutput2 = "example";
        assertEquals(expectedOutput2, Util.stripLeadingHyphens(input2));

        // Edge case: no hyphens at the beginning of the string
        String input3 = "example";
        assertEquals(input3, Util.stripLeadingHyphens(input3));

        // Error scenario: empty string
        String input4 = "";
        assertEquals(input4, Util.stripLeadingHyphens(input4));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case: leading and trailing quotes
        String input1 = "\"example\"";
        String expectedOutput1 = "example";
        assertEquals(expectedOutput1, Util.stripLeadingAndTrailingQuotes(input1));

        // Edge case: leading quote only
        String input2 = "\"example";
        String expectedOutput2 = input2;
        assertEquals(expectedOutput2, Util.stripLeadingAndTrailingQuotes(input2));

        // Edge case: trailing quote only
        String input3 = "example\"";
        String expectedOutput3 = input3;
        assertEquals(expectedOutput3, Util.stripLeadingAndTrailingQuotes(input3));

        // Error scenario: no quotes
        String input4 = "example";
        assertEquals(input4, Util.stripLeadingAndTrailingQuotes(input4));
    }
}