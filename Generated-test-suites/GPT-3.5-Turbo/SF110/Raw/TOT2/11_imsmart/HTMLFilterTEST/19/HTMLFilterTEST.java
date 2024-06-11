package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilter_nullInput() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_emptyInput() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_basicInput() {
        assertEquals("&lt;Hello World&gt;", HTMLFilter.filter("<Hello World>"));
    }

    @Test
    public void testFilter_edgeCases() {
        // Test with special characters
        assertEquals("&amp;&lt;&gt;&quot;", HTMLFilter.filter("&<>\""));

        // Test with long input
        String longInput = String.join("", Collections.nCopies(1000, "<>\"&"));
        assertEquals(String.join("", Collections.nCopies(1000, "&lt;&gt;&quot;&amp;")), HTMLFilter.filter(longInput));
    }

    @Test
    public void testFilter_exceptionCases() {
        // Test with message containing null characters
        String messageWithNullChar = "Hello\0World";
        assertEquals("Hello\0World", HTMLFilter.filter(messageWithNullChar));
    }
}