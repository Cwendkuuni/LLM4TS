package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));

        strings = Collections.emptyList();
        assertEquals("", StringUtil.join(strings, ","));

        assertEquals("a", StringUtil.join(Collections.singletonList("a"), ""));
        assertEquals("a", StringUtil.join(Collections.singletonList("a")));

        try {
            StringUtil.join(null, ",");
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            StringUtil.join(Collections.emptyIterator(), ",");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(3));
        assertEquals("    ", StringUtil.padding(5));
        assertEquals("      ", StringUtil.padding(8));
        assertEquals("         ", StringUtil.padding(11));

        try {
            StringUtil.padding(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("  "));
        assertTrue(StringUtil.isBlank("    "));
        assertTrue(StringUtil.isBlank("      "));
        assertTrue(StringUtil.isBlank("         "));
        assertTrue(StringUtil.isBlank("         "));
        assertTrue(StringUtil.isBlank(" foo\r\nbar"));

        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo "));
        assertFalse(StringUtil.isBlank("foo "));
        assertFalse(StringUtil.isBlank("foo bar"));
        assertFalse(StringUtil.isBlank("foo bar\n"));
        assertFalse(StringUtil.isBlank("foo bar\n"));
        assertFalse(StringUtil.isBlank("foo bar\n"));
        assertFalse(StringUtil.isBlank("\u00A0foo bar\u00A0"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("0"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234567890"));
        assertTrue(StringUtil.isNumeric("1234567890123"));
        assertTrue(StringUtil.isNumeric("123456789012345"));
        assertTrue(StringUtil.isNumeric("1234567890123456"));
        assertTrue(StringUtil.isNumeric("12345678901234567"));

        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("123a"));
        assertFalse(StringUtil.isNumeric("123a4"));
        assertFalse(StringUtil.isNumeric("123a45"));
        assertFalse(StringUtil.isNumeric("123a456"));
        assertFalse(StringUtil.isNumeric("123a4567"));
        assertFalse(StringUtil.isNumeric("123a45678"));
        assertFalse(StringUtil.isNumeric("123a456789"));
        assertFalse(StringUtil.isNumeric("foo"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace(160));

        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('\u0000'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertTrue(StringUtil.isActuallyWhitespace(160)); // non-breaking space

        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('\u0000'));
        assertFalse(StringUtil.isActuallyWhitespace('\u00A0')); // noise control character
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("", StringUtil.normaliseWhitespace("  "));
        assertEquals("", StringUtil.normaliseWhitespace("  \t "));
        assertEquals("", StringUtil.normaliseWhitespace("  \t \r\n "));
        assertEquals("", StringUtil.normaliseWhitespace("  \t \r\n "));
        assertEquals("", StringUtil.normaliseWhitespace("  \t \r\n "));

        assertEquals("foo", StringUtil.normaliseWhitespace("foo"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo bar"));
        assertEquals("foo bar\n", StringUtil.normaliseWhitespace("foo bar\n"));
        assertEquals("foo bar\n\r", StringUtil.normaliseWhitespace("foo bar\n\r"));
        assertEquals("foo bar\n\r", StringUtil.normaliseWhitespace(" foo bar\n\r"));
        assertEquals("foo bar\n\r", StringUtil.normaliseWhitespace("foo bar\n \r"));

        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar   baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace(" foo bar   baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz  "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz \n"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz \r"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz \r\n"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz \r\n \t"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("foo", "foo", "bar", "foo"));
        assertFalse(StringUtil.in("foo", "bar", "baz", "qux"));
        assertFalse(StringUtil.in("qux", "bar", "baz", "qux"));

        String[] needleStrings = {"foo", "bar", "baz"};
        assertTrue(StringUtil.in(Arrays.asList(needleStrings), "foo"));
        assertTrue(StringUtil.in(Arrays.asList(needleStrings), "bar"));
        assertTrue(StringUtil.in(Arrays.asList(needleStrings), "baz"));
        assertFalse(StringUtil.in(Arrays.asList(needleStrings), "qux"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"foo", "bar", "baz"};
        assertTrue(StringUtil.inSorted("foo", haystack));
        assertTrue(StringUtil.inSorted("bar", haystack));
        assertTrue(StringUtil.inSorted("baz", haystack));
        assertFalse(StringUtil.inSorted("qux", haystack));

        String[] haystackSorted = {"bar", "baz", "foo"};
        assertTrue(StringUtil.inSorted("foo", haystackSorted));
        assertTrue(StringUtil.inSorted("bar", haystackSorted));
        assertTrue(StringUtil.inSorted("baz", haystackSorted));
        assertFalse(StringUtil.inSorted("qux", haystackSorted));

        List<String> needleList = Arrays.asList("foo", "bar", "baz");
        assertTrue(StringUtil.inSorted("foo", haystack));
        assertTrue(StringUtil.inSorted("bar", haystack));
        assertTrue(StringUtil.inSorted("baz", haystack));
        assertFalse(StringUtil.inSorted("qux", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "https://example.com/";
        String relUrl = "path/file.html";

        assertEquals("https://example.com/path/file.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/path/file.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "/another-path.html";

        assertEquals("https://example.com/another-path.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/another-path.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "../another-path.html";

        assertEquals("https://example.com/another-path.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/another-path.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "/../another-path.html";

        assertEquals("https://example.com/another-path.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/another-path.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "//example.net/another-path.html";

        assertEquals("https://example.net/another-path.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.net/another-path.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "/file.html";

        assertEquals("https://example.com/file.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/file.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "file.html";

        assertEquals("https://example.com/file.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/file.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "./file.html";

        assertEquals("https://example.com/file.html", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/file.html", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "./";

        assertEquals("https://example.com/", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "../";

        assertEquals("https://example.com/", StringUtil.resolve(baseUrl, relUrl));
        assertEquals("https://example.com/", new URL(StringUtil.resolve(baseUrl, relUrl)).toExternalForm());

        baseUrl = null;
        relUrl = "path/file.html";

        assertEquals("", StringUtil.resolve(baseUrl, relUrl));
        assertNull(StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "https://example.com/path/file.html";
        relUrl = null;

        assertEquals("", StringUtil.resolve(baseUrl, relUrl));
        assertNull(StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testResolveWithMalformedURL() throws MalformedURLException {
        // The base URL is malformed, thus the rel URL should be returned
        String baseUrl = "//example.com";
        String relUrl = "path/file.html";

        assertEquals("", StringUtil.resolve(baseUrl, relUrl));
        assertNull(StringUtil.resolve(baseUrl, relUrl));

        // The base URL is malformed, thus the rel URL should be returned
        baseUrl = "https:example.com/path/file.html";

        assertEquals("", StringUtil.resolve(baseUrl, relUrl));
        assertNull(StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertEquals(0, sb.length());

        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.append(" bar");
        assertEquals("foo bar", sb.toString());

        sb.appendCodePoint(0x00A0);
        assertEquals("foo bar\u00A0", sb.toString());

        sb.setLength(0);
        assertEquals(0, sb.length());

        sb.append("foo");
        assertEquals("foo", sb.toString());
    }
}