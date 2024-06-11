package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    private StringUtil stringUtil;

    @Before
    public void setUp() {
        stringUtil = new StringUtil();
    }

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String expected = "Hello World !";
        String actual = stringUtil.join(strings, " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();

        String expected = "Hello World !";
        String actual = stringUtil.join(iterator, " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};

        String expected = "Hello World !";
        String actual = stringUtil.join(strings, " ");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        stringUtil.padding(-1);
    }

    @Test
    public void testPaddingWidthLessThanPaddingLength() {
        int width = 10;
        String expected = "         ";
        String actual = stringUtil.padding(width);
        assertEquals(expected, actual);
    }

    @Test
    public void testPaddingWidthGreaterThanPaddingLength() {
        int width = 25;
        String expected = "                         ";
        String actual = stringUtil.padding(width);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNullString() {
        String string = null;

        boolean expected = true;
        boolean actual = stringUtil.isBlank(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankEmptyString() {
        String string = "";

        boolean expected = true;
        boolean actual = stringUtil.isBlank(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankWhitespaceString() {
        String string = "   ";

        boolean expected = true;
        boolean actual = stringUtil.isBlank(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNonBlankString() {
        String string = "Hello";

        boolean expected = false;
        boolean actual = stringUtil.isBlank(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericNullString() {
        String string = null;

        boolean expected = false;
        boolean actual = stringUtil.isNumeric(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericEmptyString() {
        String string = "";

        boolean expected = false;
        boolean actual = stringUtil.isNumeric(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericNonNumericString() {
        String string = "Hello";

        boolean expected = false;
        boolean actual = stringUtil.isNumeric(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericNumericString() {
        String string = "12345";

        boolean expected = true;
        boolean actual = stringUtil.isNumeric(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceSpace() {
        int c = ' ';

        boolean expected = true;
        boolean actual = stringUtil.isWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTab() {
        int c = '\t';

        boolean expected = true;
        boolean actual = stringUtil.isWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceNewline() {
        int c = '\n';

        boolean expected = true;
        boolean actual = stringUtil.isWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        int c = '\f';

        boolean expected = true;
        boolean actual = stringUtil.isWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        int c = '\r';

        boolean expected = true;
        boolean actual = stringUtil.isWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        int c = ' ';

        boolean expected = true;
        boolean actual = stringUtil.isActuallyWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        int c = '\t';

        boolean expected = true;
        boolean actual = stringUtil.isActuallyWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        int c = '\n';

        boolean expected = true;
        boolean actual = stringUtil.isActuallyWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        int c = '\f';

        boolean expected = true;
        boolean actual = stringUtil.isActuallyWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        int c = '\r';

        boolean expected = true;
        boolean actual = stringUtil.isActuallyWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        int c = 160;

        boolean expected = true;
        boolean actual = stringUtil.isActuallyWhitespace(c);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "  Hello  \n   World!   \t ";

        String expected = " Hello World! ";
        String actual = stringUtil.normaliseWhitespace(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello  \n   World!   \t ";

        String expected = "Hello World!";
        stringUtil.appendNormalisedWhitespace(accum, string, false);
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testInStringFound() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!"};

        boolean expected = true;
        boolean actual = stringUtil.in(needle, haystack);
        assertEquals(expected, actual);
    }

    @Test
    public void testInStringNotFound() {
        String needle = "Java";
        String[] haystack = {"Hello", "World", "!"};

        boolean expected = false;
        boolean actual = stringUtil.in(needle, haystack);
        assertEquals(expected, actual);
    }

    @Test
    public void testInSortedStringFound() {
        String needle = "World";
        String[] haystack = {"!", "Hello", "World"};

        boolean expected = true;
        boolean actual = stringUtil.inSorted(needle, haystack);
        assertEquals(expected, actual);
    }

    @Test
    public void testInSortedStringNotFound() {
        String needle = "Java";
        String[] haystack = {"!", "Hello", "World"};

        boolean expected = false;
        boolean actual = stringUtil.inSorted(needle, haystack);
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveWithBaseUrlAndRelUrl() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "path/to/file.html";

        URL expected = new URL("https://www.example.com/path/to/file.html");
        URL actual = stringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveWithBaseUrlAndAbsoluteRelUrl() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "https://www.example.com/path/to/file.html";

        URL expected = new URL("https://www.example.com/path/to/file.html");
        URL actual = stringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveWithInvalidBaseUrlAndAbsoluteRelUrl() {
        String baseUrl = "invalidUrl";
        String relUrl = "https://www.example.com/path/to/file.html";

        String expected = "";
        String actual = stringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveWithInvalidBaseUrlAndRelUrl() {
        String baseUrl = "invalidUrl";
        String relUrl = "path/to/file.html";

        String expected = "";
        String actual = stringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = stringUtil.stringBuilder();
        StringBuilder sb2 = stringUtil.stringBuilder();

        assertSame(sb1, sb2);
    }
}