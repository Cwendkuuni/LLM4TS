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
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebananacherry";
        String actual = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, actual);

        strings = Collections.singletonList("");
        expected = "";
        actual = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, actual);

        strings = Collections.emptyList();
        expected = "";
        actual = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);

        strings = new String[0];
        expected = "";
        actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= 21; i++) {
            String expected = padding(i);
            String actual = StringUtil.padding(i);
            Assert.assertEquals(expected, actual);
        }

        try {
            StringUtil.padding(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsBlankNull() {
        StringUtil.isBlank(null);
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
        String string = "abc";
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
    public void testIsNumericFalseEmpty() {
        String string = "";
        boolean actual = StringUtil.isNumeric(string);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String string = "abcd";
        boolean actual = StringUtil.isNumeric(string);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean actual = StringUtil.isWhitespace(' ');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean actual = StringUtil.isWhitespace('\t');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceLf() {
        boolean actual = StringUtil.isWhitespace('\n');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceCr() {
        boolean actual = StringUtil.isWhitespace('\r');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceNbsp() {
        boolean actual = StringUtil.isWhitespace('\u00A0');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean actual = StringUtil.isActuallyWhitespace(' ');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean actual = StringUtil.isActuallyWhitespace('\t');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceLf() {
        boolean actual = StringUtil.isActuallyWhitespace('\n');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceCr() {
        boolean actual = StringUtil.isActuallyWhitespace('\r');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceNbsp() {
        boolean actual = StringUtil.isActuallyWhitespace('\u00A0');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceControlCharacter() {
        boolean actual = StringUtil.isActuallyWhitespace('\u0007');
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string1 = "   apple   ";
        String expected1 = "apple";
        String actual1 = StringUtil.normaliseWhitespace(string1);
        Assert.assertEquals(expected1, actual1);

        String string2 = "\tapple\t\n";
        String expected2 = "apple";
        String actual2 = StringUtil.normaliseWhitespace(string2);
        Assert.assertEquals(expected2, actual2);

        String string3 = "apple\n   banana\r\t\ncherry";
        String expected3 = "apple banana cherry";
        String actual3 = StringUtil.normaliseWhitespace(string3);
        Assert.assertEquals(expected3, actual3);
    }

    @Test
    public void testInEmptyCollection() {
        boolean actual = StringUtil.in("needle", new String[0]);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInStringArray() {
        boolean actual = StringUtil.in("needle", new String[]{"apple", "banana", "cherry"});
        boolean expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.in("banana", new String[]{"apple", "banana", "cherry"});
        expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        boolean actual = StringUtil.in("needle", strings);
        boolean expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.in("banana", strings);
        expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveEmptyURL() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsoluteURL() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl = "";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = baseUrl;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelativeURL() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl = "about.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "https://example.com/about.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsoluteURLWithPath() throws MalformedURLException {
        String baseUrl = "https://example.com/index.html";
        String relUrl = "./contact.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "https://example.com/contact.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveURLWithFragment() throws MalformedURLException {
        String baseUrl = "https://example.com/index.html";
        String relUrl = "#about";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = baseUrl + relUrl;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveURLWithQueryString() throws MalformedURLException {
        String baseUrl = "https://example.com/index.html?foo=bar";
        String relUrl = "?baz=qux";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = baseUrl + relUrl;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveURLWithQueryStringAndFragment() throws MalformedURLException {
        String baseUrl = "https://example.com/index.html?foo=bar";
        String relUrl = "?baz=qux#quux";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = baseUrl + relUrl;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidURL() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl = "://qux.com";
        String actual = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test
    public void testBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        String expected = "hello";
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);

        sb = StringUtil.stringBuilder();
        sb.append("hello");
        sb.append("world");
        expected = "helloworld";
        actual = sb.toString();
        Assert.assertEquals(expected, actual);

        sb = StringUtil.stringBuilder();
        sb.append("hello");
        sb.append(" ");
        sb.append("world");
        expected = "helloworld";
        actual = sb.toString();
        Assert.assertEquals(expected, actual);

        sb = StringUtil.stringBuilder();
        sb.append("hello");
        sb.append(" ");
        sb.append("world");
        sb.delete(0, 1);
        expected = "hello world";
        actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuilderGrow() {
        StringBuilder sb = StringUtil.stringBuilder();
        for (int i = 0; i < 16000; i++) {
            sb.append("a");
        }
        String expected = "aaa..."; // 16000 'a's
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }
}