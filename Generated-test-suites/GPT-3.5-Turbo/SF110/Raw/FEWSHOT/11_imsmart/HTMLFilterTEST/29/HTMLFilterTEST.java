package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // Test null input
        assertNull(HTMLFilter.filter(null));

        // Test input without special characters
        assertEquals("abc", HTMLFilter.filter("abc"));

        // Test input with special characters
        assertEquals("&lt;abc&gt;", HTMLFilter.filter("<abc>"));
        assertEquals("&amp;lt;abc&amp;gt;", HTMLFilter.filter("&lt;abc&gt;"));
        assertEquals("&amp;amp;lt;abc&amp;amp;gt;", HTMLFilter.filter("&amp;lt;abc&amp;gt;"));

        // Test input with multiple special characters
        assertEquals("&lt;&lt;abc&gt;&gt;", HTMLFilter.filter("<<abc>>"));
        assertEquals("&amp;lt;&amp;lt;abc&amp;gt;&amp;gt;", HTMLFilter.filter("&lt;&lt;abc&gt;&gt;"));
        assertEquals("&amp;amp;lt;&amp;amp;lt;abc&amp;amp;gt;&amp;amp;gt;", HTMLFilter.filter("&amp;lt;&lt;abc&gt;&gt;"));

        // Test input with quotes
        assertEquals("abc&quot;def", HTMLFilter.filter("abc\"def"));
        assertEquals("abc&amp;quot;def", HTMLFilter.filter("abc&quot;def"));
        assertEquals("abc&amp;amp;quot;def", HTMLFilter.filter("abc&amp;quot;def"));

        // Test input with combination of special characters
        assertEquals("&lt;&lt;abc&gt;&gt;!@#$%^&amp;*()_+=-|\"':;&amp;lt;&amp;gt;", HTMLFilter.filter("<<abc>>!@#$%^&*()_+=-|\":;'<>"));
        assertEquals("&amp;lt;&amp;lt;abc&amp;gt;&amp;gt;!@#$%^&amp;amp;*()_+=-|&amp;quot;':;&amp;amp;lt;&amp;amp;gt;", HTMLFilter.filter("&lt;&lt;abc&gt;&gt;!@#$%^&amp;*()_+=-|\"':;><"));
        assertEquals("&amp;amp;lt;&amp;amp;lt;abc&amp;amp;gt;&amp;amp;gt;!@#$%^&amp;amp;amp;*()_+=-|&amp;amp;quot;':;&amp;amp;amp;lt;&amp;amp;amp;gt;", HTMLFilter.filter("&amp;lt;&lt;abc&gt;&gt;!@#$%^&amp;amp;*()_+=-|\"':;><"));

    }
}