package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;foo&gt;", HTMLFilter.filter("<foo>"));
        assertEquals("&lt;foo&gt; &amp; &lt;bar&gt;", HTMLFilter.filter("<foo> & <bar>"));
        assertEquals("&amp;quot;foo&amp;quot;", HTMLFilter.filter("\"foo\""));
        assertEquals("", HTMLFilter.filter(""));
        assertNull(HTMLFilter.filter(null));
    }
}