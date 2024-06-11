package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input1 = null;
        assertEquals(null, HTMLFilter.filter(input1));

        String input2 = "";
        assertEquals("", HTMLFilter.filter(input2));

        String input3 = "<script>";
        assertEquals("&lt;script&gt;", HTMLFilter.filter(input3));

        String input4 = "Hello, <b>world</b>!";
        assertEquals("Hello, &lt;b&gt;world&lt;/b&gt;!", HTMLFilter.filter(input4));

        String input5 = "This is an & symbol";
        assertEquals("This is an &amp; symbol", HTMLFilter.filter(input5));
    }
}