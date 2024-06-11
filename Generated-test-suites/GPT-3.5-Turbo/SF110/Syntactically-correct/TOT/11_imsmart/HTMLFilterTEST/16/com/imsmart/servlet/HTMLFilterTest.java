package com.imsmart.servlet.test;

import com.imsmart.servlet.HTMLFilter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    private HTMLFilter htmlFilter;

    @Before
    public void setUp() {
        htmlFilter = new HTMLFilter();
    }

    @Test
    public void testFilter_WithNullInput_ReturnsNull() {
        assertNull(htmlFilter.filter(null));
    }

    @Test
    public void testFilter_WithEmptyString_ReturnsEmptyString() {
        assertEquals("", htmlFilter.filter(""));
    }

    @Test
    public void testFilter_WithValidString_DoesNotModifyString() {
        String input = "This is a valid string";
        assertEquals(input, htmlFilter.filter(input));
    }

    @Test
    public void testFilter_WithHTMLTags_ReplacesTagsWithHTMLentities() {
        String input = "<h1>Welcome</h1>";
        assertEquals("&lt;h1&gt;Welcome&lt;/h1&gt;", htmlFilter.filter(input));
    }

    @Test
    public void testFilter_WithSpecialCharacters_ReplacesWithHTMLentities() {
        String input = "This is a \"quoted\" word";
        assertEquals("This is a &quot;quoted&quot; word", htmlFilter.filter(input));
    }

    @Test
    public void testFilter_WithMultipleSpecialCharacters_ReplacesWithHTMLentities() {
        String input = "<hello> & welcome!";
        assertEquals("&lt;hello&gt; &amp; welcome!", htmlFilter.filter(input));
    }

    @Test
    public void testFilter_WithMixedInput_ReplacesSpecialCharactersAndHTMLTags() {
        String input = "<b>This is</b> an &lt;example&gt;.";
        assertEquals("&lt;b&gt;This is&lt;/b&gt; an &amp;lt;example&amp;gt;.", htmlFilter.filter(input));
    }
}