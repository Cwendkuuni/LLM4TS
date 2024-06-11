package org.jsoup.helper;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String result = StringUtil.join(strings.iterator(), " ");
        assertEquals("Hello World", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("12a"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("   Hello  World   ");
        assertEquals(" Hello World ", result);
    }

    @Test
    public void testIn() {
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in("Hello", haystack));
        assertFalse(StringUtil.in("Foo", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Apple", "Banana", "Orange"};
        assertTrue(StringUtil.inSorted("Banana", haystack));
        assertFalse(StringUtil.inSorted("Mango", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "/path/to/page";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/path/to/page", result.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "/path/to/page";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/path/to/page", result);
    }

}