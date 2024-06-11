package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // Test when message is null
        assertNull(HTMLFilter.filter(null));
        
        // Test when message is empty
        assertEquals("", HTMLFilter.filter(""));
        
        // Test when message contains no special characters
        assertEquals("hello", HTMLFilter.filter("hello"));
        
        // Test when message contains only <
        assertEquals("&lt;", HTMLFilter.filter("<"));
        
        // Test when message contains only >
        assertEquals("&gt;", HTMLFilter.filter(">"));
        
        // Test when message contains only &
        assertEquals("&amp;", HTMLFilter.filter("&"));
        
        // Test when message contains only "
        assertEquals("&quot;", HTMLFilter.filter("\""));
        
        // Test when message contains a combination of special characters
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
        
        // Test when message contains special characters in between other text
        assertEquals("hello&lt;world&gt;", HTMLFilter.filter("hello<world>"));
        
        // Test when message contains special characters at the beginning and end
        assertEquals("&lt;hello&gt;", HTMLFilter.filter("<hello>"));
    }
}