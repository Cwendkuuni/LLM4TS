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
    public void testJoin(Collection strings, String sep) {
        assertEquals("", StringUtil.join(Arrays.asList(), ""));
        assertEquals("foo", StringUtil.join(Arrays.asList("foo"), ""));
        assertEquals("foo-bar", StringUtil.join(Arrays.asList("foo", "bar"), "-"));
        assertEquals("one!=2!=three", StringUtil.join(Arrays.asList("one", "2", "three"), "!="));
    }

    @Test
    public void testJoin(Iterator strings, String sep) {
        assertEquals("", StringUtil.join(Arrays.asList().iterator(), ""));
        assertEquals("foo", StringUtil.join(Arrays.asList("foo").iterator(), ""));
        assertEquals("foo-bar", StringUtil.join(Arrays.asList("foo", "bar").iterator(), "-"));
        assertEquals("one!=2!=three", StringUtil.join(Arrays.asList("one", "2", "three").iterator(), "!="));
    }

    @Test
    public void testJoin(String[] strings, String sep) {
        assertEquals("", StringUtil.join(new String[]{}, ""));
        assertEquals("foo", StringUtil.join(new String[]{"foo"}, ""));
        assertEquals("foo-bar", StringUtil.join(new String[]{"foo", "bar"}, "-"));
        assertEquals("one!=2!=three", StringUtil.join(new String[]{"one", "2", "three"}, "!="));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("          ", StringUtil.padding(10));
        assertEquals("                    ", StringUtil.padding(20));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("    "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("  foo  "));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("    "));
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12345a"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
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
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals(" ", StringUtil.normaliseWhitespace("  "));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo   bar"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo \r\n\tbar"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("    foo bar    "));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "", true);
        assertEquals("", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  ", true);
        assertEquals("", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "foo   bar", false);
        assertEquals("foo bar", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "foo \r\n\tbar", false);
        assertEquals("foo bar", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "    foo bar    ", true);
        assertEquals("foo bar", sb.toString());
    }

    @Test
    public void testIn() {
        assertFalse(StringUtil.in("foo", "bar", "baz"));
        assertTrue(StringUtil.in("foo", "foo", "bar"));
        assertTrue(StringUtil.in("foo", "bar", "foo"));
    }

    @Test
    public void testInSorted() {
        assertFalse(StringUtil.inSorted("foo", new String[]{"bar", "baz"}));
        assertTrue(StringUtil.inSorted("foo", new String[]{"bar", "foo"}));
        assertTrue(StringUtil.inSorted("foo", new String[]{"bar", "foo", "baz"}));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://example.com/");

        assertEquals("http://example.com/", StringUtil.resolve(base, "").toString());
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "foo").toString());
        assertEquals("http://example.com/foo?bar=baz", StringUtil.resolve(base, "foo?bar=baz").toString());
    }

    @Test
    public void testResolveString() {
        assertEquals("", StringUtil.resolve("", "").toString());
        assertEquals("", StringUtil.resolve("http://example.com/", "").toString());
        assertEquals("foo", StringUtil.resolve("", "foo").toString());
        assertEquals("http://example.com/foo", StringUtil.resolve("http://example.com/", "foo").toString());
        assertEquals("http://example.com/foo?bar=baz", StringUtil.resolve("http://example.com/", "foo?bar=baz").toString());
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertSame(sb1, sb2);
        assertTrue(sb1.toString().isEmpty());
    }
}