package org.jsoup.helper;
import static org.junit.Assert.*;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("hello", "world");
        String result = StringUtil.join(strings, ",");
        assertEquals("hello,world", result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("hello", "world").iterator();
        String result = StringUtil.join(strings, ",");
        assertEquals("hello,world", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world"};
        String result = StringUtil.join(strings, ",");
        assertEquals("hello,world", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(4);
        assertEquals("    ", result);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("hello"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('A'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("  hello   world  ");
        assertEquals(" hello world ", result);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("hello", "hello", "world"));
        assertFalse(StringUtil.in("test", "hello", "world"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "orange"};
        assertTrue(StringUtil.inSorted("apple", haystack));
        assertFalse(StringUtil.inSorted("grape", haystack));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/path/";
        String relUrl = "./file.html";
        StringUtil.resolve(baseUrl, relUrl);
    }

}