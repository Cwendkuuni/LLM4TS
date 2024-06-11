package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testNullMessage() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testEmptyMessage() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    @Test
    public void testNoSpecialCharacters() {
        String message = "This is a test message";
        String result = HTMLFilter.filter(message);
        assertEquals(message, result);
    }

    @Test
    public void testLessThanCharacter() {
        String message = "<test>";
        String result = HTMLFilter.filter(message);
        assertEquals("&lt;test&gt;", result);
    }

    @Test
    public void testGreaterThanCharacter() {
        String message = ">test<";
        String result = HTMLFilter.filter(message);
        assertEquals("&gt;test&lt;", result);
    }

    @Test
    public void testAmpersandCharacter() {
        String message = "A & B";
        String result = HTMLFilter.filter(message);
        assertEquals("A &amp; B", result);
    }

    @Test
    public void testDoubleQuoteCharacter() {
        String message = "Quote \" test";
        String result = HTMLFilter.filter(message);
        assertEquals("Quote &quot; test", result);
    }
}

