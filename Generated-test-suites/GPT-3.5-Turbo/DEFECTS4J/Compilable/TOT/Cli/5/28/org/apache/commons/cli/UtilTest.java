package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str1 = "--example";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("example", result1);
        
        // Edge case: empty string
        String str2 = "";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("", result2);
        
        // Error scenario: no hyphens
        String str3 = "example";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("example", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str1 = "\"example\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("example", result1);
        
        // Edge case: empty string
        String str2 = "";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("", result2);
        
        // Edge case: no quotes
        String str3 = "example";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("example", result3);
        
        // Error scenario: single leading quote
        String str4 = "\"example";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("\"example", result4);
        
        // Error scenario: single trailing quote
        String str5 = "example\"";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        assertEquals("example\"", result5);
    }

}

