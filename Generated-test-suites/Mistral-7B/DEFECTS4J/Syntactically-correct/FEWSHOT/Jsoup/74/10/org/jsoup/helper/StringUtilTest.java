package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class StringUtilTest {

    private static StringBuilder stringBuilder;

    @BeforeClass
    public static void setupClass() {
        stringBuilder = StringUtil.stringBuilder();
    }

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo, bar, baz", StringUtil.join(strings, ", "));
        List<String> emptyStrings = Collections.emptyList();
        assertEquals("", StringUtil.join(emptyStrings, ", "));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            assertEquals(padding[i], StringUtil.padding(i));
        }
        assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank(" \tfoo "));
        assertFalse(StringUtil.isBlank(" foo "));
        assertFalse(StringUtil.isBlank(" foo\n"));
        assertFalse(StringUtil.isBlank("foo\r"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("1a"));
        assertFalse(StringUtil.isNumeric("--1"));
        assertTrue(StringUtil.isNumeric("1"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("1234567890"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('z'));
        assertFalse(StringUtil.isWhitespace('!'));
        assertFalse(StringUtil.isWhitespace('?'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0')); // non-breaking space
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('z'));
        assertFalse(StringUtil.isActuallyWhitespace('!'));
        assertFalse(StringUtil.isActuallyWhitespace('?'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\t baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo \tbar \n baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo \r\n bar\t baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\r baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\t baz\r"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz"));
    }

    @Test
    public void testIn() {
        assertFalse(StringUtil.in("needle", new String[]{}));
        assertFalse(StringUtil.in("needle", new String[]{"foo"}));
        assertTrue(StringUtil.in("needle", new String[]{"needle", "foo", "needle"}));
        assertTrue(StringUtil.in("foo", new String[]{"needle", "foo", "needle"}));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("needle", new String[]{"needle", "foo", "needle"}));
        assertFalse(StringUtil.inSorted("foo", new String[]{"needle", "foo", "needle"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String base = "http://example.com";
        String rel = "/foo";
        URL expected = new URL("http://example.com/foo");
        URL result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar";
        rel = "foo.html";
        expected = new URL("http://example.com/bar/foo.html");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "../foo.html";
        expected = new URL("http://example.com/foo.html");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "../../foo.html";
        expected = new URL("http://example.com/foo.html");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "foo/";
        expected = new URL("http://example.com/bar/foo/");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "//example.com/foo";
        expected = new URL("http://example.com/foo");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "//example.com/foo/";
        expected = new URL("http://example.com/foo/");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "/";
        expected = new URL("http://example.com/");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "./";
        expected = new URL("http://example.com/bar/");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = ".";
        expected = new URL("http://example.com/bar/.");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "..";
        expected = new URL("http://example.com/");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "../..";
        expected = new URL("http://example.com/");
        result = StringUtil.resolve(new URL(base), rel);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        base = "http://example.com/bar/";
        rel = "...";
        assertNull(StringUtil.resolve(new URL(base), rel));

        base = null;
        rel = "http://example.com/foo";
        assertNull(StringUtil.resolve(base, rel));
    }

    @Test
    public void testResolveEmptyString() {
        String base = "";
        String rel = "foo";
        assertNull(StringUtil.resolve(base, rel));

        base = "http://example.com";
        rel = "";
        assertNull(StringUtil.resolve(new URL(base), rel));
    }

    @Test
    public void testResolveNullBase() {
        String rel = "foo";
        assertNull(StringUtil.resolve(null, rel));
    }

    @Test
    public void testResolveNullRel() {
        String base = "http://example.com";
        assertNull(StringUtil.resolve(new URL(base), null));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        append("foo", sb);
        append("bar", sb);
        assertEquals("foo, bar", sb.toString());
        sb = StringUtil.stringBuilder();
        append("foo", sb);
        assertEquals("foo", sb.toString());
    }

    private void append(String string, StringBuilder sb) {
        StringUtil.appendNormalisedWhitespace(sb, string, false);
    }
}