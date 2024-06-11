package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertNull(HTMLFilter.filter(null));
        assertEquals("", HTMLFilter.filter(""));
        assertEquals("&lt;", HTMLFilter.filter("<"));
        assertEquals("&gt;", HTMLFilter.filter(">"));
        assertEquals("&amp;", HTMLFilter.filter("&"));
        assertEquals("&quot;", HTMLFilter.filter("\""));
        assertEquals("hello", HTMLFilter.filter("hello"));
        assertEquals("&lt;img src=&quot;image.jpg&quot;&gt;", HTMLFilter.filter("<img src=\"image.jpg\">"));
        assertEquals("&lt;a href=&quot;http://www.example.com&quot;&gt;link&lt;/a&gt;", HTMLFilter.filter("<a href=\"http://www.example.com\">link</a>"));
    }
}