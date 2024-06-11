package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--test";
        String expected1 = "test";
        String output1 = Util.stripLeadingHyphens(input1);
        assertEquals(expected1, output1);

        String input2 = "-test";
        String expected2 = "test";
        String output2 = Util.stripLeadingHyphens(input2);
        assertEquals(expected2, output2);

        String input3 = "test";
        String expected3 = "test";
        String output3 = Util.stripLeadingHyphens(input3);
        assertEquals(expected3, output3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"test\"";
        String expected1 = "test";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expected1, output1);

        String input2 = "\"test";
        String expected2 = "\"test";
        String output2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expected2, output2);

        String input3 = "test\"";
        String expected3 = "test\"";
        String output3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expected3, output3);

        String input4 = "test";
        String expected4 = "test";
        String output4 = Util.stripLeadingAndTrailingQuotes(input4);
        assertEquals(expected4, output4);
    }
}
