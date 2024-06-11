package org.jsoup.helper;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        String sep = ",";
        String expected = "Hello,World";
        String result = StringUtil.join(collection, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> iterator = Arrays.asList("Hello", "World").iterator();
        String sep = ",";
        String expected = "Hello,World";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = ",";
        String expected = "Hello,World";
        String result = StringUtil.join(strings, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        assertEquals(expected, result);
    }

    @Test
    public void testIsBlank() {
        String string = "";
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        assertEquals(expected, result);
    }

    @Test
    public void testIsNumeric() {
        String string = "12345";
        boolean expected = true;
        boolean result = StringUtil.isNumeric(string);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isWhitespace(c);
        assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace(c);
        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   Hello   World   ";
        String expected = " Hello World ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        assertEquals(expected, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        String relUrl = "path/to/file";
        URL expected = new URL("http://example.com/path/to/file");
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com/";
        String relUrl = "path/to/file";
        String expected = "http://example.com/path/to/file";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }
}