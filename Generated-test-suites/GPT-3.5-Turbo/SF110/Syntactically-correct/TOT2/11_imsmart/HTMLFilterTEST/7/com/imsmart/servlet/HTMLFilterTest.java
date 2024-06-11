
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    
    @Test
    public void testFilter() {
        // typical use case
        String input = "<p>This is a test</p>";
        String expected = "&lt;p&gt;This is a test&lt;/p&gt;";
        assertEquals(expected, HTMLFilter.filter(input));
        
        // edge case
        input = ">";
        expected = "&gt;";
        assertEquals(expected, HTMLFilter.filter(input));
        
        // error scenario
        input = null;
        assertEquals(null, HTMLFilter.filter(input));
    }
}
