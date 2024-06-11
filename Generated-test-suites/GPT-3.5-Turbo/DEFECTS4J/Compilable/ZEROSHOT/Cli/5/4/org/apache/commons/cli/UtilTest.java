package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--test";
        String input2 = "-test";

        String result1 = Util.stripLeadingHyphens(input1);
        String result2 = Util.stripLeadingHyphens(input2);

        assertEquals("test", result1);
        assertEquals("test", result2);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"one two\"";
        String input2 = "one two\"";

        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);

        assertEquals("one two", result1);
        assertEquals("one two\"", result2);
    }
}