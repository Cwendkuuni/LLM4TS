package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;script&gt;", HTMLFilter.filter("<script>"));
        assertEquals("&gt;div&lt;", HTMLFilter.filter(">div<"));
        assertEquals("&amp;pound;", HTMLFilter.filter("&pound;"));
        assertEquals("&quot;Hello&quot;", HTMLFilter.filter("\"Hello\""));
        assertNull(HTMLFilter.filter(null));
    }
}