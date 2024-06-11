package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
        List<String> list = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(list, ", "));

        Collection<String> collection = Arrays.asList("", "foo", null, "bar");
        assertEquals("", StringUtil.join(collection, ""));
        assertEquals("", StringUtil.join(collection, ", "));
        assertEquals("foo,bar", StringUtil.join(collection, ", "));

        Iterator<String> iterator = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo,bar,baz", StringUtil.join(iterator, ", "));

        String[] strings = new String[] { "", "foo", null, "bar" };
        assertEquals("", StringUtil.join(strings, ""));
        assertEquals("", StringUtil.join(strings, ", "));
        assertEquals("foo,bar", StringUtil.join(strings, ", "));

        assertNull(StringUtil.join(null, ""));
        assertNull(StringUtil.join(null, ", "));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < StringUtil.padding.length; i++) {
            assertEquals(StringUtil.padding[i], StringUtil.padding(i));
        }

        assertEquals("   abc", StringUtil.padding(5) + "abc");
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo "));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("1a"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('z'));
        assertFalse(StringUtil.isWhitespace('\u00A0'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00AD'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar  \tbaz\r"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\nbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", new String[] { "foo", "bar", "baz" }));
        assertFalse(StringUtil.in("foo", new String[] { "bar", "baz" }));
        assertFalse(StringUtil.in("qux", new String[] { "foo", "bar", "baz" }));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("foo", new String[] { "foo", "bar", "baz" }));
        assertFalse(StringUtil.inSorted("qux", new String[] { "foo", "bar", "baz" }));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com";
        String relUrl = "/path/file.html";
        URL url = new URL("http://example.com/path/file.html");
        assertEquals(url.toExternalForm(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "/../another-file.html";
        url = new URL("http://example.com/another-file.html");
        assertEquals(url.toExternalForm(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "../another-file.html";
        url = new URL("http://example.com/another-file.html");
        assertEquals(url.toExternalForm(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "..";
        assertNull(StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = ".";
        assertEquals(baseUrl, StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "..";
        assertNull(StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com";
        relUrl = "/path/file.html";
        url = new URL("http://example.com/path/file.html");
        assertEquals(url.toExternalForm(), StringUtil.resolve(new URL(baseUrl), relUrl));

        baseUrl = "http://example.com";
        relUrl = "/path/file.html";
        url = new URL("http://example.com/path/file.html");
        assertEquals(url.toExternalForm(), StringUtil.resolve(new URL(baseUrl), relUrl));
    }

    @Test
    public void testResolveAbsolute() {
        String baseUrl = "http://example.com";
        String relUrl = "/path/file.html";
        String urlString = "http://example.com/path/file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "../another-file.html";
        urlString = "http://example.com/another-file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "../another-file.html";
        urlString = "http://example.com/another-file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "..";
        assertNull(StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = ".";
        assertEquals(baseUrl, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/file.html";
        relUrl = "..";
        assertNull(StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com";
        relUrl = "/path/file.html";
        urlString = "http://example.com/path/file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com";
        relUrl = "/path/file.html";
        urlString = "http://example.com/path/file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com";
        relUrl = "/path/file.html";
        urlString = "http://example.com/path/file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com";
        relUrl = "//another-host/path/file.html";
        urlString = "http://another-host/path/file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com";
        relUrl = "//another-host/path/file.html";
        urlString = "http://another-host/path/file.html";
        assertEquals(urlString, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();

        sb1.append("foo");
        sb2.append("bar");
        sb3.append("baz");

        assertEquals("foo", sb1.toString());
        assertEquals("bar", sb2.toString());
        assertEquals("baz", sb3.toString());

        assertEquals("foo", sb1.toString());
        assertEquals("foo bar", sb1.append(sb2).toString());
        assertEquals("foo bar baz", sb1.append(sb2).append(sb3).toString());

        assertEquals(3, (int) (sb1.capacity() - sb1.length()));
        assertEquals(12, (int) (sb3.capacity() - sb3.length()));
    }
}