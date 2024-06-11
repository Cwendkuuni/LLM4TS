package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollectionString() {
        Collection<String> collection1 = Arrays.asList("a", "b", "c");
        assertEquals("a,b,c", StringUtil.join(collection1, ","));
        
        Collection<String> collection2 = Arrays.asList("hello", "world");
        assertEquals("hello world", StringUtil.join(collection2, " "));
        
        Collection<String> collection3 = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo-bar-baz", StringUtil.join(collection3, "-"));
    }

    @Test
    public void testJoinIteratorString() {
        Iterator<String> iterator1 = Arrays.asList("1", "2", "3").iterator();
        assertEquals("1,2,3", StringUtil.join(iterator1, ","));
        
        Iterator<String> iterator2 = Arrays.asList("hello", "world").iterator();
        assertEquals("hello world", StringUtil.join(iterator2, " "));
        
        Iterator<String> iterator3 = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo-bar-baz", StringUtil.join(iterator3, "-"));
    }

    @Test
    public void testJoinStringArrayString() {
        String[] array1 = {"1", "2", "3"};
        assertEquals("1,2,3", StringUtil.join(array1, ","));
        
        String[] array2 = {"hello", "world"};
        assertEquals("hello world", StringUtil.join(array2, " "));
        
        String[] array3 = {"foo", "bar", "baz"};
        assertEquals("foo-bar-baz", StringUtil.join(array3, "-"));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(2));
        assertEquals("   ", StringUtil.padding(3));
        assertEquals("    ", StringUtil.padding(4));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\r\n"));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("12a3"));
        assertFalse(StringUtil.isNumeric("abc"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
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
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("hello world", StringUtil.normaliseWhitespace("hello     world"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n\nbar\t\t\tbaz"));
        assertEquals("1 2 3 4", StringUtil.normaliseWhitespace("1 \r\n2 \t3 \n4"));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb1 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb1, "hello     world", false);
        assertEquals("hello world", sb1.toString());
        
        StringBuilder sb2 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb2, "foo\n\nbar\t\t\tbaz", false);
        assertEquals("foo bar baz", sb2.toString());
        
        StringBuilder sb3 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb3, "1 \r\n2 \t3 \n4", false);
        assertEquals("1 2 3 4", sb3.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("hello", "hello", "world"));
        assertTrue(StringUtil.in("world", "hello", "world"));
        assertFalse(StringUtil.in("foo", "hello", "world"));
    }

    @Test
    public void testInSorted() {
        String[] sortedArray1 = {"apple", "banana", "cherry", "grape", "orange"};
        assertTrue(StringUtil.inSorted("apple", sortedArray1));
        assertTrue(StringUtil.inSorted("banana", sortedArray1));
        assertFalse(StringUtil.inSorted("carrot", sortedArray1));
        
        String[] sortedArray2 = {"a", "b", "c", "d", "e"};
        assertTrue(StringUtil.inSorted("a", sortedArray2));
        assertTrue(StringUtil.inSorted("b", sortedArray2));
        assertFalse(StringUtil.inSorted("f", sortedArray2));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "foo";
        URL resolvedUrl = new URL("http://www.example.com/foo");
        assertEquals(resolvedUrl, StringUtil.resolve(base, relUrl));
        
        URL base2 = new URL("http://www.example.com/path/");
        String relUrl2 = "foo";
        URL resolvedUrl2 = new URL("http://www.example.com/path/foo");
        assertEquals(resolvedUrl2, StringUtil.resolve(base2, relUrl2));
    }

    @Test
    public void testResolve2() {
        String baseUrl = "http://www.example.com";
        String relUrl = "foo";
        String resolvedUrl = "http://www.example.com/foo";
        assertEquals(resolvedUrl, StringUtil.resolve(baseUrl, relUrl));
        
        String baseUrl2 = "http://www.example.com/path/";
        String relUrl2 = "foo";
        String resolvedUrl2 = "http://www.example.com/path/foo";
        assertEquals(resolvedUrl2, StringUtil.resolve(baseUrl2, relUrl2));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        
        assertNotEquals(sb1, sb2);
        assertEquals(0, sb1.length());
        assertEquals(0, sb2.length());
    }

}