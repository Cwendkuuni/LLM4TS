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
        List<String> strings = Arrays.asList("hello", "world", "!", "test");
        String expected = "helloworld!test";
        String actual = join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String actual = join(strings, "");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testJoinIteratorOneElement() {
        Iterator<String> strings = Arrays.asList("hello").iterator();
        String actual = join(strings, "");
        String expected = "hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorMultipleElements() {
        List<String> strings = Arrays.asList("hello", "world", "!", "test");
        String expected = "helloworld!test";
        String actual = join(strings.iterator(), "");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinWidthNegative() {
        String actual = padding(-1);
    }

    @Test
    public void testPaddingWithZero() {
        String actual = padding(0);
        Assert.assertEquals("", actual);
    }

    @Test
    public void testPaddingWithLengthLessThanPaddingArraySize() {
        String actual = padding(5);
        Assert.assertEquals("   ", actual);
    }

    @Test
    public void testPaddingWithLengthGreaterThanPaddingArraySize() {
        String actual = padding(22);
        char[] expectedChars = new char[22];
        for (int i = 0; i < 22; i++) {
            expectedChars[i] = ' ';
        }
        String expected = new String(expectedChars);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNull() {
        boolean actual = isBlank(null);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsBlankEmpty() {
        String actual = "";
        boolean expected = true;
        Assert.assertEquals(expected, isBlank(actual));
    }

    @Test
    public void testIsBlankWhitespace() {
        String actual = "   ";
        boolean expected = true;
        Assert.assertEquals(expected, isBlank(actual));
    }

    @Test
    public void testIsBlankNonBlank() {
        String actual = "Hello World";
        boolean expected = false;
        Assert.assertEquals(expected, isBlank(actual));
    }

    @Test
    public void testIsNumericTrue() {
        String actual = "123";
        boolean expected = true;
        Assert.assertEquals(expected, isNumeric(actual));
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String actual = "";
        boolean expected = false;
        Assert.assertEquals(expected, isNumeric(actual));
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String actual = "abc";
        boolean expected = false;
        Assert.assertEquals(expected, isNumeric(actual));
    }

    @Test
    public void testIsWhitespaceSpace() {
        int c = ' ';
        boolean expected = true;
        Assert.assertEquals(expected, isWhitespace(c));
    }

    @Test
    public void testIsWhitespaceTab() {
        int c = '\t';
        boolean expected = true;
        Assert.assertEquals(expected, isWhitespace(c));
    }

    @Test
    public void testIsWhitespaceNewline() {
        int c = '\n';
        boolean expected = true;
        Assert.assertEquals(expected, isWhitespace(c));
    }

    @Test
    public void testIsWhitespaceOther() {
        int c = 'x';
        boolean expected = false;
        Assert.assertEquals(expected, isWhitespace(c));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseWhitespaceWidthNegative() {
        normaliseWhitespace(-1);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String expected = "";
        String actual = join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayOneElement() {
        String[] strings = {"hello"};
        String expected = "hello";
        String actual = join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayMultipleElements() {
        String[] strings = {"hello", "world", "!", "test"};
        String expected = "helloworld!test";
        String actual = join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String needle = "world";
        String[] haystack = {"hello", "world", "!", "test"};
        boolean expected = true;
        boolean actual = in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNoMatch() {
        String needle = "foo";
        String[] haystack = {"hello", "world", "!", "test"};
        boolean expected = false;
        boolean actual = in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSorted() {
        String needle = "world";
        String[] haystack = {"test", "world", "hello"};
        boolean expected = true;
        boolean actual = inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseNull() {
        String baseUrl = null;
        String relUrl = "test";
        String expected = "";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseSimple() {
        String baseUrl = "http://example.com/";
        String relUrl = "test";
        String expected = "http://example.com/test";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseWithQuery() {
        String baseUrl = "http://example.com/test?param=value";
        String relUrl = "test";
        String expected = "http://example.com/test";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseWithFragment() {
        String baseUrl = "http://example.com/test#fragment";
        String relUrl = "test";
        String expected = "http://example.com/test";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseWithTrailingSlash() {
        String baseUrl = "http://example.com/";
        String relUrl = "test";
        String expected = "http://example.com/test";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseWithTrailingSlashRelativePath() {
        String baseUrl = "http://example.com/";
        String relUrl = "/test";
        String expected = "http://example.com/test";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseWithRelativePath() {
        String baseUrl = "http://example.com/test";
        String relUrl = "relative";
        String expected = "http://example.com/test/relative";
        String actual = resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseRelativeUrlWithProtocol() {
        String baseUrl = "http://example.com/test";
        String relUrl = "//example2.com/relative";
        resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseWithRelativeUrlWithTrailingSlash() {
        String baseUrl = "http://example.com/test";
        String relUrl = "/relative/";
        resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseWithRelativeUrlWithTrailingSlashAndFragment() {
        String baseUrl = "http://example.com/test";
        String relUrl = "/relative/#fragment";
        resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseWithRelativeUrlWithQuery() {
        String baseUrl = "http://example.com/test";
        String relUrl = "/relative?param=value";
        resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseWithRelativeUrlWithFragmentQuery() {
        String baseUrl = "http://example.com/test";
        String relUrl = "/relative#param=value";
        resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseWithRelativeUrlWithDoubleSlash() {
        String baseUrl = "http://example.com/test";
        String relUrl = "//example2.com/relative";
        resolve(baseUrl, relUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = stringBuilder();
        Assert.assertEquals(0, sb.length());
    }
}