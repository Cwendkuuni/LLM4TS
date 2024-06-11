package org.jsoup.helper;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

import static org.junit.Assert.*;
import org.jsoup.helper.StringUtil;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        String result = StringUtil.join(strings, ", ");
        assertEquals("Hello, World, !", result);
    }
    
    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, ", ");
        assertEquals("Hello, World, !", result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String result = StringUtil.join(strings, ", ");
        assertEquals("Hello, World, !", result);
    }
    
    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12A45"));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('A'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello    World");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "Hello    World", false);
        assertEquals("Hello World", sb.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World", "!"));
        assertFalse(StringUtil.in("Hi", "Hello", "World", "!"));
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"!", "Hello", "World"};
        assertTrue(StringUtil.inSorted("Hello", haystack));
        assertFalse(StringUtil.inSorted("Hi", haystack));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        String result = StringUtil.resolve("https://www.example.com", "/path");
        assertEquals("https://www.example.com/path", result);
    }
    
    @Test
    public void testResolveMalformed() {
        String result = StringUtil.resolve("invalid_url", "/path");
        assertEquals("", result);
    }

}