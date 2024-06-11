package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // test with all special characters
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));

        // test with empty string
        assertEquals("", HTMLFilter.filter(""));

        // test with null input
        assertNull(HTMLFilter.filter(null));

        // test with characters other than special characters
        assertEquals("abc123", HTMLFilter.filter("abc123"));

        // test with mixed characters
        assertEquals("abc&lt;&gt;&amp;&quot;123", HTMLFilter.filter("abc<>&\"123"));
    }
}