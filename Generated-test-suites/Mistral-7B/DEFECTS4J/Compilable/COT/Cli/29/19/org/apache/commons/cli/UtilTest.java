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

        inputStr = "test";
        expectedOutput = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = null;
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertNull(outputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String inputStr = "\"test1 test2\"";
        String expectedOutput = "test1 test2";
        String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "test1 test2";
        expectedOutput = "test1 test2";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("", outputStr);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput() {
        String inputStr = null;
        Util.stripLeadingHyphens(inputStr);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String inputStr = "";
        Util.stripLeadingAndTrailingQuotes(inputStr);
    }
}