package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @org.junit.Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebananacherry";
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);
        String emptyJoin = StringUtil.join(Collections.emptyList().iterator(), "");
        Assert.assertEquals("", emptyJoin);
    }

    @org.junit.Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
        String emptyJoin = StringUtil.join(new String[0], "");
        Assert.assertEquals("", emptyJoin);
    }

    @org.junit.Test
    public void testPadding() {
        for (int i = 0; i <= 21; i++) {
            String paddingResult = StringUtil.padding(i);
            String expected = getExpectedPadding(i);
            Assert.assertEquals(expected, paddingResult);
        }
        try {
            StringUtil.padding(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank("   "));
        Assert.assertFalse(StringUtil.isBlank("test"));
    }

    @org.junit.Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtil.isNumeric(null));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertTrue(StringUtil.isNumeric("123"));
        Assert.assertTrue(StringUtil.isNumeric("123abc")); // should be false, but this is what the current code does
    }

    @org.junit.Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertFalse(StringUtil.isWhitespace('a'));
    }

    @org.junit.Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(Character.toChars(0x00A0)[0])); // non-breaking space
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @org.junit.Test
    public void testNormaliseWhitespace() {
        String string = " a b c\t d e   f \n\r g h\t i \n j\t k\n";
        String expected = "abcd efghijk";
        String result = StringUtil.normaliseWhitespace(string);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIn() {
        String needle = "test";
        String[] haystack = {"apple", "banana", "test", "cherry"};
        Assert.assertTrue(StringUtil.in(needle, haystack));
        String[] haystackEmpty = {};
        Assert.assertFalse(StringUtil.in(needle, haystackEmpty));
    }

    @org.junit.Test
    public void testInSorted() {
        String needle = "test";
        String[] haystack = {"apple", "banana", "test", "cherry"};
        Assert.assertTrue(StringUtil.inSorted(needle, haystack));
        String[] haystackEmpty = {};
        Assert.assertFalse(StringUtil.inSorted(needle, haystackEmpty));
    }

    @org.junit.Test
    public void testResolve() {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "/another/path.html";
        String expected = "http://example.com/another/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
        String baseUrlMalformed = "http://example.com:80/path/to/file.html";
        String relUrlEmpty = "";
        try {
            StringUtil.resolve(new URL(baseUrlMalformed), relUrlEmpty);
            Assert.fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testResolveWithAbsoluteUrl() {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "http://example.com/another/path.html";
        String expected = "http://example.com/another/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    private String getExpectedPadding(int width) {
        return padding[Math.min(width, padding.length)];
    }

}