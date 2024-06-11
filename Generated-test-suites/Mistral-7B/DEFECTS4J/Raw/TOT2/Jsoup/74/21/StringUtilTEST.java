package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        strings = Arrays.asList("", "hello", "world", "!");
        expected = "helloworld!";
        result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        try {
            StringUtil.join(null, "").toString();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            StringUtil.join(Arrays.asList("https://www.example.com").iterator(), "").toString();
            Assert.fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 22; i++) {
            String expected = StringUtil.padding(i);
            String result = StringUtil.padding(i);
            Assert.assertEquals(expected, result);
        }

        try {
            StringUtil.padding(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = new String[]{"", "hello", " ", " world", "   ", "   \r\n", " foo\tbar", "12345", "123456"};
        boolean[] expected = new boolean[]{true, false, false, true, true, true, true, false, false};
        for (int i = 0; i < strings.length; i++) {
            boolean result = StringUtil.isBlank(strings[i]);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = new String[]{"123", "1A3", "1.23", "1_2_3", "1234567890", "123.45", ""};
        boolean[] expected = new boolean[]{true, false, true, true, true, true, false};
        for (int i = 0; i < strings.length; i++) {
            boolean result = StringUtil.isNumeric(strings[i]);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsWhitespace() {
        int[] codes = new int[]{32, 9, 10, 11, 12, 13, 33, 127};
        boolean[] expected = new boolean[]{true, true, true, true, true, false, false, false};
        for (int i = 0; i < codes.length; i++) {
            boolean result = StringUtil.isWhitespace(codes[i]);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsActuallyWhitespace() {
        int[] codes = new int[]{32, 9, 10, 11, 12, 13, 33, 160, 127};
        boolean[] expected = new boolean[]{true, true, true, true, true, false, true, true, false};
        for (int i = 0; i < codes.length; i++) {
            boolean result = StringUtil.isActuallyWhitespace(codes[i]);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] inputs = new String[]{"", "\n", "\t\r\f\n", " foo  \t bar ", "foo\nbar"};
        String[] expected = new String[]{"", "\n", "\t\r\f", "foo	bar", "foo\nbar"};
        for (int i = 0; i < inputs.length; i++) {
            String result = StringUtil.normaliseWhitespace(inputs[i]);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIn() {
        String[] haystack = new String[]{"foo", "bar", "baz"};
        String[] needles = new String[]{"foo", "FOO", "bar", "baz", "qux", "quux"};
        boolean[] expected = new boolean[]{true, true, true, false, false, false};
        for (int i = 0; i < needles.length; i++) {
            boolean result = StringUtil.in(needles[i], haystack);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"foo", "bar", "baz"};
        String[] needles = {"baz", "bar", "foo"};
        boolean[] expected = new boolean[]{true, true, true};
        for (int i = 0; i < needles.length; i++) {
            boolean result = StringUtil.inSorted(needles[i], haystack);
            Assert.assertEquals(expected[i], result);
        }
    }

    @Test
    public void testResolve() {
        String baseUrl = "https://www.example.com/";
        String relUrl1 = "about.html";
        String expectedUrl1 = "https://www.example.com/about.html";
        String resultUrl1 = StringUtil.resolve(baseUrl, relUrl1);
        Assert.assertEquals(expectedUrl1, resultUrl1);

        String relUrl2 = "//example.com/index.html";
        String expectedUrl2 = "https://www.example.com/index.html";
        String resultUrl2 = StringUtil.resolve(baseUrl, relUrl2);
        Assert.assertEquals(expectedUrl2, resultUrl2);

        String relUrl3 = "/path/file.html";
        String expectedUrl3 = "https://www.example.com/path/file.html";
        String resultUrl3 = StringUtil.resolve(baseUrl, relUrl3);
        Assert.assertEquals(expectedUrl3, resultUrl3);

        String relUrl4 = "/path/../file.html";
        String expectedUrl4 = "https://www.example.com/file.html";
        String resultUrl4 = StringUtil.resolve(baseUrl, relUrl4);
        Assert.assertEquals(expectedUrl4, resultUrl4);

        String relUrl5 = "https://example.com/index.html";
        String resultUrl5 = StringUtil.resolve(baseUrl, relUrl5);
        Assert.assertEquals(relUrl5, resultUrl5);

        String relUrl6 = "//example.com/index.html?foo=bar";
        String expectedUrl6 = "https://www.example.com/index.html?foo=bar";
        String resultUrl6 = StringUtil.resolve(baseUrl, relUrl6);
        Assert.assertEquals(expectedUrl6, resultUrl6);

        String relUrl7 = "//example.com/index.html?foo=bar#baz";
        String expectedUrl7 = "https://www.example.com/index.html?foo=bar#baz";
        String resultUrl7 = StringUtil.resolve(baseUrl, relUrl7);
        Assert.assertEquals(expectedUrl7, resultUrl7);

        String relUrl8 = "hello";
        String resultUrl8 = StringUtil.resolve(baseUrl, relUrl8);
        Assert.assertEquals("", resultUrl8);

        String relUrl9 = "/hello?";
        String resultUrl9 = StringUtil.resolve(baseUrl, relUrl9);
        Assert.assertEquals("", resultUrl9);

        String relUrl10 = "..";
        String resultUrl10 = StringUtil.resolve(baseUrl, relUrl10);
        Assert.assertEquals("", resultUrl10);
    }
}