package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--example";
        String expected1 = "example";
        String result1 = Util.stripLeadingHyphens(input1);
        assertEquals(expected1, result1);

        String input2 = "-example";
        String expected2 = "example";
        String result2 = Util.stripLeadingHyphens(input2);
        assertEquals(expected2, result2);

        String input3 = "example";
        String expected3 = "example";
        String result3 = Util.stripLeadingHyphens(input3);
        assertEquals(expected3, result3);

        String input4 = null;
        String expected4 = null;
        String result4 = Util.stripLeadingHyphens(input4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"example\"";
        String expected1 = "example";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expected1, result1);

        String input2 = "\"example";
        String expected2 = "\"example";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expected2, result2);

        String input3 = "example\"";
        String expected3 = "example\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expected3, result3);

        String input4 = "example";
        String expected4 = "example";
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);
        assertEquals(expected4, result4);
    }

}