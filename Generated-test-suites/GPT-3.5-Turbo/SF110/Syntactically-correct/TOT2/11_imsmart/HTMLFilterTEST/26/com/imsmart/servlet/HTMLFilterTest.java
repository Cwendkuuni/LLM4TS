package com.imsmart.servlet;
import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    @Test
    public void testFilter_EmptyString() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    @Test
    public void testFilter_NoHTMLChars() {
        String message = "This is a test message";
        String result = HTMLFilter.filter(message);
        assertEquals(message, result);
    }

    @Test
    public void testFilter_WithHTMLChars() {
        String message = "<div>This is a <b>bold</b> message &amp; test</div>";
        String expected = "&lt;div&gt;This is a &lt;b&gt;bold&lt;/b&gt; message &amp;amp; test&lt;/div&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_SpecialCharacters() {
        String message = "Special characters: < > & \"";
        String expected = "Special characters: &lt; &gt; &amp; &quot;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }
}