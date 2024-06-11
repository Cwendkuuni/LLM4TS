package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.jsoup.helper.StringUtil.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String result = join(strings, " ");
        Assert.assertEquals("hello world !", result);
    }

    @Test
    public void testJoinCollectionEmpty() {
        List<String> strings = Collections.emptyList();
        String result = join(strings, " ");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinCollectionNull() {
        List<String> strings = null;
        String result = join(strings, " ");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world", "!"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String result = join(iterator, " ");
        Assert.assertEquals("hello world !", result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        String[] strings = new String[0];
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String result = join(iterator, " ");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "!"};
        String result = join(strings, " ");
        Assert.assertEquals("hello world !", result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String result = join(strings, " ");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinArrayNull() {
        String[] strings = null;
        String result = join(strings, " ");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            String paddingString = padding(i);
            String expected = String.format("%" + (i + 1) + "s", "");
            Assert.assertEquals(expected, paddingString);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegative() {
        padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        Assert.assertTrue(isBlank(null));
    }

    @Test
    public void testIsBlankEmpty() {
        Assert.assertTrue(isBlank(""));
    }

    @Test
    public void testIsBlankWhitespace() {
        String whitespace = "   ";
        Assert.assertTrue(isBlank(whitespace));
    }

    @Test
    public void testIsBlankNonWhitespace() {
        String nonWhitespace = "hello";
        Assert.assertFalse(isBlank(nonWhitespace));
    }

    @Test
    public void testIsNumericEmpty() {
        Assert.assertFalse(isNumeric(""));
    }

    @Test
    public void testIsNumericTrue() {
        String numeric = "12345";
        Assert.assertTrue(isNumeric(numeric));
    }

    @Test
    public void testIsNumericFalse() {
        String nonNumeric = "abc";
        Assert.assertFalse(isNumeric(nonNumeric));
    }

    @Test
    public void testIsWhitespaceSpace() {
        Assert.assertTrue(isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        Assert.assertTrue(isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewline() {
        Assert.assertTrue(isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        Assert.assertTrue(isWhitespace('\f'));
    }

    @Test
    public void testIsWhitespaceReturn() {
        Assert.assertTrue(isWhitespace('\r'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "   hello world   \n\r   \t   ";
        String expected = "hello world";
        String result = normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "world";
        String[] haystack = {"hello world", "hello foo", "foo world"};
        boolean result = in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInEmptyString() {
        String needle = "world";
        String[] haystack = {};
        boolean result = in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInNullArray() {
        String needle = "world";
        String[] haystack = null;
        boolean result = in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSingleElementArray() {
        String needle = "world";
        String[] haystack = {"world"};
        boolean result = in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInSingleElementString() {
        String needle = "world";
        String haystack = "world";
        boolean result = in(needle, haystack.split(""));
        Assert.assertTrue(result);
    }

    @Test
    public void testInEmptyNeedle() {
        String needle = "";
        String[] haystack = {"hello world", "hello foo", "foo world"};
        boolean result = in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSameString() {
        String needle = "hello world";
        String haystack = "hello world";
        boolean result = in(needle, haystack.split(""));
        Assert.assertTrue(result);
    }

    @Test
    public void testInDifferentStrings() {
        String needle = "hello world";
        String haystack = "foo bar baz";
        boolean result = in(needle, haystack.split(""));
        Assert.assertFalse(result);
    }

    @Test
    public void testResolveAbsoluteURL() throws MalformedURLException {
        String base = "http://example.com/path/to/index.html";
        String relUrl = "/about.html";
        URL expected = new URL("http://example.com/about.html");
        URL result = resolve(new URL(base), relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteURLWithRelativeURLAsBase() throws MalformedURLException {
        String base = "/about.html";
        String relUrl = "/path/to/index.html";
        URL expected = new URL("http://example.com/path/to/index.html");
        URL result = resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteURLWithInvalidBase() throws MalformedURLException {
        String base = "http://example.com";
        String relUrl = "/path/to/index.html";
        URL expected = new URL("http://example.com/path/to/index.html");
        URL result = resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveAbsoluteURLWithInvalidRelativeURL() throws MalformedURLException {
        String base = "http://example.com/path/to/index.html";
        String relUrl = "/foo/bar%";
        resolve(base, relUrl);
    }

    @Test
    public void testResolveAbsoluteURLWithEmptyBase() throws MalformedURLException {
        String base = "";
        String relUrl = "/path/to/index.html";
        URL result = resolve(base, relUrl);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testResolveAbsoluteURLWithNullBase() throws MalformedURLException {
        String base = null;
        String relUrl = "/path/to/index.html";
        URL result = resolve(base, relUrl);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testResolveAbsoluteURLWithNullRelativeURL() throws MalformedURLException {
        String base = "http://example.com/path/to/index.html";
        String relUrl = null;
        URL result = resolve(base, relUrl);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testResolveAbsoluteURLWithEmptyRelativeURL() throws MalformedURLException {
        String base = "http://example.com/path/to/index.html";
        String relUrl = "";
        URL result = resolve(base, relUrl);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testResolveAbsoluteURLWithEmptyStringAsBase() throws MalformedURLException {
        String base = "";
        String relUrl = "/path/to/index.html";
        URL result = resolve(base, relUrl);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testResolveAbsoluteURLWithNull() throws MalformedURLException {
        String base = null;
        String relUrl = "/path/to/index.html";
        URL result = resolve(base, relUrl);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        Assert.assertEquals(0, sb.length());
        sb.append("hello");
        Assert.assertEquals("hello", sb.toString());
        sb.append(" world");
        Assert.assertEquals("hello world", sb.toString());
        sb.append("!");
        Assert.assertEquals("hello world!", sb.toString());
    }

    @Test
    public void testStringBuilderWithMaxSize() {
        StringBuilder sb = StringUtil.stringBuilder();
        for (int i = 0; i < MaxCachedBuilderSize * 2; i++) {
            sb.append("x");
        }
        Assert.assertEquals(MaxCachedBuilderSize * 2, sb.length());
        String newSb = StringUtil.stringBuilder().append("y");
        Assert.assertEquals(1, sb.length());
    }

}