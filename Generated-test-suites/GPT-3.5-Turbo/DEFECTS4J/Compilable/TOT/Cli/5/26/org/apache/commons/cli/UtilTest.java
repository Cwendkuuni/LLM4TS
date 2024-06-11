package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case - string starts with "--"
        String str1 = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str1));
        
        // Typical use case - string starts with "-"
        String str2 = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str2));
        
        // Edge case - empty string
        String str3 = "";
        assertEquals("", Util.stripLeadingHyphens(str3));
        
        // Edge case - string does not start with hyphens
        String str4 = "example";
        assertEquals("example", Util.stripLeadingHyphens(str4));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case - string starts and ends with double quotes
        String str1 = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));
        
        // Typical use case - string starts and ends with single quotes
        String str2 = "'example'";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str2));
        
        // Edge case - empty string
        String str3 = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str3));
        
        // Edge case - string does not start or end with quotes
        String str4 = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str4));
        
        // Error scenario - string starts with quote but does not end with quote
        String str5 = "\"example";
        assertEquals("\"example", Util.stripLeadingAndTrailingQuotes(str5));
        
        // Error scenario - string ends with quote but does not start with quote
        String str6 = "example\"";
        assertEquals("example\"", Util.stripLeadingAndTrailingQuotes(str6));
    }
}