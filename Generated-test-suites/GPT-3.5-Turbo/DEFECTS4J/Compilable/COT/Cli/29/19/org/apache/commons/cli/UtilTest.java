package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--test";
        String expected1 = "test";
        String input2 = "-test";
        String expected2 = "test";
        
        assertEquals(expected1, Util.stripLeadingHyphens(input1));
        assertEquals(expected2, Util.stripLeadingHyphens(input2));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"test\"";
        String expected1 = "test";
        String input2 = "test\"";
        String expected2 = "test";
        
        assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(input1));
        assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(input2));
    }

}