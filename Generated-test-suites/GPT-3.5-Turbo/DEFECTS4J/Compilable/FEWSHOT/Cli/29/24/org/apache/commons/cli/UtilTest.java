package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1
        String result1 = Util.stripLeadingAndTrailingQuotes("\"foo\"");
        assertEquals("foo", result1); 
        
        // Test case 2
        String result2 = Util.stripLeadingAndTrailingQuotes("foo \"bar\"");
        assertEquals("foo \"bar\"", result2); 
        
        // Test case 3
        String result3 = Util.stripLeadingAndTrailingQuotes("\"foo\" bar");
        assertEquals("\"foo\" bar", result3); 
        
        // Test case 4
        String result4 = Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\"");
        assertEquals("\"foo\" and \"bar\"", result4); 
        
        // Test case 5
        String result5 = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", result5); 
    }

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1
        String result1 = Util.stripLeadingHyphens("-f");
        assertEquals("f", result1);
        
        // Test case 2
        String result2 = Util.stripLeadingHyphens("--foo");
        assertEquals("foo", result2);
        
        // Test case 3
        String result3 = Util.stripLeadingHyphens("---foo");
        assertEquals("-foo", result3);
        
        // Test case 4
        String result4 = Util.stripLeadingHyphens(null);
        assertNull(result4);
    }
}