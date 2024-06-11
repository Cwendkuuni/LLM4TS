
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        String expected = "foo,bar,baz";
        String result = StringUtil.join(strings, ",");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("foo", "bar", "baz").iterator();
        String expected = "foo,bar,baz";
        String result = StringUtil.join(strings, ",");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"foo", "bar", "baz"};
        String expected = "foo,bar,baz";
        String result = StringUtil.join(strings, ",");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("          ", StringUtil.padding(10));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("foo"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("123a"));
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
        String input = "   foo     bar   baz   ";
        String expected = "foo bar baz";
        String result = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  foo    ", true);
        StringUtil.appendNormalisedWhitespace(sb, "bar   baz   ", false);
        String expected = "foo bar baz";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertFalse(StringUtil.in("foobar", "foo", "bar", "baz"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"bar", "baz", "foo"};
        assertTrue(StringUtil.inSorted("foo", haystack));
        assertFalse(StringUtil.inSorted("foobar", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "path/to/resource";
        URL expected = new URL("http://www.example.com/path/to/resource");
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "path/to/resource";
        String expected = "http://www.example.com/path/to/resource";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertFalse(sb1.equals(sb2));
    }

}

