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
        Collection<String> strings = Arrays.asList("Hello", "World");
        String joinedString = StringUtil.join(strings, " ");
        assertEquals("Hello World", joinedString);
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String joinedString = StringUtil.join(strings, " ");
        assertEquals("Hello World", joinedString);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String joinedString = StringUtil.join(strings, " ");
        assertEquals("Hello World", joinedString);
    }
    
    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("  "));
        assertFalse(StringUtil.isBlank("Hello"));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("Hello"));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('A'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("  Hello World  ");
        assertEquals("Hello World", normalized);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  Hello World  ", true);
        assertEquals("Hello World", sb.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World"));
        assertFalse(StringUtil.in("Foo", "Hello", "World"));
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"Apple", "Banana", "Orange"};
        assertTrue(StringUtil.inSorted("Apple", haystack));
        assertFalse(StringUtil.inSorted("Grape", haystack));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String resolvedUrl = StringUtil.resolve(base, "/path");
        assertEquals("https://www.example.com/path", resolvedUrl);
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String resolvedUrl = StringUtil.resolve(baseUrl, "/path");
        assertEquals("https://www.example.com/path", resolvedUrl);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }
}