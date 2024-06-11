package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test null input
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);

        // Test input starting with "--"
        String input1 = "--test";
        String expected1 = "test";
        String result1 = Util.stripLeadingHyphens(input1);
        assertEquals(expected1, result1);

        // Test input starting with "-"
        String input2 = "-test";
        String expected2 = "test";
        String result2 = Util.stripLeadingHyphens(input2);
        assertEquals(expected2, result2);

        // Test input without "-"
        String input3 = "test";
        String expected3 = "test";
        String result3 = Util.stripLeadingHyphens(input3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test input with leading and trailing quotes
        String input1 = "\"test\"";
        String expected1 = "test";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expected1, result1);

        // Test input without leading and trailing quotes
        String input2 = "test";
        String expected2 = "test";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expected2, result2);

        // Test input with leading quote
        String input3 = "\"test";
        String expected3 = "\"test";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expected3, result3);

        // Test input with trailing quote
        String input4 = "test\"";
        String expected4 = "test\"";
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);
        assertEquals(expected4, result4);
    }
}