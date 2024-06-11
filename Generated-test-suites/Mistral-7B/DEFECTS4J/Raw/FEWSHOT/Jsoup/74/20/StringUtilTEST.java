package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        Collection c = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo:bar:baz", StringUtil.join(c, ":"));

        Iterator it = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo:bar:baz", StringUtil.join(it, ":"));

        String[] strings = new String[]{"foo", "bar", "baz"};
        assertEquals("foo:bar:baz", StringUtil.join(strings, ":"));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 22; i++) {
            assertEquals(padding(i), StringUtil.padding(i));
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank(" foo "));
        assertFalse(StringUtil.isBlank(" foo\n"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("1"));
        assertFalse(StringUtil.isNumeric("12"));
        assertFalse(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("1234"));
        assertFalse(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("foo1"));
        assertFalse(StringUtil.isNumeric("1foo"));
        assertFalse(StringUtil.isNumeric("1a"));
    }

    @Test
    public void testIsWhitespace() {
        for (int c : new int[]{' ', '\t', '\n', '\f', '\r'}) {
            assertTrue(StringUtil.isWhitespace(c));
        }
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('z'));
        assertFalse(StringUtil.isWhitespace('!'));
        assertFalse(StringUtil.isWhitespace('.'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        for (int c : new int[]{' ', '\t', '\n', '\f', '\r'}) {
            assertTrue(StringUtil.isActuallyWhitespace(c));
        }
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('z'));
        assertFalse(StringUtil.isActuallyWhitespace('!'));
        assertFalse(StringUtil.isActuallyWhitespace('.'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\t baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\t baz\n"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace(""));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("needle", new String[]{"haystack1", "needle", "haystack2"}));
        assertFalse(StringUtil.in("needle", new String[]{"haystack1", "haystack2"}));
    }

    @Test
    public void testInSorted() {
        List list = Arrays.asList("haystack1", "needle", "haystack2");
        assertTrue(StringUtil.inSorted("needle", list.toArray(new String[0])));
        assertFalse(StringUtil.inSorted("haystack3", list.toArray(new String[0])));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "https://example.com/";
        String relUrl = "/path/file.html";
        URL expected = new URL("https://example.com/path/file.html");
        URL url = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), url.toString());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "../other.html";
        expected = new URL("https://example.com/other.html");
        url = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), url.toString());

        baseUrl = "https://example.com/path/file.html";
        relUrl = "./other.html";
        expected = new URL("https://example.com/other.html");
        url = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), url.toString());

        baseUrl = "https://example.com/path/file.html";
        relUrl = ".";
        assertNull(StringUtil.resolve(new URL(baseUrl), relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();

        sb1.append("foo");
        sb3.append("bar");
        sb1.append(sb3);

        StringBuilder sb4 = StringUtil.stringBuilder();
        sb4.append("qux");

        assertTrue(sb1.toString().equals("foobar"));
        assertNotNull(sb4);
    }

}