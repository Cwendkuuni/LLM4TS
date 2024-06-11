package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> collection1 = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo, bar, baz", StringUtil.join(collection1, ", "));
        
        Collection<String> collection2 = Arrays.asList("1", "2", "3");
        assertEquals("1_2_3", StringUtil.join(collection2, "_"));
        
        Collection<String> collection3 = Arrays.asList("");
        assertEquals("", StringUtil.join(collection3, "_"));
        
        Collection<String> collection4 = Arrays.asList();
        assertEquals("", StringUtil.join(collection4, "_"));
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> collection1 = Arrays.asList("foo", "bar", "baz");
        Iterator<String> iterator1 = collection1.iterator();
        assertEquals("foo, bar, baz", StringUtil.join(iterator1, ", "));
        
        Collection<String> collection2 = Arrays.asList("1", "2", "3");
        Iterator<String> iterator2 = collection2.iterator();
        assertEquals("1_2_3", StringUtil.join(iterator2, "_"));
        
        Collection<String> collection3 = Arrays.asList("");
        Iterator<String> iterator3 = collection3.iterator();
        assertEquals("", StringUtil.join(iterator3, "_"));
        
        Collection<String> collection4 = Arrays.asList();
        Iterator<String> iterator4 = collection4.iterator();
        assertEquals("", StringUtil.join(iterator4, "_"));
    }
    
    @Test
    public void testJoinArray() {
        String[] array1 = {"foo", "bar", "baz"};
        assertEquals("foo, bar, baz", StringUtil.join(array1, ", "));
        
        String[] array2 = {"1", "2", "3"};
        assertEquals("1_2_3", StringUtil.join(array2, "_"));
        
        String[] array3 = {""};
        assertEquals("", StringUtil.join(array3, "_"));
        
        String[] array4 = {};
        assertEquals("", StringUtil.join(array4, "_"));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("   ", StringUtil.padding(3));
        assertEquals("                    ", StringUtil.padding(20));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\r\n"));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("  foo  "));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("1a2b3c"));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('1'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('1'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals(" ", StringUtil.normaliseWhitespace("\r\n"));
        assertEquals(" ", StringUtil.normaliseWhitespace("\t"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo  bar\r\n  baz"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("  foo       bar   "));
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb1 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb1, "", false);
        assertEquals("", sb1.toString());
        
        StringBuilder sb2 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb2, "  foo  bar\r\n  baz  ", true);
        assertEquals("foo bar baz", sb2.toString());
        
        StringBuilder sb3 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb3, "  foo       bar   ", false);
        assertEquals("  foo       bar   ", sb3.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("bar", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("baz", "foo", "bar", "baz"));
        assertFalse(StringUtil.in("qux", "foo", "bar", "baz"));
    }
    
    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("bar", new String[]{"foo", "bar", "baz"}));
        assertFalse(StringUtil.inSorted("qux", new String[]{"foo", "bar", "baz"}));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base1 = new URL("https://example.com/");
        assertEquals("https://example.com/path?foo", StringUtil.resolve(base1, "/path?foo").toString());
        assertEquals("https://example.com/path/foo", StringUtil.resolve(base1, "./path/foo").toString());
        
        URL base2 = new URL("https://example.com/base");
        assertEquals("https://example.com/base", StringUtil.resolve(base2, "").toString());
        assertEquals("https://example.com/path?foo", StringUtil.resolve(base2, "/path?foo").toString());
    }
    
    @Test
    public void testResolveString() {
        assertEquals("", StringUtil.resolve("", ""));
        assertEquals("", StringUtil.resolve("", "path"));
        assertEquals("https://example.com", StringUtil.resolve("https://example.com", ""));
        assertEquals("https://example.com/path?foo", StringUtil.resolve("https://example.com", "/path?foo"));
    }
    
    // Additional test cases can be added here
    
}