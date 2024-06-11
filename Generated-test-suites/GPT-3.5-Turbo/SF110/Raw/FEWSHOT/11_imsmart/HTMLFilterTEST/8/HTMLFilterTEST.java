package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;Hello World&gt;", HTMLFilter.filter("<Hello World>"));
        assertEquals("&gt;Hello World&lt;", HTMLFilter.filter(">Hello World<"));
        assertEquals("&amp;Hello World", HTMLFilter.filter("&Hello World"));
        assertEquals("&quot;Hello World&quot;", HTMLFilter.filter("\"Hello World\""));
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));
        assertNull(HTMLFilter.filter(null));
    }
}