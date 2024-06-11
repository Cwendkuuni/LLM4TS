package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        assertEquals("&lt;script&gt;", HTMLFilter.filter("<script>"));
        assertEquals("&lt;a href=&quot;https://www.example.com&quot;&gt;", HTMLFilter.filter("<a href=\"https://www.example.com\">"));
        assertEquals("&lt;p&gt;Hello World!&lt;/p&gt;", HTMLFilter.filter("<p>Hello World!</p>"));
        assertEquals("&lt;h1&gt;&amp;lt;Test&amp;gt;&lt;/h1&gt;", HTMLFilter.filter("<h1>&lt;Test&gt;</h1>"));
        assertEquals("&amp;amp", HTMLFilter.filter("&amp"));
        assertEquals("", HTMLFilter.filter(""));
        assertEquals(null, HTMLFilter.filter(null));
    }
}