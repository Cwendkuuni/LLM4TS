package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings1 = new ArrayList<>();
        strings1.add("foo");
        strings1.add("bar");
        String result1 = StringUtil.join(strings1, "--");
        assertEquals("foo--bar", result1);

        List<String> strings2 = new ArrayList<>();
        strings2.add("apple");
        strings2.add("banana");
        String result2 = StringUtil.join(strings2, "");
        assertEquals("applebanana", result2);

        List<String> strings3 = new ArrayList<>();
        String result3 = StringUtil.join(strings3, "");
        assertNull(result3);

        List<String> strings4 = Arrays.asList("one", "two", "three");
        String result4 = StringUtil.join(strings4, "--");
        assertEquals("one--two--three", result4);

        List<String> strings5 = Collections.emptyList();
        String result5 = StringUtil.join(strings5, "--");
        assertEquals("", result5);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < StringUtil.padding.length; i++) {
            assertEquals(StringUtil.padding[i], StringUtil.padding(i));
        }

        try {
            StringUtil.padding(-1);
            fail("ArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertTrue(StringUtil.isBlank(" \r\n\t\f"));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank(" foo"));
        assertFalse(StringUtil.isBlank("123"));
        assertFalse(StringUtil.isBlank("123 "));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("abc"));
        assertFalse(StringUtil.isNumeric("123 "));
        assertFalse(StringUtil.isNumeric("123a"));
        assertFalse(StringUtil.isNumeric("123.45"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234"));
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsWhitespaceAndIsActuallyWhitespace() {
        assertFalse(StringUtil.isWhitespace(0x00A0));
        assertFalse(StringUtil.isWhitespace(0x00AD));

        for (int i = 0x20; i <= 0x21; i++) {
            assertTrue(StringUtil.isWhitespace((char) i));
            assertTrue(StringUtil.isActuallyWhitespace((char) i));
        }

        for (int i = 0x0020; i <= 0x007E; i++) {
            if (i == 0x0022 || i == 0x0027) {
                continue;
            }
            assertFalse(StringUtil.isActuallyWhitespace((char) i));
            assertFalse(StringUtil.isWhitespace((char) i));
        }

        assertFalse(StringUtil.isWhitespace(0x007F));
        assertFalse(StringUtil.isWhitespace(0x00FF));
        assertFalse(StringUtil.isWhitespace(0x200B));
        assertFalse(StringUtil.isWhitespace(0x200C));
        assertFalse(StringUtil.isWhitespace(0x2028));
        assertFalse(StringUtil.isWhitespace(0x2029));
        assertFalse(StringUtil.isWhitespace(0x205F));
        assertFalse(StringUtil.isWhitespace(0x2060));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(null));
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("abc", StringUtil.normaliseWhitespace("abc"));
        assertEquals(" a b c", StringUtil.normaliseWhitespace(" a b c"));
        assertEquals(" a\nb c", StringUtil.normaliseWhitespace(" a\nb c"));
        assertEquals("a b c ", StringUtil.normaliseWhitespace("a b c "));
        assertEquals(" a b c d", StringUtil.normaliseWhitespace("a b c \ta d"));
        assertEquals("a\nb\tc", StringUtil.normaliseWhitespace("a\nb\tc"));
        assertEquals("", StringUtil.normaliseWhitespace(" \t\r\n"));
        assertEquals("one two three", StringUtil.normaliseWhitespace("one\ttwo three"));
        assertEquals("one two three", StringUtil.normaliseWhitespace("one\ttwo\n three"));
        assertEquals("one\ttwo three", StringUtil.normaliseWhitespace("one\ttwo three"));
    }

    @Test
    public void testIn() {
        assertFalse(StringUtil.in("needle", new String[0]));
        assertFalse(StringUtil.in("needle", new String[]{"apple"}));
        assertFalse(StringUtil.in("needle", new String[]{"apple", "banana"}));
        assertTrue(StringUtil.in("needle", new String[]{"needle", "apple", "banana"}));
        assertTrue(StringUtil.in("needle", new String[]{"needle"}));
        assertTrue(StringUtil.in("needle", new String[]{"needle", "needle"}));
    }

    @Test
    public void testInSorted() {
        List<String> haystack = Arrays.asList("apple", "banana", "needle");
        assertFalse(StringUtil.inSorted("needle", haystack.toArray(new String[0])));
        assertTrue(StringUtil.inSorted("needle", new String[]{"needle"}));
        assertTrue(StringUtil.inSorted("needle", haystack.toArray(new String[0])));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/relative/path.html", result);

        String baseUrl2 = "http://example.com/path/to/file.html";
        String relUrl2 = "relative/path.html";
        String result2 = StringUtil.resolve(baseUrl2, relUrl2);
        assertEquals("http://example.com/relative/path.html", result2);

        String baseUrl3 = "http://example.com/path/to/file.html";
        String relUrl3 = "./relative/path.html";
        String result3 = StringUtil.resolve(baseUrl3, relUrl3);
        assertEquals("http://example.com/relative/path.html", result3);

        String baseUrl4 = "http://example.com/path/to/file.html";
        String relUrl4 = "../relative/path.html";
        String result4 = StringUtil.resolve(baseUrl4, relUrl4);
        assertEquals("http://example.com/../relative/path.html", result4);

        String baseUrl5 = "http://example.com/path/to/file.html";
        String relUrl5 = "../../relative/path.html";
        String result5 = StringUtil.resolve(baseUrl5, relUrl5);
        assertEquals("http://example.com/../../relative/path.html", result5);

        String baseUrl6 = "http://example.com/path/to/file.html";
        String relUrl6 = "//example.com/relative/path.html";
        String result6 = StringUtil.resolve(baseUrl6, relUrl6);
        assertEquals("http://example.com/relative/path.html", result6);

        String baseUrl7 = "http://example.com/path/to/file.html";
        String relUrl7 = "//another.com/relative/path.html";
        String result7 = StringUtil.resolve(baseUrl7, relUrl7);
        assertEquals("", result7);

        String baseUrl8 = "http://example.com/path/to/file.html";
        String relUrl8 = "/";
        String result8 = StringUtil.resolve(baseUrl8, relUrl8);
        assertEquals("http://example.com/", result8);
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        String baseUrl = "http://example.com:8080/path/to/file.html";
        String relUrl = "relative.html";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals("http://example.com:8080/relative.html", result.toExternalForm());

        String baseUrl2 = "http://example.com:8080/path/to/file.html";
        String relUrl2 = "relative.html";
        URL result2 = StringUtil.resolve(baseUrl2.toExternalForm(), relUrl2);
        assertEquals("http://example.com:8080/relative.html", result2.toExternalForm());
    }
}