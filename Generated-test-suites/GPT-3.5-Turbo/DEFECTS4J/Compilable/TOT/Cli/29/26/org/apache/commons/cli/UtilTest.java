package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str1 = "--test";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("test", result1);
        
        // Edge case: no hyphen at the beginning
        String str2 = "test";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("test", result2);
        
        // Edge case: multiple hyphens at the beginning
        String str3 = "----test";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("---test", result3);
        
        // Error scenario: null string
        String str4 = null;
        String result4 = Util.stripLeadingHyphens(str4);
        assertNull(result4);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str1 = "\"test\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("test", result1);
        
        // Edge case: no leading quotes
        String str2 = "test\"";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("test\"", result2);
        
        // Edge case: no trailing quotes
        String str3 = "\"test";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("\"test", result3);
        
        // Edge case: multiple leading and trailing quotes
        String str4 = "\"\"test\"\"";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("\"test\"", result4);
        
        // Error scenario: empty string
        String str5 = "";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        assertEquals("", result5);
    }
}