package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // typical use case
        String str1 = "--example";
        String expected1 = "example";
        assertEquals(expected1, Util.stripLeadingHyphens(str1));
        
        // edge case: empty string
        String str2 = "";
        String expected2 = "";
        assertEquals(expected2, Util.stripLeadingHyphens(str2));
        
        // error scenario: null input
        String str3 = null;
        assertNull(Util.stripLeadingHyphens(str3));
        
        // edge case: input starts with a single hyphen
        String str4 = "-example";
        String expected4 = "example";
        assertEquals(expected4, Util.stripLeadingHyphens(str4));
        
        // edge case: input starts with multiple hyphens
        String str5 = "---example";
        String expected5 = "example";
        assertEquals(expected5, Util.stripLeadingHyphens(str5));
        
        // typical use case: input without hyphens
        String str6 = "example";
        String expected6 = "example";
        assertEquals(expected6, Util.stripLeadingHyphens(str6));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // typical use case
        String str1 = "\"one two\"";
        String expected1 = "one two";
        assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(str1));
        
        // edge case: empty string
        String str2 = "";
        String expected2 = "";
        assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(str2));
        
        // edge case: no leading and trailing quotes
        String str3 = "example";
        String expected3 = "example";
        assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(str3));
        
        // error scenario: null input
        String str4 = null;
        assertNull(Util.stripLeadingAndTrailingQuotes(str4));
        
        // edge case: leading and trailing quotes only
        String str5 = "\"\"";
        String expected5 = "";
        assertEquals(expected5, Util.stripLeadingAndTrailingQuotes(str5));
        
        // edge case: multiple leading and trailing quotes
        String str6 = "\"\"one two\"\"";
        String expected6 = "\"one two\"";
        assertEquals(expected6, Util.stripLeadingAndTrailingQuotes(str6));
    }
}