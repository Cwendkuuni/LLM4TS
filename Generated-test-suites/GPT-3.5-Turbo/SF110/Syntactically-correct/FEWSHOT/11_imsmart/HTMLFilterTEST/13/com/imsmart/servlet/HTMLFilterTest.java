package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;HTML&gt;", HTMLFilter.filter("<HTML>"));
        assertEquals("&gt;HTML&lt;", HTMLFilter.filter(">HTML<"));
        assertEquals("&amp;HTML&amp;", HTMLFilter.filter("&HTML&"));
        assertEquals("&quot;HTML&quot;", HTMLFilter.filter("\"HTML\""));
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));
        assertNull(HTMLFilter.filter(null));
    }
}