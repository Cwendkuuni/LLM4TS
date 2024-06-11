package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String sep = ",";
        String expected = "Hello,World,!";
        Assert.assertEquals(expected, StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();
        String sep = ",";
        String expected = "Hello,World,!";
        Assert.assertEquals(expected, StringUtil.join(iterator, sep));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String sep = ",";
        String expected = "Hello,World,!";
        Assert.assertEquals(expected, StringUtil.join(strings, sep));
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        Assert.assertEquals(expected, StringUtil.padding(width));
    }

    @Test
    public void testIsBlank() {
        String string1 = null;
        String string2 = "";
        String string3 = " ";
        String string4 = "Hello";
        Assert.assertTrue(StringUtil.isBlank(string1));
        Assert.assertTrue(StringUtil.isBlank(string2));
        Assert.assertTrue(StringUtil.isBlank(string3));
        Assert.assertFalse(StringUtil.isBlank(string4));
    }

    @Test
    public void testIsNumeric() {
        String string1 = null;
        String string2 = "";
        String string3 = "123";
        String string4 = "Hello";
        Assert.assertFalse(StringUtil.isNumeric(string1));
        Assert.assertFalse(StringUtil.isNumeric(string2));
        Assert.assertTrue(StringUtil.isNumeric(string3));
        Assert.assertFalse(StringUtil.isNumeric(string4));
    }

    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        int c6 = 160;
        int c7 = 'A';
        Assert.assertTrue(StringUtil.isWhitespace(c1));
        Assert.assertTrue(StringUtil.isWhitespace(c2));
        Assert.assertTrue(StringUtil.isWhitespace(c3));
        Assert.assertTrue(StringUtil.isWhitespace(c4));
        Assert.assertTrue(StringUtil.isWhitespace(c5));
        Assert.assertTrue(StringUtil.isWhitespace(c6));
        Assert.assertFalse(StringUtil.isWhitespace(c7));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        int c6 = 160;
        int c7 = 'A';
        Assert.assertTrue(StringUtil.isActuallyWhitespace(c1));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(c2));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(c3));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(c4));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(c5));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(c6));
        Assert.assertFalse(StringUtil.isActuallyWhitespace(c7));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   Hello   World  !   ";
        String expected = "Hello World !";
        Assert.assertEquals(expected, StringUtil.normaliseWhitespace(string));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        String string = "   Hello   World  !   ";
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, string, true);
        String expected = "Hello World !";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testIn() {
        String needle1 = "Hello";
        String needle2 = "World";
        String needle3 = "!";
        String needle4 = "Goodbye";
        String[] haystack = {"Hello", "World", "!"};
        Assert.assertTrue(StringUtil.in(needle1, haystack));
        Assert.assertTrue(StringUtil.in(needle2, haystack));
        Assert.assertTrue(StringUtil.in(needle3, haystack));
        Assert.assertFalse(StringUtil.in(needle4, haystack));
    }

    @Test
    public void testInSorted() {
        String needle1 = "Hello";
        String needle2 = "World";
        String needle3 = "!";
        String needle4 = "Goodbye";
        String[] haystack = {"!", "Hello", "World"};
        Assert.assertTrue(StringUtil.inSorted(needle1, haystack));
        Assert.assertTrue(StringUtil.inSorted(needle2, haystack));
        Assert.assertTrue(StringUtil.inSorted(needle3, haystack));
        Assert.assertFalse(StringUtil.inSorted(needle4, haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/path/file.html";
        String expected = "https://www.example.com/path/file.html";
        Assert.assertEquals(expected, StringUtil.resolve(base, relUrl).toExternalForm());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/path/file.html";
        String expected = "https://www.example.com/path/file.html";
        Assert.assertEquals(expected, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        Assert.assertEquals(0, sb.length());
    }
}