package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;abc", HTMLFilter.filter("<abc"));
        assertEquals("abc&gt;", HTMLFilter.filter("abc>"));
        assertEquals("abc&amp;def", HTMLFilter.filter("abc&def"));
        assertEquals("abc&quot;def", HTMLFilter.filter("abc\"def"));
        assertEquals("a&amp;b&lt;c&gt;d&quot;e", HTMLFilter.filter("a&b<c>d\"e"));
        assertEquals("", HTMLFilter.filter(null));
        assertEquals("", HTMLFilter.filter(""));
    }
}