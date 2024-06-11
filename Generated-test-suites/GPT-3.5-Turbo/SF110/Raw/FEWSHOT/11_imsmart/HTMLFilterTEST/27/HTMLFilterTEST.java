package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;script&gt;", HTMLFilter.filter("<script>"));
        assertEquals("&gt;script&lt;", HTMLFilter.filter(">script<"));
        assertEquals("&amp;script&amp;", HTMLFilter.filter("&script&"));
        assertEquals("&quot;script&quot;", HTMLFilter.filter("\"script\""));
        assertEquals("text", HTMLFilter.filter("text"));
        assertEquals("", HTMLFilter.filter(""));
        assertNull(HTMLFilter.filter(null));
    }
}