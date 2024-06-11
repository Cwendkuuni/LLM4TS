package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage() {
        String message = null;
        assertNull(HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_EmptyMessage() {
        String message = "";
        assertEquals("", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        String message = "This message does not contain any special characters.";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_LessThanCharacter() {
        String message = "This message contains the less than character <.";
        String expected = "This message contains the less than character &lt;.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_GreaterThanCharacter() {
        String message = "This message contains the greater than character >.";
        String expected = "This message contains the greater than character &gt;.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_AmpersandCharacter() {
        String message = "This message contains the ampersand character &.";
        String expected = "This message contains the ampersand character &amp;.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_QuoteCharacter() {
        String message = "This message contains the quote character \".";
        String expected = "This message contains the quote character &quot;.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_CombinationOfCharacters() {
        String message = "<This> message &contains \"a\" combination of <special> characters.";
        String expected = "&lt;This&gt; message &amp;contains &quot;a&quot; combination of &lt;special&gt; characters.";
        assertEquals(expected, HTMLFilter.filter(message));
    }
}