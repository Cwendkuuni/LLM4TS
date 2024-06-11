package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("test1");
        strings.add("test2");
        strings.add("test3");

        String expected = "test1 test2 test3";
        String actual = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = new ArrayList<>();
        String actual = StringUtil.join(strings, " ");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("test1");
        strings.add("test2");
        strings.add("test3");
        Iterator<String> iterator = strings.iterator();

        String expected = "test1 test2 test3";
        String actual = StringUtil.join(iterator, " ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"test1", "test2", "test3"};

        String expected = "test1 test2 test3";
        String actual = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String actual = StringUtil.padding(-1);
    }

    @Test
    public void testPadding() {
        String actual = StringUtil.padding(5);
        String expected = "     ";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNull() {
        String string = null;
        boolean actual = StringUtil.isBlank(string);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankEmpty() {
        String string = "";
        boolean actual = StringUtil.isBlank(string);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankWhitespace() {
        String string = "   ";
        boolean actual = StringUtil.isBlank(string);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNonWhitespace() {
        String string = "test";
        boolean actual = StringUtil.isBlank(string);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericTrue() {
        String string = "123";
        boolean actual = StringUtil.isNumeric(string);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericFalse() {
        String string = "test";
        boolean actual = StringUtil.isNumeric(string);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTrue() {
        char codePoint = ' ';
        boolean actual = StringUtil.isWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceFalse() {
        char codePoint = 'a';
        boolean actual = StringUtil.isWhitespace(codePoint);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrue() {
        char codePoint = ' ';
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceFalse() {
        char codePoint = 'a';
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String actual = StringUtil.normaliseWhitespace(" test test \t \n test");
        String expected = "test test test";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String needle = "test";
        String[] haystack = {"test1", "test2", "test3", "test"};

        boolean actual = StringUtil.in(needle, haystack);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInFalse() {
        String needle = "test4";
        String[] haystack = {"test1", "test2", "test3", "test"};

        boolean actual = StringUtil.in(needle, haystack);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSortedTrue() {
        String needle = "test";
        String[] haystack = {"test", "test1", "test2", "test3"};

        boolean actual = StringUtil.inSorted(needle, haystack);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSortedFalse() {
        String needle = "test4";
        String[] haystack = {"test", "test1", "test2", "test3"};

        boolean actual = StringUtil.inSorted(needle, haystack);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolve() throws Exception {
        String baseUrl = "http://www.example.com";
        String relUrl = "/test.html";

        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://www.example.com/test.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveMalformedUrl() throws Exception {
        String baseUrl = "http://www.example.com";
        String relUrl = "//test.html";

        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveNullBaseUrl() throws Exception {
        String relUrl = "http://test.html";

        String actual = StringUtil.resolve(null, relUrl);
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveNullRelUrl() throws Exception {
        String baseUrl = "http://www.example.com";

        String actual = StringUtil.resolve(baseUrl, null);
        String expected = baseUrl;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder actual = StringUtil.stringBuilder();
        String expected = "";
        Assert.assertEquals(expected, actual.toString());
    }

    @Test
    public void testStringBuilderCapacity() {
        StringBuilder actual = StringUtil.stringBuilder();
        actual.append("test");

        int expected = StringUtil.MaxCachedBuilderSize;
        int actualCapacity = actual.capacity();
        Assert.assertEquals(expected, actualCapacity);
    }

}