package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test cases for Util.stripLeadingHyphens(String str)
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test--";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = null;
        expectedOutput = null;
        output = Util.stripLeadingHyphens(input);
        assertNull(output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test cases for Util.stripLeadingAndTrailingQuotes(String str)
        String input = "\"one two\"";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "one two";
        expectedOutput = "one two";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\"one two\"";
        expectedOutput = "one two";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = null;
        expectedOutput = null;
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertNull(output);
    }
}