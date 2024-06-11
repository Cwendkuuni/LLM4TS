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
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String expectedResult = "Hello-World";
        String result = StringUtil.join(strings, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        String expectedResult = "Hello-World";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String expectedResult = "Hello-World";
        String result = StringUtil.join(strings, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expectedResult = "     ";
        String result = StringUtil.padding(width);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsBlank() {
        String string = "";
        assertTrue(StringUtil.isBlank(string));
    }

    @Test
    public void testIsBlankNotEmpty() {
        String string = "Hello";
        assertFalse(StringUtil.isBlank(string));
    }

    @Test
    public void testIsNumeric() {
        String string = "12345";
        assertTrue(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsNumericNotNumeric() {
        String string = "Hello";
        assertFalse(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsNotWhitespace() {
        int c = 'A';
        assertFalse(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testIsNotActuallyWhitespace() {
        int c = 'A';
        assertFalse(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "  Hello \n World  ";
        String expectedResult = "Hello World";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello \n World  ";
        boolean stripLeading = true;
        String expectedResult = "Hello World";
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        String result = accum.toString();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testNotIn() {
        String needle = "Hello";
        String[] haystack = {"Foo", "Bar"};
        assertFalse(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Apple", "Hello", "World"};
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testInSortedNotIn() {
        String needle = "Hello";
        String[] haystack = {"Apple", "World"};
        assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://example.com");
        String relUrl = "/test.html";
        String expectedResult = "https://example.com/test.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testResolveInvalidBaseUrl() {
        String baseUrl = "invalid";
        String relUrl = "/test.html";
        String expectedResult = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expectedResult, result);
    }

    @Test
    public void stringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
    }
}