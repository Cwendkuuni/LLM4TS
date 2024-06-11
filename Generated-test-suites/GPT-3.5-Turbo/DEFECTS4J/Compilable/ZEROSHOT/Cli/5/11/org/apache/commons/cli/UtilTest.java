package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";
        
        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);
        
        assertEquals("test", result1);
        assertEquals("test", result2);
        assertEquals("test", result3);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "test\"";
        String str3 = "\"test";
        String str4 = "test";
        
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        
        assertEquals("test", result1);
        assertEquals("test", result2);
        assertEquals("test", result3);
        assertEquals("test", result4);
    }
}