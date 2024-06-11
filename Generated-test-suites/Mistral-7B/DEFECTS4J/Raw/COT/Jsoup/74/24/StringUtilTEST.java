package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String actual = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = Collections.emptyList();
        String actual = StringUtil.join(strings.iterator(), "");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world", "!"};
        String expected = "helloworld!";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        String actual = StringUtil.padding(5);
        String expected = "     ";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePadding() {
        StringUtil.padding(-5);
    }

    @Test
    public void testIsBlank() {
        String actual = StringUtil.isBlank(null);
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        String empty = "";
        actual = StringUtil.isBlank(empty);
        expected = true;
        Assert.assertEquals(expected, actual);

        String whitespace = "   ";
        actual = StringUtil.isBlank(whitespace);
        expected = true;
        Assert.assertEquals(expected, actual);

        String notBlank = "hello";
        actual = StringUtil.isBlank(notBlank);
        expected = false;
        Assert.assertFalse(actual);

        String hasDigit = "123";
        actual = StringUtil.isBlank(hasDigit);
        expected = false;
        Assert.assertFalse(actual);

        String hasSpace = " 123 ";
        actual = StringUtil.isBlank(hasSpace);
        expected = false;
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsNumeric() {
        String actual = StringUtil.isNumeric("123");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isNumeric("abc");
        expected = false;
        Assert.assertFalse(actual);

        actual = StringUtil.isNumeric("");
        expected = false;
        Assert.assertFalse(actual);

        actual = StringUtil.isNumeric("123abc");
        expected = false;
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsWhitespace() {
        for (int codePoint : Arrays.asList(32, 9, 10, 11, 12, 13)) {
            boolean actual = StringUtil.isWhitespace(codePoint);
            boolean expected = true;
            Assert.assertEquals(expected, actual);
        }

        int notWhitespace = 33;
        boolean actual = StringUtil.isWhitespace(notWhitespace);
        boolean expected = false;
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsActuallyWhitespace() {
        for (int codePoint : Arrays.asList(32, 9, 10, 11, 12, 13, 160)) {
            boolean actual = StringUtil.isActuallyWhitespace(codePoint);
            boolean expected = true;
            Assert.assertEquals(expected, actual);
        }

        int notWhitespace = 33;
        boolean actual = StringUtil.isActuallyWhitespace(notWhitespace);
        boolean expected = false;
        Assert.assertFalse(actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String actual = StringUtil.normaliseWhitespace("   hello   worlds   !");
        String expected = "helloworlds!";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String needle = "needle";
        String[] haystack = {"haystack1", needle, "haystack2"};
        boolean actual = StringUtil.in(needle, haystack);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInEmptyHaystack() {
        String needle = "needle";
        String[] haystack = {};
        boolean actual = StringUtil.in(needle, haystack);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSorted() {
        String needle = "needle";
        String[] haystack = {"haystack1", "haystack2", needle};
        boolean actual = StringUtil.inSorted(needle, haystack);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolve() throws Exception {
        String baseUrl = "http://example.com/base";
        String relUrl = "/relative";
        String expected = "http://example.com/relative";
        String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidBaseUrl() throws Exception {
        String baseUrl = "http://example.com/base";
        String relUrl = "";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelUrl() throws Exception {
        String baseUrl = "http://example.com/base";
        String relUrl = "/relative/../..";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test
    public void testResolveRelativeUrlToRoot() throws Exception {
        String baseUrl = "http://example.com/base";
        String relUrl = "/";
        String expected = "http://example.com/";
        String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsoluteUrl() throws Exception {
        String baseUrl = "http://example.com/base";
        String relUrl = "http://example.com/absolute";
        String expected = "http://example.com/absolute";
        String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder actual = StringUtil.stringBuilder();
        StringBuilder expected = new StringBuilder(8 * 1024);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(8 * 1024, actual.capacity());
        String s = actual.append("hello").toString();
        Assert.assertEquals("hello", s);
        Assert.assertEquals("hello", actual.toString());
    }

}