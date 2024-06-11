package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!", "test");
        String expected = "helloworld!test";
        String actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);

        strings = Collections.emptyList();
        expected = "";
        actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);

        strings = Arrays.asList("", null);
        expected = "";
        actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);

        strings = Arrays.asList("apple", "banana", "orange");
        String sep = " - ";
        expected = "apple - banana - orange";
        actual = StringUtil.join(strings, sep);
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world", "!", "test"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "helloworld!test";
        String actual = StringUtil.join(iterator, "");
        assertEquals(expected, actual);

        iterator = Collections.emptyIterator();
        expected = "";
        actual = StringUtil.join(iterator, "");
        assertEquals(expected, actual);

        iterator = Arrays.asList("", null).iterator();
        expected = "";
        actual = StringUtil.join(iterator, "");
        assertEquals(expected, actual);

        strings = new String[]{"apple", "banana", "orange"};
        String sep = " - ";
        expected = "apple - banana - orange";
        actual = StringUtil.join(iterator, sep);
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "!", "test"};
        String expected = "helloworld!test";
        String actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);

        strings = new String[0];
        expected = "";
        actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);

        strings = new String[]{null, "", null};
        expected = "";
        actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);

        strings = new String[]{"apple", "banana", "orange"};
        String sep = " - ";
        expected = "apple - banana - orange";
        actual = StringUtil.join(strings, sep);
        assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length + 1; i++) {
            String actual = StringUtil.padding(i);
            String expected = padding[i];
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = new String[]{"", " ", " hello", " hello world", " hello\tworld", " hello\nworld", "hello world!", null};
        for (String string : strings) {
            boolean expected = string == null || string.isEmpty();
            boolean actual = StringUtil.isBlank(string);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = new String[]{"123", "1A3", "123a", "123", "123.4", "123.45", null};
        for (String string : strings) {
            boolean expected = string == null ? false : string.matches("[0-9]+");
            boolean actual = StringUtil.isNumeric(string);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsWhitespace() {
        int[] chars = new int[]{32, 9, 10, 11, 12, 13, 33};
        for (int c : chars) {
            boolean expected = ArrayContains.in(c, new int[]{32, 9, 10, 11, 12, 13, 32, 33}) != 0;
            boolean actual = StringUtil.isWhitespace(c);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeSize() {
        StringUtil.padding(-1);
    }

    @Test
    public void testJoinWithNullStrings() {
        String[] strings = new String[]{"hello", null, "world"};
        String expected = "hellonullworld";
        String actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] strings = new String[]{"   hello world   ", "hello\tworld\n", "   \t   "};
        String expected = "helloworld";
        String actual = StringUtil.normaliseWhitespace(StringUtil.join(Arrays.asList(strings), ""));
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveURL() {
        String baseUrl = "https://example.com";
        String relUrl = "/about";
        String expected = "https://example.com/about";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        baseUrl = "https://example.com";
        relUrl = "https://example2.com";
        expected = "https://example2.com";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        baseUrl = "https://example.com/over/there?foo=1";
        relUrl = "relative";
        expected = "https://example.com/over/there/relative?foo=1";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        baseUrl = "https://example.com/over/there?foo=1";
        relUrl = "..";
        expected = "https://example.com/over/there";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        baseUrl = "https://example.com/over/there?foo=1";
        relUrl = "../..";
        expected = "https://example.com";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        baseUrl = "https://example.com/over/there?foo=1";
        relUrl = "...";
        expected = "https://example.com/over/there";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveURLWithMalformedBase() {
        String baseUrl = "https://example.com";
        String relUrl = "/about";
        try {
            String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
            fail("MalformedURLException expected");
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testResolveURLWithMalformedRel() {
        String baseUrl = "https://example.com";
        String relUrl = "//example2.com";
        try {
            String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
            fail("MalformedURLException expected");
        } catch (MalformedURLException e) {
            // expected
        }
    }
}