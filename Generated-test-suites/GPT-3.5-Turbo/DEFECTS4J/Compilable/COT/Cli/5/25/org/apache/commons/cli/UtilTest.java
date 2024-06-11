package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str1));
        
        String str2 = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str2));
        
        String str3 = "test";
        assertEquals("test", Util.stripLeadingHyphens(str3));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"one two\"";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str1));
        
        String str2 = "\"one two";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str2));

        String str3 = "one two\"";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str3));
        
        String str4 = "one two";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str4));
    }
}
