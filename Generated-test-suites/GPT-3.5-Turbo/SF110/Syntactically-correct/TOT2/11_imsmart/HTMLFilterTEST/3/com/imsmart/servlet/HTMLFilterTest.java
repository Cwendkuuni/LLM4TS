package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnNull() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyString_ReturnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnOriginalString() {
        String message = "This is a test string";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_LessThanCharacter_ReturnCorrectlyFilteredString() {
        String message = "This is < a test string";
        assertEquals("This is &lt; a test string", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_GreaterThanCharacter_ReturnCorrectlyFilteredString() {
        String message = "This is > a test string";
        assertEquals("This is &gt; a test string", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_AmpersandCharacter_ReturnCorrectlyFilteredString() {
        String message = "This is & a test string";
        assertEquals("This is &amp; a test string", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_QuoteCharacter_ReturnCorrectlyFilteredString() {
        String message = "This is \" a test string";
        assertEquals("This is &quot; a test string", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MixedSpecialCharacters_ReturnCorrectlyFilteredString() {
        String message = "<This is > \" an & example string";
        assertEquals("&lt;This is &gt; &quot; an &amp; example string", HTMLFilter.filter(message));
    }

}