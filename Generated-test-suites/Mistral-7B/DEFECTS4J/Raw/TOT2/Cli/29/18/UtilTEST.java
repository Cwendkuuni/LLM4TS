package org.apache.commons.cli;

import org.junit.Test;
import org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--str";
        String expectedOutput1 = "str";
        String output1 = Util.stripLeadingHyphens(input1);
        assertEquals(expectedOutput1, output1);

        String input2 = "str";
        String expectedOutput2 = "str";
        String output2 = Util.stripLeadingHyphens(input2);
        assertEquals(expectedOutput2, output2);

        String input3 = "---str";
        String expectedOutput3 = "str";
        String output3 = Util.stripLeadingHyphens(input3);
        assertEquals(expectedOutput3, output3);

        String input4 = null;
        String output4 = Util.stripLeadingHyphens(input4);
        assertEquals(null, output4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"one two\"";
        String expectedOutput1 = "one two";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expectedOutput1, output1);

        String input2 = "\"\"";
        String expectedOutput2 = "";
        String output2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expectedOutput2, output2);

        String input3 = "one two";
        String expectedOutput3 = "one two";
        String output3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expectedOutput3, output3);

        String input4 = "\"\"one two\"\"";
        String expectedOutput4 = "one two";
        String output4 = Util.stripLeadingAndTrailingQuotes(input4);
        assertEquals(expectedOutput4, output4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithMultipleQuotes() {
        String input1 = "\"\"one two\"\"";
        String expectedOutput1 = "one two";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expectedOutput1, output1);
    }

    @Test
    public void testStripLeadingHyphensWithTrailingHyphens() {
        String input1 = "--str--";
        String expectedOutput1 = "str";
        String output1 = Util.stripLeadingHyphens(input1);
        assertEquals(expectedOutput1, output1);
    }

    @Test
    public void testStripTrailingHyphensWithLeadingHyphens() {
        String input1 = "--str";
        String expectedOutput1 = "str";
        String output1 = Util.stripLeadingAndTrailingHyphens(input1);
        assertEquals(expectedOutput1, output1);
    }
}