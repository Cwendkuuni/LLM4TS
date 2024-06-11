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
    public void testJoinCollectionString() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        String sep = ",";
        String expected = "Hello,World";
        String result = StringUtil.join(collection, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorString() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        Iterator<String> iterator = collection.iterator();
        String sep = ",";
        String expected = "Hello,World";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinStringArrayString() {
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
        String string1 = null;
        String string2 = "";
        String string3 = "   ";
        assertTrue(StringUtil.isBlank(string1));
        assertTrue(StringUtil.isBlank(string2));
        assertTrue(StringUtil.isBlank(string3));
    }

    @Test
    public void testIsNumeric() {
        String string1 = null;
        String string2 = "";
        String string3 = "123";
        String string4 = "abc";
        assertFalse(StringUtil.isNumeric(string1));
        assertFalse(StringUtil.isNumeric(string2));
        assertTrue(StringUtil.isNumeric(string3));
        assertFalse(StringUtil.isNumeric(string4));
    }

    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        assertTrue(StringUtil.isWhitespace(c1));
        assertTrue(StringUtil.isWhitespace(c2));
        assertTrue(StringUtil.isWhitespace(c3));
        assertTrue(StringUtil.isWhitespace(c4));
        assertTrue(StringUtil.isWhitespace(c5));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        int c6 = 160;
        assertTrue(StringUtil.isActuallyWhitespace(c1));
        assertTrue(StringUtil.isActuallyWhitespace(c2));
        assertTrue(StringUtil.isActuallyWhitespace(c3));
        assertTrue(StringUtil.isActuallyWhitespace(c4));
        assertTrue(StringUtil.isActuallyWhitespace(c5));
        assertTrue(StringUtil.isActuallyWhitespace(c6));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "  Hello   World  ";
        String expected = " Hello World ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello   World  ";
        String expected = " Hello World ";
        StringUtil.appendNormalisedWhitespace(accum, string, false);
        String result = accum.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Apple", "Hello", "World"};
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolveURLString() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "/path";
        URL expected = new URL("http://www.example.com/path");
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveStringString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "/path";
        String expected = "http://www.example.com/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertNotSame(sb1, sb2);
    }

}