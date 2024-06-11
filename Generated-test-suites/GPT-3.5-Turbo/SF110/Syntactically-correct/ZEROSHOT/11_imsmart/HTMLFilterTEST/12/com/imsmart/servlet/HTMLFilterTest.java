package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_nullInput() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_noSpecialCharacters() {
        String message = "Hello World";
        assertEquals("Hello World", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_withSpecialCharacters() {
        String message = "<Hello> & World\"";
        assertEquals("&lt;Hello&gt; &amp; World&quot;", HTMLFilter.filter(message));
    }

    // Add more test methods for additional scenarios

}