package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        String message = "This is a test message.";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsLessThanCharacter() {
        String message = "This<is a test message.";
        assertEquals("This&lt;is a test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsGreaterThanCharacter() {
        String message = "This>is a test message.";
        assertEquals("This&gt;is a test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsAmpersandCharacter() {
        String message = "This&is a test message.";
        assertEquals("This&amp;is a test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsDoubleQuoteCharacter() {
        String message = "This\"is a test message.";
        assertEquals("This&quot;is a test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsMultipleSpecialCharacters() {
        String message = "This< is >a & test\" message.";
        assertEquals("This&lt; is &gt;a &amp; test&quot; message.", HTMLFilter.filter(message));
    }

}