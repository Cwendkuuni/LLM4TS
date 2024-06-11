package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        String result = StringUtil.join(strings, ",");
        Assert.assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, ",");
        Assert.assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"one", "two", "three"};
        String result = StringUtil.join(strings, ",");
        Assert.assertEquals("one,two,three", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        Assert.assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        boolean result = StringUtil.isBlank(null);
        Assert.assertTrue(result);

        result = StringUtil.isBlank("");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("   ");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("not blank");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumeric() {
        boolean result = StringUtil.isNumeric(null);
        Assert.assertFalse(result);

        result = StringUtil.isNumeric("");
        Assert.assertFalse(result);

        result = StringUtil.isNumeric("123");
        Assert.assertTrue(result);

        result = StringUtil.isNumeric("123a");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\t');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\n');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\f');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\r');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace(160);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\f');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\r');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace(160);
        Assert.assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("  hello   world  ");
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  hello   world  ", true);
        Assert.assertEquals("hello world", sb.toString());
    }

    @Test
    public void testIn() {
        String needle = "one";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertTrue(result);

        needle = "four";
        result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSorted() {
        String needle = "one";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(result);

        needle = "four";
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "page.html";
        URL result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals("http://www.example.com/page.html", result.toString());
    }

    @Test
    public void testResolveInvalid() {
        String baseUrl = "http://www.example.com";
        String relUrl = "page.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        sb1.append("Hello");
        StringBuilder sb2 = StringUtil.stringBuilder();
        Assert.assertEquals(0, sb2.length());
    }
}