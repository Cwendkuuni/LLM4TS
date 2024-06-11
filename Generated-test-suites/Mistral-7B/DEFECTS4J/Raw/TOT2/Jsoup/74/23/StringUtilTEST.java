package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "test");
        String expected = "helloworldtest";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> iterator = Collections.emptyIterator();
        String result = StringUtil.join(iterator, "");
        String expected = "";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorOneElement() {
        Iterator<String> iterator = Arrays.asList("test").iterator();
        String result = StringUtil.join(iterator, "");
        String expected = "test";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world", "test"};
        String expected = "helloworldtest";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPaddingNegativeWidth() {
        Assert.assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1));
    }

    @Test
    public void testPaddingZeroWidth() {
        String expected = "";
        String result = StringUtil.padding(0);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPaddingValidWidth() {
        String expected = "               ";
        String result = StringUtil.padding(5);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankNull() {
        boolean expected = true;
        boolean result = StringUtil.isBlank(null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankEmpty() {
        boolean expected = true;
        boolean result = StringUtil.isBlank("");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWhitespace() {
        boolean expected = true;
        boolean result = StringUtil.isBlank("   ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankNonWhitespace() {
        boolean expected = false;
        boolean result = StringUtil.isBlank("test");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericEmpty() {
        boolean expected = false;
        boolean result = StringUtil.isNumeric("");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericDigits() {
        boolean expected = true;
        boolean result = StringUtil.isNumeric("12345");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericLetters() {
        boolean expected = false;
        boolean result = StringUtil.isNumeric("abc");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean expected = true;
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean expected = true;
        boolean result = StringUtil.isWhitespace('\t');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceNewline() {
        boolean expected = true;
        boolean result = StringUtil.isWhitespace('\n');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespaceNonWhitespace() {
        boolean expected = false;
        boolean result = StringUtil.isActuallyWhitespace('A');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(" ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String expected = " ";
        String result = StringUtil.normaliseWhitespace("   ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceTab() {
        String expected = " ";
        String result = StringUtil.normaliseWhitespace("\t");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceNewline() {
        String expected = " ";
        String result = StringUtil.normaliseWhitespace("\n");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String expected = "  \t \n ";
        String result = StringUtil.normaliseWhitespace("   \t\n ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        boolean expected = true;
        String haystack = "hello, world, test";
        String needle = "test";
        boolean result = StringUtil.in(needle, haystack.split(", "));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInNoMatch() {
        boolean expected = false;
        String haystack = "hello, world, test";
        String needle = "foo";
        boolean result = StringUtil.in(needle, haystack.split(", "));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInNullNeedle() {
        boolean expected = false;
        String haystack = "hello, world, test";
        boolean result = StringUtil.in(null, haystack.split(", "));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSortedNullNeedle() {
        String[] haystack = {"hello", "world", "test"};
        boolean expected = false;
        boolean result = StringUtil.inSorted(null, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"hello", "world", "test"};
        String needle = "test";
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSortedNoMatch() {
        String[] haystack = {"hello", "world", "test"};
        String needle = "foo";
        boolean expected = false;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBase() throws MalformedURLException {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = "/relative/path.html";
        String expected = "http://www.example.com/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBaseInvalidRelative() throws MalformedURLException {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = "foo:bar";
        String result = StringUtil.resolve(baseUrl, relUrl);
        String expected = "";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBaseEmptyBase() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "/path/to/file.html";
        String expected = "/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBaseInvalidRelative2() throws MalformedURLException {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = "";
        String expected = "http://www.example.com/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBaseAbsoluteRelative() throws MalformedURLException {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = "http://www.example.com/relative/path.html";
        String expected = "http://www.example.com/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBaseNullBase() throws MalformedURLException {
        String baseUrl = null;
        String relUrl = "/path/to/file.html";
        String expected = "/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteBaseNullRelative() throws MalformedURLException {
        String baseUrl = "http://www.example.com/path/to/file.html";
        String relUrl = null;
        String expected = "http://www.example.com/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringBuilderCache() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        sb1.append("hello");
        sb2.append("world");

        StringBuilder sb3 = StringUtil.stringBuilder();
        sb3.append(sb1);
        sb3.append(sb2);

        String expected = "helloworld";
        String result = sb3.toString();
        Assert.assertEquals(expected, result);
    }
}