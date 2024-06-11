package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String inputStr = "--test";
        String expectedOutput = "test";
        String outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "-test";
        expectedOutput = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "test";
        expectedOutput = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_SingleQuotes() {
        String inputStr = "'test'";
        String expectedOutput = "test";
        String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_DoubleQuotes() {
        String inputStr = "\"test\"";
        String expectedOutput = "test";
        String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        String inputStr = "";
        Util.stripLeadingAndTrailingQuotes(inputStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphens_EmptyString() {
        String inputStr = "";
        Util.stripLeadingHyphens(inputStr);
    }
}