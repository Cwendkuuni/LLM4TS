package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("Hello", "World", "!!!");
        String expected = "HelloWorld!!!";
        String actual = StringUtil.join(strings.iterator(), " ");
        assertEquals(expected, actual);

        strings = Collections.singletonList("");
        expected = "";
        actual = StringUtil.join(strings.iterator(), " ");
        assertEquals(expected, actual);

        strings = Collections.emptyList();
        expected = "";
        actual = StringUtil.join(strings.iterator(), " ");
        assertEquals(expected, actual);

        strings = Arrays.asList("", "Hello", "World");
        expected = "Hello World";
        actual = StringUtil.join(strings.iterator(), " ");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinWithNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testPadding() {
        String expected = "    Hello";
        String actual = StringUtil.padding(5) + "Hello";
        assertEquals(expected, actual);

        expected = "     ";
        actual = StringUtil.padding(0);
        assertEquals(expected, actual);

        expected = "";
        actual = StringUtil.padding(0) + "";
        assertEquals(expected, actual);

        expected = "   Hello";
        actual = StringUtil.padding(3) + "Hello";
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlank() {
        String string1 = null;
        Assert.assertTrue(StringUtil.isBlank(string1));

        String string2 = "";
        Assert.assertTrue(StringUtil.isBlank(string2));

        String string3 = "   ";
        Assert.assertTrue(StringUtil.isBlank(string3));

        String string4 = " Hello";
        Assert.assertFalse(StringUtil.isBlank(string4));

        String string5 = "Hello ";
        Assert.assertFalse(StringUtil.isBlank(string5));

        String string6 = " Hello";
        Assert.assertFalse(StringUtil.isBlank(string6));

        String string7 = "Hello";
        Assert.assertFalse(StringUtil.isBlank(string7));

        String string8 = "Hello World";
        Assert.assertFalse(StringUtil.isBlank(string8));
    }

    @Test
    public void testIsNumeric() {
        String string1 = null;
        Assert.assertFalse(StringUtil.isNumeric(string1));

        String string2 = "";
        Assert.assertFalse(StringUtil.isNumeric(string2));

        String string3 = "12345";
        Assert.assertTrue(StringUtil.isNumeric(string3));

        String string4 = "123";
        Assert.assertTrue(StringUtil.isNumeric(string4));

        String string5 = "123a";
        Assert.assertFalse(StringUtil.isNumeric(string5));

        String string6 = "-123";
        Assert.assertTrue(StringUtil.isNumeric(string6));

        String string7 = "+123";
        Assert.assertTrue(StringUtil.isNumeric(string7));

        String string8 = "0123";
        Assert.assertTrue(StringUtil.isNumeric(string8));

        String string9 = "123.45";
        Assert.assertFalse(StringUtil.isNumeric(string9));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\f'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertTrue(StringUtil.isWhitespace(160));

        Assert.assertFalse(StringUtil.isWhitespace('a'));
        Assert.assertFalse(StringUtil.isWhitespace('b'));
        Assert.assertFalse(StringUtil.isWhitespace('c'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "   Hello World   ";
        String expected = "Hello World";
        String actual = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, actual);

        input = "   Hello\tWorld\n   ";
        expected = "Hello World";
        actual = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, actual);

        input = "Hello\nWorld";
        expected = "Hello World";
        actual = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, actual);

        input = "   Hello\tWorld\nWorld\n   ";
        expected = "Hello WorldWorld";
        actual = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String needle = "test";
        String[] haystack = {"test1", "test2", "test"};
        Assert.assertTrue(StringUtil.in(needle, haystack));

        haystack = new String[0];
        Assert.assertFalse(StringUtil.in(needle, haystack));

        needle = "Test";
        Assert.assertFalse(StringUtil.in(needle, haystack));

        haystack = new String[]{"test", "Test", "TEST"};
        Assert.assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "test";
        String[] haystack = {"test1", "test2", "test"};
        Assert.assertTrue(StringUtil.inSorted(needle, haystack));

        needle = "Test";
        Assert.assertFalse(StringUtil.inSorted(needle, haystack));

        haystack = new String[0];
        Assert.assertFalse(StringUtil.inSorted(needle, haystack));

        haystack = new String[]{"test", "Test", "TEST"};
        Assert.assertTrue(StringUtil.inSorted(needle, haystack));

        haystack = new String[]{"Test", "test", "test1"};
        Assert.assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithInvalidBase() {
        String base = "http://example.com/";
        String relUrl = "//example.com";
        StringUtil.resolve(new URL(base), relUrl);
    }

    @Test
    public void testResolve() {
        String base = "http://example.com/";
        String relUrl = "/foo/bar.html";
        String expected = "http://example.com/foo/bar.html";
        String actual = StringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);

        base = "http://example.com/";
        relUrl = "http://example.org/baz.html";
        actual = StringUtil.resolve(base, relUrl);
        assertEquals(relUrl, actual);

        base = "http://example.com/";
        relUrl = "//example.org/baz.html";
        expected = "http://example.org/baz.html";
        actual = StringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);

        base = "http://example.com/";
        relUrl = "/foo/bar.html?qux=123";
        expected = "http://example.com/foo/bar.html?qux=123";
        actual = StringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);
    }
}