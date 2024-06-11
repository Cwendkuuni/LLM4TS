package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilter_ReplaceLessThanSymbol() {
        String input = "This is a < test";
        String expected = "This is a &lt; test";
        
        String result = HTMLFilter.filter(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ReplaceGreaterThanSymbol() {
        String input = "This is a > test";
        String expected = "This is a &gt; test";
        
        String result = HTMLFilter.filter(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ReplaceAmpersandSymbol() {
        String input = "This is a test & message";
        String expected = "This is a test &amp; message";
        
        String result = HTMLFilter.filter(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ReplaceDoubleQuoteSymbol() {
        String input = "This is a test \" message";
        String expected = "This is a test &quot; message";
        
        String result = HTMLFilter.filter(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String input = null;
        
        String result = HTMLFilter.filter(input);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testFilter_EmptyInput_ReturnsEmptyString() {
        String input = "";
        
        String result = HTMLFilter.filter(input);
        
        assertEquals("", result);
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnsOriginalString() {
        String input = "This is a simple string";
        
        String result = HTMLFilter.filter(input);
        
        assertEquals(input, result);
    }
}