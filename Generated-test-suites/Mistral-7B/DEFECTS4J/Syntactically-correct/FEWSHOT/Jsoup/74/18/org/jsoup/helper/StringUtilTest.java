package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoin() {
        Collection strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));

        Iterator iterator = strings.iterator();
        String first = iterator.next();
        assertEquals(first, StringUtil.join(iterator, " ").substring(0, first.length()));
        assertEquals("bar baz", StringUtil.join(iterator, " ").substring(first.length()));

        String[] stringArray = new String[] {"foo", "bar", "baz"};
        assertEquals("foo,bar,baz", StringUtil.join(stringArray, ","));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 22; i++) {
            assertEquals(padding[i], StringUtil.padding(i));
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\r"));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank(" foo "));
        assertFalse(StringUtil.isBlank("123"));
        assertFalse(StringUtil.isBlank("1234"));
        assertFalse(StringUtil.isBlank("12345"));
        assertFalse(StringUtil.isBlank("123456"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("123foo"));
        assertFalse(StringUtil.isNumeric("foo123"));
        assertFalse(StringUtil.isNumeric("1a2b3c"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("123456"));
        assertTrue(StringUtil.isNumeric("1234567"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('\b'));
        assertFalse(StringUtil.isWhitespace('\f'));
        assertFalse(StringUtil.isWhitespace('.'));
        assertFalse(StringUtil.isWhitespace('.'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('\b'));
        assertFalse(StringUtil.isActuallyWhitespace('\f'));
        assertFalse(StringUtil.isActuallyWhitespace('.'));
        assertFalse(StringUtil.isActuallyWhitespace('.'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo  bar   baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\t baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\nbartabaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\tbartabaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n\tbartabaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\r\nbartabaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\r\tbar tabaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\r\t bar  tabaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\r\n bar\t tabaz"));

        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("", StringUtil.normaliseWhitespace(" "));
        assertEquals("", StringUtil.normaliseWhitespace("\n"));
        assertEquals("", StringUtil.normaliseWhitespace("\r"));
        assertEquals("", StringUtil.normaliseWhitespace("\t"));
    }

    @Test
    public void testIn() {
        String[] haystack = {"apple", "banana", "cherry", "date"};
        assertTrue(StringUtil.in("apple", haystack));
        assertTrue(StringUtil.in("banana", haystack));
        assertTrue(StringUtil.in("cherry", haystack));
        assertTrue(StringUtil.in("date", haystack));
        assertFalse(StringUtil.in("pear", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry", "date"};
        assertTrue(StringUtil.inSorted("apple", haystack));
        assertTrue(StringUtil.inSorted("banana", haystack));
        assertTrue(StringUtil.inSorted("cherry", haystack));
        assertTrue(StringUtil.inSorted("date", haystack));
        assertFalse(StringUtil.inSorted("pear", haystack));

        String[] sortedHaystack = {"apple", "cherry", "date", "banana"};
        assertTrue(StringUtil.inSorted("apple", sortedHaystack));
        assertTrue(StringUtil.inSorted("cherry", sortedHaystack));
        assertTrue(StringUtil.inSorted("date", sortedHaystack));
        assertTrue(StringUtil.inSorted("banana", sortedHaystack));
        assertFalse(StringUtil.inSorted("pear", sortedHaystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "relative.html";
        URL expected = new URL("http://example.com/path/to/file.html/relative.html");

        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "/folder/file.html";
        expected = new URL("http://example.com/path/to/file.html/folder/file.html");

        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../folder/file.html";
        expected = new URL("http://example.com/path/to/folder/file.html");

        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../../folder/file.html";
        expected = new URL("http://example.com/folder/file.html");

        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "./file.html";
        assertEquals(baseUrl, StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "file.html";
        assertEquals(baseUrl, StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "";
        assertEquals(baseUrl, StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = null;
        assertNull(StringUtil.resolve(new URL(baseUrl), relUrl));
    }

    @Test
    public void testResolveWithEmptyBaseUrl() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "relative.html";

        assertNull(StringUtil.resolve(null, relUrl));
        assertNull(StringUtil.resolve(new URL(baseUrl), relUrl));
    }

    @Test
    public void testResolveWithAbsoluteRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "http://example.org/path/to/file.html";

        assertEquals(relUrl, StringUtil.resolve(new URL(baseUrl), relUrl));
    }
}