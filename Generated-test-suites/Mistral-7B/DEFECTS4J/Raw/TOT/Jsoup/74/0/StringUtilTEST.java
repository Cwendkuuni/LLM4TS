package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!", " ");
        String expected = "hello world ! ";
        String actual = StringUtil.join(strings.iterator(), " ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinCollectionEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String actual = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinCollectionNullFirst() {
        Iterator<String> strings = Arrays.asList(null, "world", "!", " ").iterator();
        String actual = StringUtil.join(strings, " ");
        String expected = "world ! ";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world", "!", " "};
        String expected = "hello world ! ";
        String actual = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String actual = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        String actual = StringUtil.padding(5);
        String expected = "     ";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPaddingNegative() {
        Assert.throwsException(IllegalArgumentException.class, () -> StringUtil.padding(-5));
    }

    @Test
    public void testIsBlank() {
        String empty = "";
        String blank = "   ";
        String notBlank = "hello";
        Assert.assertTrue(StringUtil.isBlank(empty));
        Assert.assertTrue(StringUtil.isBlank(blank));
        Assert.assertFalse(StringUtil.isBlank(notBlank));
    }

    @Test
    public void testIsNumeric() {
        String numeric = "12345";
        String nonNumeric = "hello";
        String nonNumericWithDigits = "hello123";
        Assert.assertTrue(StringUtil.isNumeric(numeric));
        Assert.assertFalse(StringUtil.isNumeric(nonNumeric));
        Assert.assertFalse(StringUtil.isNumeric(nonNumericWithDigits));
    }

    @Test
    public void testIsWhitespace() {
        char space = ' ';
        char tab = '\t';
        char newline = '\n';
        char formFeed = '\f';
        char carriageReturn = '\r';
        Assert.assertTrue(StringUtil.isWhitespace(space));
        Assert.assertTrue(StringUtil.isWhitespace(tab));
        Assert.assertTrue(StringUtil.isWhitespace(newline));
        Assert.assertTrue(StringUtil.isWhitespace(formFeed));
        Assert.assertTrue(StringUtil.isWhitespace(carriageReturn));
    }

    @Test
    public void testIsActuallyWhitespace() {
        char space = ' ';
        char tab = '\t';
        char newline = '\n';
        char formFeed = '\f';
        char carriageReturn = '\r';
        char nonBreakingSpace = '\u00A0';
        Assert.assertTrue(StringUtil.isActuallyWhitespace(space));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(tab));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(newline));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(formFeed));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(carriageReturn));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));
    }

    @Test
    public void testNormaliseWhitespace() {
        String multiWhiteSpace = "hello \t world \n";
        String noWhiteSpace = "hello world";
        String nonBreakingSpace = "hello \u00A0 world";
        String expected = "hello world";
        String actual = StringUtil.normaliseWhitespace(multiWhiteSpace);
        Assert.assertEquals(expected, actual);
        expected = noWhiteSpace;
        actual = StringUtil.normaliseWhitespace(noWhiteSpace);
        Assert.assertEquals(expected, actual);
        expected = expected + "\u00A0";
        actual = StringUtil.normaliseWhitespace(nonBreakingSpace);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String needle = "hello";
        String[] haystack = {"hello world", "goodbye", "hello"};
        boolean expected = true;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNull() {
        String needle = null;
        String[] haystack = {"hello world", "goodbye"};
        boolean expected = false;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInEmpty() {
        String needle = "hello";
        String[] haystack = {};
        boolean expected = false;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSorted() {
        String needle = "hello";
        String[] haystack = {"hello world", "goodbye", "hello"};
        boolean expected = true;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSortedEmpty() {
        String needle = "hello";
        String[] haystack = {};
        boolean expected = false;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "/about.html";
        String expected = "http://example.com/about.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveNullBaseUrl() throws MalformedURLException {
        String relUrl = "/about.html";
        String expected = "";
        String actual = StringUtil.resolve(null, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveNullRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String expected = "";
        String actual = StringUtil.resolve(baseUrl, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveMalformedUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "//about.html";
        String expected = "";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveEmptyBaseUrl() throws MalformedURLException {
        String relUrl = "/about.html";
        String expected = "/about.html";
        String actual = StringUtil.resolve("", relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "http://example.com/about.html";
        String expected = relUrl;
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }
}