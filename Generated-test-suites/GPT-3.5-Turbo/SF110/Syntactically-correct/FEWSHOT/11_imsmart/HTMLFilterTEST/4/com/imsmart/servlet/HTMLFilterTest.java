package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;tag&gt;", HTMLFilter.filter("<tag>"));
        assertEquals("&gt;", HTMLFilter.filter(">"));
        assertEquals("&amp;", HTMLFilter.filter("&"));
        assertEquals("&quot;", HTMLFilter.filter("\""));
        assertEquals("content", HTMLFilter.filter("content"));
        assertNull(HTMLFilter.filter(null));
    }
}