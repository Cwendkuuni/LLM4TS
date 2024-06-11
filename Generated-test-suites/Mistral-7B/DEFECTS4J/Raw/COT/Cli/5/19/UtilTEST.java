package org.apache.commons.cli.test;

import org.junit.Test;
import org.junit.Assert.*;
import static org.apache.commons.cli.Util.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String inputStr = "--test";
        String expectedOutput = "test";
        String outputStr = stripLeadingHyphens(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "test";
        outputStr = stripLeadingHyphens(inputStr);
        assertEquals(inputStr, outputStr);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensWithNullString() {
        String inputStr = null;
        stripLeadingHyphens(inputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String inputStr = "\"test\"";
        String expectedOutput = "test";
        String outputStr = stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(expectedOutput, outputStr);

        inputStr = "test";
        outputStr = stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(inputStr, outputStr);

        inputStr = "";
        outputStr = stripLeadingAndTrailingQuotes(inputStr);
        assertEquals(inputStr, outputStr);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        String inputStr = null;
        stripLeadingAndTrailingQuotes(inputStr);
    }
}