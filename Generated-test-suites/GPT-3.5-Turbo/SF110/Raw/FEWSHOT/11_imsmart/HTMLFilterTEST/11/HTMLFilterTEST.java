package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Test case: message containing "<"
        assertEquals("&lt;p&gt;", HTMLFilter.filter("<p>"));

        // Test case: message containing ">"
        assertEquals("&gt;div&lt;", HTMLFilter.filter(">div<"));

        // Test case: message containing "&"
        assertEquals("&amp;", HTMLFilter.filter("&"));

        // Test case: message containing """
        assertEquals("&quot;", HTMLFilter.filter("\""));

        // Test case: message containing a combination of special characters
        assertEquals("&lt;p&gt;&gt;&amp;", HTMLFilter.filter("<p>>&"));

        // Test case: message containing no special characters
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));

        // Test case: message is null
        assertNull(HTMLFilter.filter(null));
    }
}