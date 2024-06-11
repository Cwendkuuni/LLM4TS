package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--input";
        String input2 = "-input";
        String input3 = "input";
        
        String result1 = Util.stripLeadingHyphens(input1);
        String result2 = Util.stripLeadingHyphens(input2);
        String result3 = Util.stripLeadingHyphens(input3);
        
        assertEquals(result1, "input");
        assertEquals(result2, "input");
        assertEquals(result3, "input");
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"input\"";
        String input2 = "input";
        
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        
        assertEquals(result1, "input");
        assertEquals(result2, "input");
    }
}