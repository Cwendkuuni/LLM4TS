package com.imsmart.servlet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;script&gt;", HTMLFilter.filter("<script>"));
        assertEquals("&gt;&lt;&amp;&quot;", HTMLFilter.filter("><&\""));
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));
        assertNull(HTMLFilter.filter(null));
        assertEquals("", HTMLFilter.filter(""));
    }
}