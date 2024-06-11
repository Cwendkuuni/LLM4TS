package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
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
        // Collection test
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));

        // Iterator test
        Iterator<String> iterator = strings.iterator();
        assertEquals("foo,bar,baz", StringUtil.join(iterator, ","));

        // Array test
        String[] arr = new String[]{"foo", "bar", "baz"};
        assertEquals("foo,bar,baz", StringUtil.join(arr, ","));

        // Empty collections/iterators/arrays test
        assertEquals("", StringUtil.join(Arrays.asList(), ""));
        assertEquals("", StringUtil.join(Arrays.asList(new String[0]).iterator(), ""));
        assertEquals("", StringUtil.join(new String[0], ""));
    }

    @Test
    public void testPadding() {
        for (int width = 0; width <= padding.length; width++) {
            assertEquals(padding[width], StringUtil.padding(width));
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
        assertTrue(StringUtil.isBlank(" foo "));
        assertTrue(StringUtil.isBlank(" foo\n"));
        assertTrue(StringUtil.isBlank(" foo\r"));
        assertTrue(StringUtil.isBlank(" foo\t"));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo bar"));
        assertFalse(StringUtil.isBlank("foo bar "));
        assertFalse(StringUtil.isBlank("foo bar\n"));
        assertFalse(StringUtil.isBlank("foo bar\r"));
        assertFalse(StringUtil.isBlank("foo bar\t"));
        assertFalse(StringUtil.isBlank("foo bar foo"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("123abc"));
        assertFalse(StringUtil.isNumeric("123."));
        assertFalse(StringUtil.isNumeric("ab123"));
        assertFalse(StringUtil.isNumeric("123e3"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("123456"));
        assertTrue(StringUtil.isNumeric("1234567"));
    }

    @Test
    public void testIsWhitespaceAndIsActuallyWhitespace() {
        assertTrue(StringUtil.isWhitespace('\u0020'));
        assertTrue(StringUtil.isWhitespace('\u0009'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('z'));
        assertTrue(StringUtil.isActuallyWhitespace('\u0020'));
        assertTrue(StringUtil.isActuallyWhitespace('\u0009'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\nbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar \tbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\t\nbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\t\t\nbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\t\t\t\tbaz"));
    }

    @Test
    public void testIn() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertTrue(StringUtil.in(null, strings));
        assertTrue(StringUtil.in("", strings));
        assertTrue(StringUtil.in("foo", strings));
        assertTrue(StringUtil.in("bar", strings));
        assertTrue(StringUtil.in("baz", strings));
        assertFalse(StringUtil.in("quux", strings));

        String[] arr = new String[]{"foo", "bar", "baz"};
        assertTrue(StringUtil.in(null, arr));
        assertTrue(StringUtil.in("", arr));
        assertTrue(StringUtil.in("foo", arr));
        assertTrue(StringUtil.in("bar", arr));
        assertTrue(StringUtil.in("baz", arr));
        assertFalse(StringUtil.in("quux", arr));
    }

    @Test
    public void testInSorted() {
        String[] arr = new String[]{"bar", "foo", "quux", "baz"};
        assertFalse(StringUtil.inSorted("quux", arr));
        assertFalse(StringUtil.inSorted("quux", new String[0]));
        assertTrue(StringUtil.inSorted("foo", arr));
        assertTrue(StringUtil.inSorted("foo", new String[0]));
        assertTrue(StringUtil.inSorted("bar", arr));
        assertTrue(StringUtil.inSorted("bar", new String[0]));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl1 = "relative.html";
        String relUrl2 = "../other/file.html";
        String relUrl3 = "http://www.example.com/another/file.html";

        assertEquals("http://www.example.com/path/to/relative.html", StringUtil.resolve(baseUrl, relUrl1));
        assertEquals("http://www.example.com/other/file.html", StringUtil.resolve(baseUrl, relUrl2));
        assertEquals("http://www.example.com/another/file.html", StringUtil.resolve(baseUrl, relUrl3));
    }

    @Test
    public void testResolveWithMalformedURL() {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = "//relative.html";

        assertEquals("", StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testResolveWithNullBaseUrl() {
        String relUrl = "relative.html";

        assertNull(StringUtil.resolve(null, relUrl));
    }

    @Test
    public void testResolveWithNullRelUrl() {
        String baseUrl = "http://www.example.com/path/to/file.html";

        assertNull(StringUtil.resolve(baseUrl, null));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithMalformedRelUrl() {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = "relative";

        StringUtil.resolve(baseUrl, relUrl);
    }
}