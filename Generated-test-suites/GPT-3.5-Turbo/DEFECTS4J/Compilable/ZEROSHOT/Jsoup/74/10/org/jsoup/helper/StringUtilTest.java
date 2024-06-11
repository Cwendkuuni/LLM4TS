package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        strings.add("ghi");
        String joinedString = StringUtil.join(strings, ",");
        Assert.assertEquals("abc,def,ghi", joinedString);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        strings.add("ghi");
        Iterator<String> iterator = strings.iterator();
        String joinedString = StringUtil.join(iterator, ",");
        Assert.assertEquals("abc,def,ghi", joinedString);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"abc", "def", "ghi"};
        String joinedString = StringUtil.join(strings, ",");
        Assert.assertEquals("abc,def,ghi", joinedString);
    }

    @Test
    public void testPadding() {
        Assert.assertEquals("", StringUtil.padding(0));
        Assert.assertEquals(" ", StringUtil.padding(1));
        Assert.assertEquals("  ", StringUtil.padding(2));
        Assert.assertEquals("            ", StringUtil.padding(12));
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank(" "));
        Assert.assertTrue(StringUtil.isBlank("\n\t"));
        Assert.assertFalse(StringUtil.isBlank("abc"));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtil.isNumeric(null));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric("abc"));
        Assert.assertTrue(StringUtil.isNumeric("123"));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\f'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertFalse(StringUtil.isWhitespace('a'));
        Assert.assertFalse(StringUtil.isWhitespace('6'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace((char) 160));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('6'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "  hello\n\tworld  ";
        String output = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals("hello world", output);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  hello\n\tworld  ", true);
        Assert.assertEquals("hello world", sb.toString());
    }

    @Test
    public void testIn() {
        Assert.assertTrue(StringUtil.in("abc", "abc", "def", "ghi"));
        Assert.assertTrue(StringUtil.in("def", "abc", "def", "ghi"));
        Assert.assertTrue(StringUtil.in("ghi", "abc", "def", "ghi"));
        Assert.assertFalse(StringUtil.in("xyz", "abc", "def", "ghi"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"abc", "def", "ghi"};
        Assert.assertTrue(StringUtil.inSorted("abc", haystack));
        Assert.assertTrue(StringUtil.inSorted("def", haystack));
        Assert.assertTrue(StringUtil.inSorted("ghi", haystack));
        Assert.assertFalse(StringUtil.inSorted("xyz", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/foo/");
        String relUrl = "../bar";
        URL resolvedUrl = StringUtil.resolve(base, relUrl);
        Assert.assertEquals("http://example.com/bar", resolvedUrl.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com/foo/";
        String relUrl = "../bar";
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/bar", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        Assert.assertNotSame(sb1, sb2);
        Assert.assertEquals(0, sb1.length());
        Assert.assertEquals(0, sb2.length());
    }

}