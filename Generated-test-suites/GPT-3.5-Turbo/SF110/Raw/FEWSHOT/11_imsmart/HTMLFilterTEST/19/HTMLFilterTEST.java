
package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter() {
        // Test case with all special characters
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));

        // Test case with no special characters
        assertEquals("hello world", HTMLFilter.filter("hello world"));

        // Test case with null message
        assertNull(HTMLFilter.filter(null));

        // Test case with empty message
        assertEquals("", HTMLFilter.filter(""));
    }
}
