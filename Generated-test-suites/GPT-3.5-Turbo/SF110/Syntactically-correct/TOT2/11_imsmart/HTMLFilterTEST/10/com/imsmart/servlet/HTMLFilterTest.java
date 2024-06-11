package com.imsmart.servlet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_AllSpecialCharacters() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }
    
    @Test
    public void testFilter_NullInput() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_EmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_NoSpecialCharacters() {
        assertEquals("abcdefg123", HTMLFilter.filter("abcdefg123"));
    }
    
    @Test
    public void testFilter_OnlySpecialCharacters() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }
    
    @Test
    public void testFilter_LargeInput() {
        String input = "<>&\"".repeat(1000);
        String expectedOutput = "&lt;&gt;&amp;&quot;".repeat(1000);
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
}