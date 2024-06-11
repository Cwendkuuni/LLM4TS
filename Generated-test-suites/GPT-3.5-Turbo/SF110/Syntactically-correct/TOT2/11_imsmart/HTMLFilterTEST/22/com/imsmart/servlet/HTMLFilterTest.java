
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyString_ReturnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnSameString() {
        assertEquals("hello", HTMLFilter.filter("hello"));
    }

    @Test
    public void testFilter_WithSpecialCharacters_ReturnFilteredString() {
        assertEquals("&lt;&gt;&amp;&quot;&#39;", HTMLFilter.filter("<>&\"'"));
    }

    @Test
    public void testFilter_LongMessage_ReturnFilteredString() {
        assertEquals("&lt;&gt;&amp;&quot;&#39;hello&lt;&gt;&amp;&quot;&#39;", HTMLFilter.filter("<>&\"'hello<>&\"'"));
    }

    @Test
    public void testFilter_UnicodeCharacters_ReturnFilteredString() {
        assertEquals("&#128514;&#128516;", HTMLFilter.filter("\uD83D\uDE02\uD83D\uDE04"));
    }
}
