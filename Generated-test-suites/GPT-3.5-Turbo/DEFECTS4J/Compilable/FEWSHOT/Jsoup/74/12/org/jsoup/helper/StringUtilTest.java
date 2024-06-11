
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> collection = Arrays.asList("a", "b", "c");
        String sep = "-";
        String expectedResult = "a-b-c";
        String actualResult = StringUtil.join(collection, sep);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = collection.iterator();
        String sep = "-";
        String expectedResult = "a-b-c";
        String actualResult = StringUtil.join(iterator, sep);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testJoinArray() {
        String[] array = {"a", "b", "c"};
        String sep = "-";
        String expectedResult = "a-b-c";
        String actualResult = StringUtil.join(array, sep);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        String expectedResult = "     ";
        String actualResult = StringUtil.padding(width);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsBlank() {
        String blankString = "";
        String nonBlankString = "not blank";
        assertTrue(StringUtil.isBlank(blankString));
        assertFalse(StringUtil.isBlank(nonBlankString));
    }
    
    @Test
    public void testIsNumeric() {
        String numericString = "12345";
        String nonNumericString = "abc123";
        assertTrue(StringUtil.isNumeric(numericString));
        assertFalse(StringUtil.isNumeric(nonNumericString));
    }
    
    @Test
    public void testIsWhitespace() {
        int whitespaceCodePoint = ' ';
        int nonWhitespaceCodePoint = 'a';
        assertTrue(StringUtil.isWhitespace(whitespaceCodePoint));
        assertFalse(StringUtil.isWhitespace(nonWhitespaceCodePoint));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        int whitespaceCodePoint = ' ';
        int nonWhitespaceCodePoint = 'a';
        assertTrue(StringUtil.isActuallyWhitespace(whitespaceCodePoint));
        assertFalse(StringUtil.isActuallyWhitespace(nonWhitespaceCodePoint));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String string = "  this is a   test   string  ";
        String expectedResult = "this is a test string";
        String actualResult = StringUtil.normaliseWhitespace(string);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        String string = "  this is a   test   string  ";
        boolean stripLeading = true;
        String expectedResult = "this is a test string";
        StringUtil.appendNormalisedWhitespace(sb, string, stripLeading);
        assertEquals(expectedResult, sb.toString());
    }
    
    @Test
    public void testIn() {
        String needle = "needle";
        String[] haystack = {"hay", "needle", "stack"};
        assertTrue(StringUtil.in(needle, haystack));
        assertFalse(StringUtil.in("another needle", haystack));
    }
    
    @Test
    public void testInSorted() {
        String needle = "needle";
        String[] haystack = {"apple", "banana", "carrot", "needle", "orange"};
        assertTrue(StringUtil.inSorted(needle, haystack));
        assertFalse(StringUtil.inSorted("pear", haystack));
    }
    
    @Test
    public void testResolveURL() throws MalformedURLException {
        String baseUrl = "http://www.example.com";
        String relUrl = "path/to/resource";
        String expectedResult = "http://www.example.com/path/to/resource";
        URL base = new URL(baseUrl);
        URL actualResult = StringUtil.resolve(base, relUrl);
        assertEquals(expectedResult, actualResult.toString());
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "path/to/resource";
        String expectedResult = "http://www.example.com/path/to/resource";
        String actualResult = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertTrue(sb.length() == 0);
    }
}
