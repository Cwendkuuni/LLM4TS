package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;", HTMLFilter.filter("<"));
        assertEquals("&gt;", HTMLFilter.filter(">"));
        assertEquals("&amp;", HTMLFilter.filter("&"));
        assertEquals("&quot;", HTMLFilter.filter("\""));
        assertEquals("abc", HTMLFilter.filter("abc"));
        assertEquals("abc&lt;", HTMLFilter.filter("abc<"));
        assertEquals("&gt;abc", HTMLFilter.filter(">abc"));
        assertEquals("abc&amp;def", HTMLFilter.filter("abc&def"));
        assertEquals("&quot;abc&quot;", HTMLFilter.filter("\"abc\""));
        assertNull(HTMLFilter.filter(null));
    }
}