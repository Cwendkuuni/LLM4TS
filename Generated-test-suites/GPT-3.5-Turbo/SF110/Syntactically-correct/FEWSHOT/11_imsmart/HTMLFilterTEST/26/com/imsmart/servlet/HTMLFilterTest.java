package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilterNull() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilterEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilterNoSpecialCharacters() {
        assertEquals("abc", HTMLFilter.filter("abc"));
    }

    @Test
    public void testFilterWithLessThanCharacter() {
        assertEquals("&lt;abc", HTMLFilter.filter("<abc"));
    }

    @Test
    public void testFilterWithGreaterThanCharacter() {
        assertEquals("abc&gt;", HTMLFilter.filter("abc>"));
    }

    @Test
    public void testFilterWithAmpersandCharacter() {
        assertEquals("abc&amp;def", HTMLFilter.filter("abc&def"));
    }

    @Test
    public void testFilterWithQuoteCharacter() {
        assertEquals("abc&quot;def", HTMLFilter.filter("abc\"def"));
    }

    @Test
    public void testFilterWithMultipleSpecialCharacters() {
        assertEquals("&lt;abc&gt;&amp;&quot;", HTMLFilter.filter("<abc>&\""));
    }

    @Test
    public void testFilterWithUnicodeCharacter() {
        assertEquals("&#1234;", HTMLFilter.filter("ሴ"));
    }

}