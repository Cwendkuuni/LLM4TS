package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }

    @Test
    public void testFilter_MessageWithLessThanSymbol_ReturnsFilteredMessage() {
        String message = "<hello>";
        String expected = "&lt;hello&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithGreaterThanSymbol_ReturnsFilteredMessage() {
        String message = "4 > 2";
        String expected = "4 &gt; 2";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithAmpersandSymbol_ReturnsFilteredMessage() {
        String message = "AT&T";
        String expected = "AT&amp;T";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithDoubleQuoteSymbol_ReturnsFilteredMessage() {
        String message = "She said, \"Hello\"";
        String expected = "She said, &quot;Hello&quot;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithoutSpecialCharacters_ReturnsSameMessage() {
        String message = "Just a normal message";
        String expected = "Just a normal message";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }
}