package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testNullFilter() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testEmptyFilter() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilterWithNoSpecialCharacters() {
        assertEquals("abc", HTMLFilter.filter("abc"));
    }
    
    @Test
    public void testFilterWithLessThan() {
        assertEquals("&lt;abc", HTMLFilter.filter("<abc"));
    }
    
    @Test
    public void testFilterWithGreaterThan() {
        assertEquals("abc&gt;", HTMLFilter.filter("abc>"));
    }
    
    @Test
    public void testFilterWithAmpersand() {
        assertEquals("abc&amp;", HTMLFilter.filter("abc&"));
    }
    
    @Test
    public void testFilterWithDoubleQuotes() {
        assertEquals("abc&quot;", HTMLFilter.filter("abc\""));
    }
    
    @Test
    public void testFilterWithMultipleSpecialCharacters() {
        assertEquals("&lt;abc&gt;&amp;xyz&quot;", HTMLFilter.filter("<abc>&xyz\""));
    }
}