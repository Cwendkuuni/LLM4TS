package org.jsoup.helper;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinWithCollectionAndSep() {
        Collection<String> collection = Arrays.asList("one", "two", "three");
        String sep = ",";
        String expected = "one,two,three";
        String result = StringUtil.join(collection, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithIteratorAndSep() {
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
        String sep = ",";
        String expected = "one,two,three";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithArrayAndSep() {
        String[] array = {"one", "two", "three"};
        String sep = ",";
        String expected = "one,two,three";
        String result = StringUtil.join(array, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithBlankString() {
        String string = "";
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }
    
    @Test
    public void testIsBlankWithNullString() {
        String string = null;
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }
    
    @Test
    public void testIsBlankWithNonBlankString() {
        String string = "test";
        boolean result = StringUtil.isBlank(string);
        assertFalse(result);
    }
    
    @Test
    public void testIsNumericWithNumericString() {
        String string = "12345";
        boolean result = StringUtil.isNumeric(string);
        assertTrue(result);
    }
    
    @Test
    public void testIsNumericWithNonNumericString() {
        String string = "test";
        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }
    
    @Test
    public void testIsNumericWithEmptyString() {
        String string = "";
        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceCodePoint() {
        int c = ' ';
        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }
    
    @Test
    public void testIsWhitespaceWithNonWhitespaceCodePoint() {
        int c = 'a';
        boolean result = StringUtil.isWhitespace(c);
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithWhitespaceCodePoint() {
        int c = ' ';
        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }
    
    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceCodePoint() {
        int c = 'a';
        boolean result = StringUtil.isActuallyWhitespace(c);
        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   test   string  ";
        String expected = " test string ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithStripLeadingTrue() {
        StringBuilder sb = new StringBuilder();
        String string = "   test   string  ";
        String expected = "test string ";
        StringUtil.appendNormalisedWhitespace(sb, string, true);
        assertEquals(expected, sb.toString());
    }
    
    @Test
    public void testAppendNormalisedWhitespaceWithStripLeadingFalse() {
        StringBuilder sb = new StringBuilder();
        String string = "   test   string  ";
        String expected = " test string ";
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        assertEquals(expected, sb.toString());
    }
    
    @Test
    public void testInWithMatch() {
        String needle = "one";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }
    
    @Test
    public void testInWithNoMatch() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }
    
    @Test
    public void testInSortedWithMatch() {
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }
    
    @Test
    public void testInSortedWithNoMatch() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testResolveWithValidURLs() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "subpage";
        URL expected = new URL("http://www.example.com/subpage");
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithInvalidURLs() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "http://www.example2.com";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithValidBaseAndAbsoluteRelUrl() {
        String baseUrl = "http://www.example.com";
        String relUrl = "http://www.example2.com";
        String expected = "http://www.example2.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithInvalidBaseAndInvalidRelUrl() {
        String baseUrl = "example";
        String relUrl = "example2";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertSame(sb1, sb2);
    }
}