package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.net.URL;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection_String() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String result = StringUtil.join(strings, " ");

        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinIterator_String() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        Iterator<String> iterator = strings.iterator();

        String result = StringUtil.join(iterator, " ");

        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinStringArray_String() {
        String[] strings = {"Hello", "World", "!"};

        String result = StringUtil.join(strings, " ");

        assertEquals("Hello World !", result);
    }

    @Test
    public void testPadding() {
        int width = 5;

        String result = StringUtil.padding(width);

        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        String string = "";

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void testIsNumeric() {
        String string = "123";

        boolean result = StringUtil.isNumeric(string);

        assertTrue(result);
    }

    @Test
    public void testIsWhitespace() {
        int codePoint = ' ';

        boolean result = StringUtil.isWhitespace(codePoint);

        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int codePoint = ' ';

        boolean result = StringUtil.isActuallyWhitespace(codePoint);

        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "Hello  \n  World!";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals("Hello World!", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder stringBuilder = new StringBuilder();
        String string = "Hello  \n  World!";

        StringUtil.appendNormalisedWhitespace(stringBuilder, string, true);

        assertEquals("Hello World!", stringBuilder.toString());
    }

    @Test
    public void testIn() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!"};

        boolean result = StringUtil.in(needle, haystack);

        assertTrue(result);
    }

    @Test
    public void testInSorted() {
        String needle = "World";
        String[] haystack = {"!", "Hello", "World"};

        boolean result = StringUtil.inSorted(needle, haystack);

        assertTrue(result);
    }

    @Test
    public void testResolve_URL() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "/about";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL("http://www.example.com/about"), result);
    }

    @Test
    public void testResolve_String_String() {
        String baseUrl = "http://www.example.com";
        String relUrl = "/about";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals("http://www.example.com/about", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder result = StringUtil.stringBuilder();

        assertNotNull(result);
    }
}

