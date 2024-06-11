package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyInput() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_AllSpecialCharacters() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }

    @Test
    public void testFilter_AlphanumericInput() {
        assertEquals("abc123", HTMLFilter.filter("abc123"));
    }

    @Test
    public void testFilter_SpecialCharactersWithText() {
        assertEquals("Hello &lt; World", HTMLFilter.filter("Hello < World"));
    }

    @Test
    public void testFilter_LongInput() {
        // Edge case: Testing with a longer input string
        String longInput = "<1234567890>".repeat(10000); // 100,000 characters
        String expectedOutput = "&lt;1234567890&gt;".repeat(10000); // 100,000 characters
        assertEquals(expectedOutput, HTMLFilter.filter(longInput));
    }

    @Test
    public void testFilter_NullCharAtIndex() {
        // Edge case: Testing with a null char at a specific index
        String input = "Hello \0 World"; // null char at index 6
        String expectedOutput = "Hello \0 World";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    @Test(expected = NullPointerException.class)
    public void testFilter_NullCharInArray() {
        // Error scenario: Testing with a null char in the input array
        char[] inputArray = {'H', 'e', 'l', 'l', 'o', '\0', 'W', 'o', 'r', 'l', 'd'};
        HTMLFilter.filter(new String(inputArray));
    }
}