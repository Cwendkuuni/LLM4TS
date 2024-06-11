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

        assertEquals("test", HTMLFilter.filter("test"));
        assertEquals("", HTMLFilter.filter(""));
        assertNull(HTMLFilter.filter(null));

        // additional test cases
        assertEquals("&lt;&gt;", HTMLFilter.filter("<>"));
        assertEquals("test&amp;", HTMLFilter.filter("test&"));
        assertEquals("&quot;test", HTMLFilter.filter("\"test"));
        assertEquals("<&>", HTMLFilter.filter("<&>"));
    }
}