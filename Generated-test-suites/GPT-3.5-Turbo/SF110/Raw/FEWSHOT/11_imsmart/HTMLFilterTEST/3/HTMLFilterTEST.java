package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // Test when message contains "<"
        assertEquals("&lt;foo&gt;", HTMLFilter.filter("<foo>"));
        // Test when message contains ">"
        assertEquals("foo&lt;bar&gt;", HTMLFilter.filter("foo<bar>"));
        // Test when message contains "&"
        assertEquals("&lt;foo&gt; and &lt;bar&gt;", HTMLFilter.filter("<foo> and <bar>"));
        // Test when message contains """
        assertEquals("&quot;foo&quot;", HTMLFilter.filter("\"foo\""));
        // Test when message contains a combination of special characters
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
        // Test when message is null
        assertNull(HTMLFilter.filter(null));
        // Test when message is empty
        assertEquals("", HTMLFilter.filter(""));
    }
}