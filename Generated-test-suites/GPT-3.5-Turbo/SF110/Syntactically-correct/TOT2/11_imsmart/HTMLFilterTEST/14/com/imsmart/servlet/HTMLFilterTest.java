package com.imsmart.servlet;
import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals("hello world", HTMLFilter.filter("hello world"));
    }

    @Test
    public void testFilter_SpecialCharacters() {
        assertEquals("&lt;hello &amp; world&gt;", HTMLFilter.filter("<hello & world>"));
    }

    @Test
    public void testFilter_SpecialCharactersAndQuotes() {
        assertEquals("&lt;quote &quot;hello&quot;&gt;", HTMLFilter.filter("<quote \"hello\">"));
    }

    @Test
    public void testFilter_LongMessage() {
        String longMessage = "&lt;This is a long &amp; special message&gt;";
        assertEquals("&lt;This is a long &amp; special message&gt;", HTMLFilter.filter(longMessage));
    }

}