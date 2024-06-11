package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // Typical use case: join a collection with a separator
        Collection<String> strings = Arrays.asList("one", "two", "three");
        String sep = ",";
        String expected = "one,two,three";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        // Typical use case: join an iterator with a separator
        Collection<String> strings = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = strings.iterator();
        String sep = ",";
        String expected = "one,two,three";
        String result = StringUtil.join(iterator, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        // Typical use case: join an array with a separator
        String[] strings = { "one", "two", "three" };
        String sep = ",";
        String expected = "one,two,three";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        // Typical use case: create padding of a specific width
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testIsBlank() {
        // Typical use case: string is null
        String string = null;
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
        
        // Typical use case: string is empty
        string = "";
        expected = true;
        result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
        
        // Typical use case: string contains only whitespace characters
        string = "   ";
        expected = true;
        result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
        
        // Typical use case: string is not blank
        string = "not blank";
        expected = false;
        result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testIsNumeric() {
        // Typical use case: string is null
        String string = null;
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
        
        // Typical use case: string is empty
        string = "";
        expected = false;
        result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
        
        // Typical use case: string contains only digit characters
        string = "123456789";
        expected = true;
        result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
        
        // Typical use case: string contains non-digit characters
        string = "1234abc";
        expected = false;
        result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testIsWhitespace() {
        // Typical use case: code point is a whitespace character
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, result);
        
        // Typical use case: code point is not a whitespace character
        c = 'a';
        expected = false;
        result = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        // Typical use case: code point is a whitespace character
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, result);
        
        // Typical use case: code point is not a whitespace character
        c = 'a';
        expected = false;
        result = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        // Typical use case: normalize whitespace within a string
        String string = "The  quick\r\n\tbrown   fox";
        String expected = "The quick brown fox";
        String result = StringUtil.normaliseWhitespace(string);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        // Typical use case: append normalized whitespace to a string builder
        StringBuilder accum = new StringBuilder();
        String string = "The  quick\r\n\tbrown   fox";
        boolean stripLeading = false;
        String expected = "The quick brown fox";
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        String result = accum.toString();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testIn() {
        // Typical use case: needle is in the haystack
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
        
        // Typical use case: needle is not in the haystack
        needle = "four";
        expected = false;
        result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testInSorted() {
        // Typical use case: needle is in the sorted haystack
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
        
        // Typical use case: needle is not in the sorted haystack
        needle = "four";
        expected = false;
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testResolve() {
        // Typical use case: resolve a relative URL with an existing absolute URL
        String baseUrl = "http://www.example.com";
        String relUrl = "images/image.jpg";
        String expected = "http://www.example.com/images/image.jpg";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testResolveMalformedUrl() {
        // Error scenario: base URL is malformed
        String baseUrl = "invalid url";
        String relUrl = "images/image.jpg";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        // Typical use case: obtain a new StringBuilder
        StringBuilder sb = StringUtil.stringBuilder();
        String expected = "";
        String result = sb.toString();
        Assert.assertEquals(expected, result);
    }
    
}
