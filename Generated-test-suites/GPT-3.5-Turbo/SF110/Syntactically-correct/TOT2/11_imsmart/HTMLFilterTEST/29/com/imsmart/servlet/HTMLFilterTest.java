
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

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
        assertEquals("Hello, world!", HTMLFilter.filter("Hello, world!"));
    }

    @Test
    public void testFilter_LesserThanSign() {
        assertEquals("&lt;script&gt;", HTMLFilter.filter("<script>"));
    }

    @Test
    public void testFilter_GreaterThanSign() {
        assertEquals("&gt;body&lt;", HTMLFilter.filter(">body<"));
    }

    @Test
    public void testFilter_AmpersandSign() {
        assertEquals("Awesome &amp; amazing", HTMLFilter.filter("Awesome & amazing"));
    }

    @Test
    public void testFilter_DoubleQuote() {
        assertEquals("She said &quot;hello&quot;", HTMLFilter.filter("She said \"hello\""));
    }

    @Test
    public void testFilter_AllSpecialCharacters() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }
}

