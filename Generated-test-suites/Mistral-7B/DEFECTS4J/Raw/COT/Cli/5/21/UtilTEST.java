package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--str";
        String expectedOutput1 = "str";
        String output1 = Util.stripLeadingHyphens(input1);
        assertEquals(expectedOutput1, output1);

        String input2 = "-str";
        String expectedOutput2 = "str";
        String output2 = Util.stripLeadingHyphens(input2);
        assertEquals(expectedOutput2, output2);

        String input3 = "str";
        String expectedOutput3 = "str";
        String output3 = Util.stripLeadingHyphens(input3);
        assertEquals(expectedOutput3, output3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"one two\"";
        String expectedOutput1 = "one two";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expectedOutput1, output1);

        String input2 = "";
        String expectedOutput2 = "";
        String output2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expectedOutput2, output2);

        String input3 = "one two";
        String expectedOutput3 = "one two";
        String output3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expectedOutput3, output3);

        String input4 = "\"\"";
        String expectedOutput4 = "";
        String output4 = Util.stripLeadingAndTrailingQuotes(input4);
        assertEquals(expectedOutput4, output4);

        String input5 = null;
        String expectedOutput5 = null;
        String output5 = Util. stripLeadingAndTrailingQuotes(input5);
        assertNull(output5);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}