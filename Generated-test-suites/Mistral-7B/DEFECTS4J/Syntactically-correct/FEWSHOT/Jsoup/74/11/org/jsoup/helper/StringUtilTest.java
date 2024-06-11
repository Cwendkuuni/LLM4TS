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
        assertEquals("foo, bar, buzz", StringUtil.join(strings, ", "));

        strings = Collections.emptyList();
        assertEquals("", StringUtil.join(strings, " "));

        strings = Arrays.asList("foo", null, "baz");
        assertEquals("foo, , baz", StringUtil.join(strings, ", "));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 21; i++) {
            assertEquals("" + i, StringUtil.padding(i));
        }

        try {
            StringUtil.padding(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo bar"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("1a"));
        assertTrue(StringUtil.isNumeric("123"));
    }

    @Test
    public void testIsWhitespace() {
        assertFalse(StringUtil.isWhitespace(0x2014)); // em dash
        assertFalse(StringUtil.isWhitespace(0x2015)); // en dash
        assertFalse(StringUtil.isWhitespace(0x2018)); // left quotation mark
        assertFalse(StringUtil.isWhitespace(0x2019)); // right quotation mark
        assertTrue(StringUtil.isWhitespace(0x20)); // space
        assertTrue(StringUtil.isWhitespace(0x09)); // tab
        assertTrue(StringUtil.isWhitespace(0x0A)); // line feed
        assertTrue(StringUtil.isWhitespace(0x0D)); // carriage return

        assertFalse(StringUtil.isActuallyWhitespace(0x2014));
        assertFalse(StringUtil.isActuallyWhitespace(0x2015));
        assertFalse(StringUtil.isActuallyWhitespace(0x2018));
        assertFalse(StringUtil.isActuallyWhitespace(0x2019));
        assertTrue(StringUtil.isActuallyWhitespace(0x20));
        assertTrue(StringUtil.isActuallyWhitespace(0x09));
        assertTrue(StringUtil.isActuallyWhitespace(0x0A));
        assertTrue(StringUtil.isActuallyWhitespace(0x0D));
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo \t bar\n baz", StringUtil.normaliseWhitespace("foo \t bar\n baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar   baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar	baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\n baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\tbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\r\nbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\r baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\f baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar"));
        assertEquals("", StringUtil.normaliseWhitespace(""));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("foo", "foo", "foo", "bar", "baz"));
        assertFalse(StringUtil.in("foo", "bar", "baz"));
        assertFalse(StringUtil.in(null, "foo", "bar", "baz"));
        assertFalse(StringUtil.in("", "foo", "bar", "baz"));
    }

    @Test
    public void testInSorted() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertTrue(StringUtil.inSorted("foo", strings.toArray(new String[0])));
        assertFalse(StringUtil.inSorted("quux", strings.toArray(new String[0])));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "/foo";
        URL expected = new URL(baseUrl, relUrl);
        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/";
        relUrl = "/foo/";
        expected = new URL(baseUrl, relUrl);
        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = "bar";
        expected = new URL(baseUrl, relUrl);
        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = "../bar";
        expected = new URL(baseUrl, relUrl);
        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = "../../bar";
        expected = new URL("http://example.com/bar");
        assertEquals(expected.toString(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = ".";
        assertSame(baseUrl, StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = "..";
        assertSame(new URL("http://example.com"), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = "";
        assertSame(baseUrl, StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/foo";
        relUrl = null;
        assertNull(StringUtil.resolve(new URL(baseUrl), relUrl));
    }

    @Test
    public void testResolveWithNullBaseUrl() {
        String relUrl = "/foo";
        assertNull(StringUtil.resolve(null, relUrl));
    }

    @Test
    public void testResolveWithNullRelUrl() {
        String baseUrl = "http://example.com/foo";
        assertNull(StringUtil.resolve(new URL(baseUrl), null));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithInvalidUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/foo";
        String relUrl = "foo:bar";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithRelativeUrlStartingWithDoubleSlash() throws MalformedURLException {
        String baseUrl = "http://example.com/foo";
        String relUrl = "//example.com/bar";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithRelativeUrlStartingWithThreeSlashes() throws MalformedURLException {
        String baseUrl = "http://example.com/foo";
        String relUrl = "///example.com/bar";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }
}