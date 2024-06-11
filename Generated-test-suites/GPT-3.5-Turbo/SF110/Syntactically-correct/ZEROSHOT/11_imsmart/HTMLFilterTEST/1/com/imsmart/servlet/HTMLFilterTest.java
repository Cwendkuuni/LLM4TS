package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String message = null;
        String result = HTMLFilter.filter(message);
        assertNull(result);
    }
    
    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String message = "";
        String result = HTMLFilter.filter(message);
        assertEquals("", result);
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnsMessage() {
        String message = "Hello World";
        String result = HTMLFilter.filter(message);
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testFilter_MessageWithLessThanCharacter_ReturnsFilteredResult() {
        String message = "This is less than <.";
        String result = HTMLFilter.filter(message);
        assertEquals("This is less than &lt;.", result);
    }
    
    @Test
    public void testFilter_MessageWithGreaterThanCharacter_ReturnsFilteredResult() {
        String message = "This is greater than >.";
        String result = HTMLFilter.filter(message);
        assertEquals("This is greater than &gt;.", result);
    }
    
    @Test
    public void testFilter_MessageWithAmpersandCharacter_ReturnsFilteredResult() {
        String message = "This is an ampersand &.";
        String result = HTMLFilter.filter(message);
        assertEquals("This is an ampersand &amp;.", result);
    }
    
    @Test
    public void testFilter_MessageWithQuoteCharacter_ReturnsFilteredResult() {
        String message = "This is a quote character \".";
        String result = HTMLFilter.filter(message);
        assertEquals("This is a quote character &quot;.", result);
    }
}