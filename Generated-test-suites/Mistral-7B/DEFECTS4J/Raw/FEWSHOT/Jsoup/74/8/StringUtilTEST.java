package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo:bar:baz", StringUtil.join(strings, ":"));
        assertEquals("foo bar baz", StringUtil.join(strings, " "));
        assertEquals("foo bar baz", StringUtil.join(strings.toArray(new String[0]), " "));

        strings = Collections.singletonList(null);
        assertNull(StringUtil.join(strings, ""));

        strings = Collections.emptyList();
        assertEquals("", StringUtil.join(strings, ""));

        strings = Arrays.asList("");
        assertEquals("", StringUtil.join(strings, ""));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= padding.length; i++) {
            String paddingString = StringUtil.padding(i);
            assertEquals(i, paddingString.length());
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
        assertTrue(StringUtil.isBlank("  "));
        assertTrue(StringUtil.isBlank("    "));
        assertTrue(StringUtil.isBlank("       "));

        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo bar"));
        assertFalse(StringUtil.isBlank("foo bar baz"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("foo bar"));
        assertFalse(StringUtil.isNumeric("1a"));

        assertTrue(StringUtil.isNumeric("1"));
        assertTrue(StringUtil.isNumeric("12"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("123456"));
        assertTrue(StringUtil.isNumeric("1234567"));
    }

    @Test
    public void testIsWhitespace() {
        assertFalse(StringUtil.isWhitespace(0x0000));
        assertFalse(StringUtil.isWhitespace(0x0020));
        assertFalse(StringUtil.isWhitespace(0x00A0));
        assertFalse(StringUtil.isWhitespace(0x1F680));
        assertFalse(StringUtil.isWhitespace(0x200B));
        assertFalse(StringUtil.isWhitespace(0x200C));
        assertFalse(StringUtil.isWhitespace(0x2011));
        assertFalse(StringUtil.isWhitespace(0x2013));
        assertFalse(StringUtil.isWhitespace(0x2014));
        assertFalse(StringUtil.isWhitespace(0x2015));
        assertFalse(StringUtil.isWhitespace(0x201E));
        assertFalse(StringUtil.isWhitespace(0x202F));
        assertFalse(StringUtil.isWhitespace(0x205F));
        assertFalse(StringUtil.isWhitespace(0x3030));

        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertTrue(StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertFalse(StringUtil.isActuallyWhitespace(0x0000));
        assertFalse(StringUtil.isActuallyWhitespace(0x0020));
        assertFalse(StringUtil.isActuallyWhitespace(0x00A0));
        assertFalse(StringUtil.isActuallyWhitespace(0x1F680));
        assertFalse(StringUtil.isActuallyWhitespace(0x200B));
        assertFalse(StringUtil.isActuallyWhitespace(0x200C));
        assertFalse(StringUtil.isActuallyWhitespace(0x2011));
        assertFalse(StringUtil.isActuallyWhitespace(0x2013));
        assertFalse(StringUtil.isActuallyWhitespace(0x2014));
        assertFalse(StringUtil.isActuallyWhitespace(0x2015));
        assertFalse(StringUtil.isActuallyWhitespace(0x201E));
        assertFalse(StringUtil.isActuallyWhitespace(0x202F));
        assertFalse(StringUtil.isActuallyWhitespace(0x205F));
        assertFalse(StringUtil.isActuallyWhitespace(0x3030));

        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo   bar"));
        assertEquals("foo bar\nbaz", StringUtil.normaliseWhitespace("foo   bar\nbaz"));
        assertEquals("foo bar\nbaz\r", StringUtil.normaliseWhitespace("foo   bar\nbaz\r"));
        assertEquals("foo bar\nbaz\r qux", StringUtil.normaliseWhitespace("foo   bar\nbaz\r qux"));
        assertEquals("foo bar\nbaz\r qux quux", StringUtil.normaliseWhitespace("foo   bar\nbaz\r qux quux"));
        assertEquals("foo bar\nbaz\r qux quux\n quux quux quux", StringUtil.normaliseWhitespace("foo   bar\nbaz\r qux quux\n quux quux quux"));
    }

    @Test
    public void testIn() {
        assertFalse(StringUtil.in("needle", new String[] {}));
        assertFalse(StringUtil.in("needle", new String[] {"foo"}));
        assertTrue(StringUtil.in("needle", new String[] {"needle", "foo"}));
        assertTrue(StringUtil.in("foo", new String[] {"foo", "needle"}));

        assertFalse(StringUtil.inSorted("needle", new String[] {"apple", "banana", "orange", "needle"}));
        assertTrue(StringUtil.inSorted("needle", new String[] {"apple", "banana", "needle", "orange"}));
        assertTrue(StringUtil.inSorted("needle", new String[] {"apple", "needle", "orange"}));
        assertTrue(StringUtil.inSorted("needle", new String[] {"needle", "orange"}));
        assertTrue(StringUtil.inSorted("needle", new String[] {"needle"}));

        assertFalse(StringUtil.inSorted("needle", new String[] {"apple", "banana", "orange"}));
        assertFalse(StringUtil.inSorted("needle", new String[] {"apple", "orange"}));
        assertFalse(StringUtil.inSorted("needle", new String[] {"banana", "orange"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/foo/bar";
        String relUrl = "baz";
        assertEquals("http://example.com/foo/bar/baz", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "/baz";
        assertEquals("http://example.com/foo/baz", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "baz/";
        assertEquals("http://example.com/foo/baz", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "baz";
        // baseUrl is already absolute, shouldn't change it
        assertEquals("http://example.com/foo/bar", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = ".";
        assertEquals("http://example.com/foo/bar", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "..";
        assertEquals("http://example.com/foo", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/bar";
        assertEquals("http://example.com/foo/bar", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/bar/baz";
        assertEquals("http://example.com/foo/baz", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/bar/baz/..";
        assertEquals("http://example.com/foo", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar";
        assertEquals("http://example.com/bar", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar/baz";
        assertEquals("http://example.com/bar/baz", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar/baz/..";
        assertEquals("http://example.com", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar/baz/../qux";
        assertEquals("http://example.com/qux", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/foo/bar";
        relUrl = "//qux.com/baz";
        // baseUrl is not a valid URL, should fail to parse it
        assertNull(StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        String baseUrl = "http://example.com/foo/bar";
        String relUrl = "baz";
        URL url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo/bar/baz", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "/baz";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo/baz", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "baz/";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo/baz", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = ".";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo/bar", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "..";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/bar";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo/bar", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/bar/baz";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/foo/baz", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/bar", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar/baz";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/bar/baz", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar/baz/..";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "../foo/../bar/baz/../qux";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertEquals("http://example.com/qux", url.toExternalForm());

        baseUrl = "http://example.com/foo/bar";
        relUrl = "//qux.com/baz";
        url = new URL(StringUtil.resolve(baseUrl, relUrl));
        assertNull(url);

        baseUrl = null;
        relUrl = "baz";
        try {
            url = new URL(StringUtil.resolve(baseUrl, relUrl));
            fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }
}