package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    // Test cases for join(Collection strings, String sep) method
    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String sep = " ";

        String expected = "Hello World !";
        String actual = StringUtil.join(strings, sep);

        assertEquals(expected, actual);
    }

    // Test cases for join(Iterator strings, String sep) method
    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        Iterator<String> iterator = strings.iterator();
        String sep = " ";

        String expected = "Hello World !";
        String actual = StringUtil.join(iterator, sep);

        assertEquals(expected, actual);
    }

    // Test cases for join(String[] strings, String sep) method
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String sep = " ";

        String expected = "Hello World !";
        String actual = StringUtil.join(strings, sep);

        assertEquals(expected, actual);
    }

    // Test cases for padding(int width) method
    @Test
    public void testPadding() {
        int width = 5;

        String expected = "     ";
        String actual = StringUtil.padding(width);

        assertEquals(expected, actual);
    }

    // Test cases for isBlank(String string) method
    @Test
    public void testIsBlank() {
        String string1 = "";
        String string2 = " ";
        String string3 = "\t";
        String string4 = "Hello";

        assertTrue(StringUtil.isBlank(string1));
        assertTrue(StringUtil.isBlank(string2));
        assertTrue(StringUtil.isBlank(string3));
        assertFalse(StringUtil.isBlank(string4));
    }

    // Test cases for isNumeric(String string) method
    @Test
    public void testIsNumeric() {
        String string1 = "";
        String string2 = "123";
        String string3 = "12a";

        assertFalse(StringUtil.isNumeric(string1));
        assertTrue(StringUtil.isNumeric(string2));
        assertFalse(StringUtil.isNumeric(string3));
    }

    // Test cases for isWhitespace(int c) method
    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\r';
        int c5 = 'a';

        assertTrue(StringUtil.isWhitespace(c1));
        assertTrue(StringUtil.isWhitespace(c2));
        assertTrue(StringUtil.isWhitespace(c3));
        assertTrue(StringUtil.isWhitespace(c4));
        assertFalse(StringUtil.isWhitespace(c5));
    }

    // Test cases for isActuallyWhitespace(int c) method
    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\r';
        int c5 = 'a';

        assertTrue(StringUtil.isActuallyWhitespace(c1));
        assertTrue(StringUtil.isActuallyWhitespace(c2));
        assertTrue(StringUtil.isActuallyWhitespace(c3));
        assertTrue(StringUtil.isActuallyWhitespace(c4));
        assertFalse(StringUtil.isActuallyWhitespace(c5));
    }

    // Test cases for normaliseWhitespace(String string) method
    @Test
    public void testNormaliseWhitespace() {
        String string1 = "  Hello   World  ";
        String string2 = "\tHello\tWorld\t";
        String string3 = "\nHello\nWorld\n";
        String string4 = "\rHello\rWorld\r";

        String expected = " Hello World ";
        String actual = StringUtil.normaliseWhitespace(string1);

        assertEquals(expected, actual);

        expected = " Hello World ";
        actual = StringUtil.normaliseWhitespace(string2);

        assertEquals(expected, actual);

        expected = " Hello World ";
        actual = StringUtil.normaliseWhitespace(string3);

        assertEquals(expected, actual);

        expected = " Hello World ";
        actual = StringUtil.normaliseWhitespace(string4);

        assertEquals(expected, actual);
    }

    // Test cases for resolve(URL base, String relUrl) method
    @Test
    public void testResolve() {
        try {
            URL base = new URL("http://www.example.com/");
            String relUrl = "index.html";

            String expected = "http://www.example.com/index.html";
            String actual = StringUtil.resolve(base, relUrl).toExternalForm();

            assertEquals(expected, actual);
        } catch (MalformedURLException e) {
            fail("MalformedURLException occurred");
        }
    }

    // Test cases for resolve(String baseUrl, String relUrl) method
    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com/";
        String relUrl = "index.html";

        String expected = "http://www.example.com/index.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);

        assertEquals(expected, actual);
    }

    // Test cases for stringBuilder() method
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        assertNotSame(sb1, sb2);
        assertTrue(sb1.length() <= StringUtil.MaxCachedBuilderSize);
        assertTrue(sb2.length() <= StringUtil.MaxCachedBuilderSize);
    }

}