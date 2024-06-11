package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.jsoup.helper.StringUtil.join;
import static org.jsoup.helper.StringUtil.isActuallyWhitespace;
import static org.jsoup.helper.StringUtil.isBlank;
import static org.jsoup.helper.StringUtil.isNumeric;
import static org.jsoup.helper.StringUtil.join;
import static org.jsoup.helper.StringUtil.padding;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world");
        String expected = "helloworld";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = Collections.emptyList();
        String actual = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world"};
        String expected = "helloworld";
        String actual = StringUtil.join(Arrays.asList(strings), "");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinNegativeWidth() {
        int width = -1;
        String actual = StringUtil.padding(width);
        Assert.fail();
    }

    @Test
    public void testIsBlankNullString() {
        String actual = StringUtil.isBlank(null);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsBlankEmptyString() {
        String actual = StringUtil.isBlank("");
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsBlankWhitespaceString() {
        String actual = StringUtil.isBlank("   ");
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsBlankNonWhitespaceString() {
        String actual = StringUtil.isBlank("hello");
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsNumericTrue() {
        String actual = StringUtil.isNumeric("123");
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String actual = StringUtil.isNumeric("");
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String actual = StringUtil.isNumeric("abc");
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsWhitespaceTrue() {
        int codePoint = ' ';
        boolean actual = StringUtil.isWhitespace(codePoint);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsWhitespaceFalse() {
        int codePoint = 'a';
        boolean actual = StringUtil.isWhitespace(codePoint);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrue() {
        int codePoint = ' ';
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsActuallyWhitespaceFalse() {
        int codePoint = 'a';
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        Assert.assertFalse(actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String actual = StringUtil.normaliseWhitespace("   foo   ");
        String expected = "foo";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceNoChange() {
        String actual = StringUtil.normaliseWhitespace("foo");
        String expected = "foo";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String actual = StringUtil.normaliseWhitespace("foo   bar");
        String expected = "foo bar";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsInTrue() {
        String needle = "foo";
        String[] haystack = {"foo", "bar", "baz", "qux", "foo"};
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsInFalse() {
        String needle = "qux";
        String[] haystack = {"foo", "bar", "baz", "qux", "foo"};
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertFalse(actual);
    }

    @Test
    public void testInSortedTrue() {
        String needle = "qux";
        String[] haystack = {"qux", "bar", "foo", "baz"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(actual);
    }

    @Test
    public void testInSortedFalse() {
        String needle = "quux";
        String[] haystack = {"qux", "bar", "foo", "baz"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(actual);
    }

    @Test
    public void testResolveNullBase() {
        String relUrl = "/path/to/file.html";
        String actual = StringUtil.resolve(null, relUrl);
        Assert.assertNull(actual);
    }

    @Test
    public void testResolveEmptyBase() {
        String relUrl = "/path/to/file.html";
        String actual = StringUtil.resolve("", relUrl);
        Assert.assertEquals("", actual);
    }

    @Test
    public void testResolveRelativeUrlOnly() {
        String baseUrl = "http://example.com/";
        String relUrl = "/path/to/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/path/to/file.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsoluteUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "/path/to/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/path/to/file.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveMalformedUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "//path/to/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path/to/file.html", actual);
    }

    @Test
    public void testResolveRelativeUrlWithDeltaBase() {
        String baseUrl = "http://example.com/path/to/";
        String relUrl = "../file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/file.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelativeUrlWithDeltaBaseFail() {
        String baseUrl = "http://example.com/path/to/";
        String relUrl = "..//file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", actual);
    }

    @Test
    public void testResolveRelativeUrlWithProtocol() {
        String baseUrl = "http://example.com/path/to/";
        String relUrl = "//another.example.com/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://another.example.com/file.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelativeUrlWithPort() {
        String baseUrl = "http://example.com:8080/path/to/";
        String relUrl = "//another.example.com:80/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://another.example.com:80/file.html";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlBase() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/";
        String relUrl = "://another.example.com:80/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlRelative() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/";
        String relUrl = "://another.example.com:80/file.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        sb1.append("hello");
        sb2.append("world");

        StringBuilder sb3 = StringUtil.stringBuilder();
        sb3.append(sb1);
        sb3.append(sb2);

        String expected = "helloworld";
        String actual = sb3.toString();
        Assert.assertEquals(expected, actual);
    }

}