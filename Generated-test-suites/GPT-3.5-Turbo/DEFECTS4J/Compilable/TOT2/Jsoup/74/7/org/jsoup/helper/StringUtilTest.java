package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("hello", "world");
        String joined = StringUtil.join(strings, " ");
        assertEquals("hello world", joined);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("hello", "world").iterator();
        String joined = StringUtil.join(strings, " ");
        assertEquals("hello world", joined);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world"};
        String joined = StringUtil.join(strings, " ");
        assertEquals("hello world", joined);
    }

    @Test
    public void testPadding() {
        String padded = StringUtil.padding(5);
        assertEquals("     ", padded);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("  "));
        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("abc"));
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("hello  world");
        assertEquals("hello world", normalized);
    }

    @Test
    public void testIn() {
        String[] haystack = {"hello", "world"};
        assertTrue(StringUtil.in("hello", haystack));
        assertFalse(StringUtil.in("foo", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        assertTrue(StringUtil.inSorted("banana", haystack));
        assertFalse(StringUtil.inSorted("date", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "test.html";
        URL resolved = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/test.html", resolved.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "../test.html";
        String resolved = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/../test.html", resolved);
    }

}