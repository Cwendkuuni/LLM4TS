package com.imsmart.servlet;

import org.junit.Test;

import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsOriginalMessage() {
        String message = "Hello, World!";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_LessThanSymbol_ReplacesWithHtmlEntity() {
        String message = "2 < 5";
        String expected = "2 &lt; 5";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_GreaterThanSymbol_ReplacesWithHtmlEntity() {
        String message = "10 > 5";
        String expected = "10 &gt; 5";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_AmpersandSymbol_ReplacesWithHtmlEntity() {
        String message = "AT&T";
        String expected = "AT&amp;T";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_DoubleQuoteSymbol_ReplacesWithHtmlEntity() {
        String message = "He said, \"Hello!\"";
        String expected = "He said, &quot;Hello!&quot;";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    // Add more test cases as needed

}