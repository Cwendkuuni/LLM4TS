package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("apple", "banana", "cherry");
        String sep = ",";
        String expected = "apple,banana,cherry";
        String result = StringUtil.join(strings, sep);
        assertEquals(expected, result);
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("apple", "banana", "cherry").iterator();
        String sep = ",";
        String expected = "apple,banana,cherry";
        String result = StringUtil.join(strings, sep);
        assertEquals(expected, result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};
        String sep = ",";
        String expected = "apple,banana,cherry";
        String result = StringUtil.join(strings, sep);
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
    public void testIsBlank() {
        String string1 = null;
        String string2 = "";
        String string3 = " ";
        String string4 = "abc";
        
        assertTrue(StringUtil.isBlank(string1));
        assertTrue(StringUtil.isBlank(string2));
        assertTrue(StringUtil.isBlank(string3));
        assertFalse(StringUtil.isBlank(string4));
    }
    
    @Test
    public void testIsNumeric() {
        String string1 = null;
        String string2 = "";
        String string3 = "123";
        String string4 = "abc123";
        
        assertFalse(StringUtil.isNumeric(string1));
        assertFalse(StringUtil.isNumeric(string2));
        assertTrue(StringUtil.isNumeric(string3));
        assertFalse(StringUtil.isNumeric(string4));
    }
    
    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        
        assertTrue(StringUtil.isWhitespace(c1));
        assertTrue(StringUtil.isWhitespace(c2));
        assertTrue(StringUtil.isWhitespace(c3));
        assertTrue(StringUtil.isWhitespace(c4));
        assertTrue(StringUtil.isWhitespace(c5));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        int c6 = 160;
        
        assertTrue(StringUtil.isActuallyWhitespace(c1));
        assertTrue(StringUtil.isActuallyWhitespace(c2));
        assertTrue(StringUtil.isActuallyWhitespace(c3));
        assertTrue(StringUtil.isActuallyWhitespace(c4));
        assertTrue(StringUtil.isActuallyWhitespace(c5));
        assertTrue(StringUtil.isActuallyWhitespace(c6));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String string1 = "  This   is    a  test   ";
        String expected = " This is a test ";
        String result = StringUtil.normaliseWhitespace(string1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        String string1 = "  This   is    a  test   ";
        StringBuilder accum = new StringBuilder();
        String expected = " This is a test ";
        StringUtil.appendNormalisedWhitespace(accum, string1, true);
        String result = accum.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testIn() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "cherry"};
        
        assertTrue(StringUtil.in(needle, haystack));
    }
    
    @Test
    public void testInSorted() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "cherry"};
        
        assertTrue(StringUtil.inSorted(needle, haystack));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "/path";
        
        URL expected = new URL("https://www.example.com/path");
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "/path";
        
        String expected = "https://www.example.com/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }
    
    // Additional unit tests for StringUtil class can be added here
    
}