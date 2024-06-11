
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameString() {
        String input = "Hello World";
        assertEquals(input, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_AllSpecialCharacters_ReturnsFilteredString() {
        String input = "<>&\"";
        String expected = "&lt;&gt;&amp;&quot;";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_MixedSpecialCharacters_ReturnsFilteredString() {
        String input = "Testing<>&123";
        String expected = "Testing&lt;&gt;&amp;123";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_LongString_ReturnsFilteredString() {
        String input = "A" + "B".repeat(1000000) + "C";
        String expected = "A" + "&gt;".repeat(1000000) + "C";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_InvalidUnicodeCharacter_ReturnsFilteredStringWithoutError() {
        String input = "Hello\uD83D\uDCA9World";
        String expected = "Hello�World";
        assertEquals(expected, HTMLFilter.filter(input));
    }
}
