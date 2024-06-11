package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter() {
        assertEquals("&lt;div&gt;", HTMLFilter.filter("<div>"));
        assertEquals("&gt;div&lt;", HTMLFilter.filter(">div<"));
        assertEquals("&amp;&quot;hello&quot;&amp;", HTMLFilter.filter("&\"hello\"&"));
        assertEquals("abc", HTMLFilter.filter("abc"));
        assertNull(HTMLFilter.filter(null));
        assertEquals("", HTMLFilter.filter(""));
    }
}