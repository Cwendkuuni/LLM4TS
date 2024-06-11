package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // Test cases for special characters
        assertEquals("&lt;", HTMLFilter.filter("<"));
        assertEquals("&gt;", HTMLFilter.filter(">"));
        assertEquals("&amp;", HTMLFilter.filter("&"));
        assertEquals("&quot;", HTMLFilter.filter("\""));
        
        // Test cases for normal text
        assertEquals("abc", HTMLFilter.filter("abc"));
        assertEquals("123", HTMLFilter.filter("123"));
        assertEquals("abc123", HTMLFilter.filter("abc123"));
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));

        // Test cases for null input
        assertNull(HTMLFilter.filter(null));
    }
}