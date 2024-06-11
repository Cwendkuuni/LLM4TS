package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String inputStr = "--str";
        String expectedOutput = "str";
        String outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "str";
        expectedOutput = "str";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "str--";
        expectedOutput = "str";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = null;
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertNull(outputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String inputStr = "\"\"one two\"\"";
        String expectedOutput = "one two";
        String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "\"one two\"";
        expectedOutput = "one two";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "\" ";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("", outputStr);

        inputStr = " ";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("", outputStr);

        inputStr = null;
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertNull(outputStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullInput() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingHyphensWithShortString() {
        String inputStr = "--";
        Util.stripLeadingHyphens(inputStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}