package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.jsoup.helper.StringUtil.join;
import static org.jsoup.helper.StringUtil.isActuallyWhitespace;
import static org.jsoup.helper.StringUtil.isBlank;
import static org.jsoup.helper.StringUtil.isNumeric;
import static org.jsoup.helper.StringUtil.padding;
import static org.jsoup.helper.StringUtil.padding;
import static org.jsoup.helper.StringUtil.normaliseWhitespace;
import static org.jsoup.helper.StringUtil.in;
import static org.jsoup.helper.StringUtil.resolve;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("hello", "world", "!");
        String expected = "hello world !";
        String result = join(list, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> list = Collections.emptyList();
        String result = join(list, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> list = Arrays.asList("hello", "world", "!");
        String expected = "hello world !";
        Iterator<String> iterator = list.iterator();
        String result = join(iterator, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyIterator() {
        Iterator<String> iterator = Collections.emptyIterator();
        String result = join(iterator, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "!"};
        String expected = "hello world !";
        String result = join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyArray() {
        String[] strings = new String[0];
        String result = join(strings, "");
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String result = padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        boolean result = StringUtil.isBlank(null);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankEmpty() {
        boolean result = StringUtil.isBlank("");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespace() {
        boolean result = StringUtil.isBlank("   ");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankNonWhitespace() {
        boolean result = StringUtil.isBlank("hello");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericEmpty() {
        boolean result = StringUtil.isNumeric("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericAllDigits() {
        boolean result = StringUtil.isNumeric("123456");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNumericNonDigit() {
        boolean result = StringUtil.isNumeric("123a56");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean result = StringUtil.isWhitespace('\t');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceNewline() {
        boolean result = StringUtil.isWhitespace('\n');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceOther() {
        boolean result = StringUtil.isWhitespace('x');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        boolean result = StringUtil.isActuallyWhitespace(160);
        Assert.assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespaceNoChange() {
        String input = "   hello   world   ";
        String expected = "   hello   world   ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceCollapsesMultipleSpaces() {
        String input = "   hello   world   ";
        String expected = "   hello world   ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceRemovesLeadingWhitespace() {
        String input = "   hello   world   ";
        String expected = "hello world   ";
        String result = StringUtil.normaliseWhitespace(input, true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceRemovesTrailingWhitespace() {
        String input = "   hello   world   ";
        String expected = "   hello world";
        String result = StringUtil.normaliseWhitespace(input, false, true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceRemovesLeadingAndTrailingWhitespace() {
        String input = "   hello   world   ";
        String expected = "hello world";
        String result = StringUtil.normaliseWhitespace(input, true, true);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInNullNeedle() {
        String[] haystack = new String[]{"hello", "world"};
        String result = StringUtil.in(null, haystack);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInNullHaystack() {
        String needle = "hello";
        String[] result = StringUtil.in(needle, null);
    }

    @Test
    public void testInExactMatch() {
        String needle = "hello";
        String[] haystack = new String[]{"hello", "world"};
        String[] result = StringUtil.in(needle, haystack);
        Assert.assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testInPartialMatch() {
        String needle = "ell";
        String[] haystack = new String[]{"hello", "world"};
        String[] result = StringUtil.in(needle, haystack);
        Assert.assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testInNoMatch() {
        String needle = "foo";
        String[] haystack = new String[]{"hello", "world"};
        String[] result = StringUtil.in(needle, haystack);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testResolveBaseNull() {
        String baseUrl = null;
        String relUrl = "test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveBaseEmpty() {
        String baseUrl = "";
        String relUrl = "test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testResolveBaseAbsolute() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path/to/file/test", result);
    }

    @Test
    public void testResolveRelativeNull() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = null;
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveRelativeEmpty() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path/to/file", result);
    }

    @Test
    public void testResolveRelativeAbsolute() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "/path/to/other";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path/to/other", result);
    }

    @Test
    public void testResolveRelativeRelative() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "path/to/other";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path/to/other", result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveRelativeStringWithProtocol() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "http://example.com/path/to/other";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();

        sb1.append("hello");
        sb2.append("world");
        sb3.append("!");

        StringBuilder sb = StringUtil.stringBuilder();
        sb.append(sb1);
        sb.append(sb2);
        sb.append(sb3);

        String expected = "helloworld!";
        String result = sb.toString();
        Assert.assertEquals(expected, result);

        String result2 = sb1.toString();
        Assert.assertEquals("hello", result2);
    }

}