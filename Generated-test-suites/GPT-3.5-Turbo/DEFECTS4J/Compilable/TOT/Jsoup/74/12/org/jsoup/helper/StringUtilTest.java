package org.jsoup.helper;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {

    // Test case for join(Collection strings, String sep)
    @Test
    public void testJoinCollection() {
        String[] strings = {"abc", "def", "ghi"};
        String sep = ",";
        String expected = "abc,def,ghi";
        String result = StringUtil.join(Arrays.asList(strings), sep);
        assertEquals(expected, result);
    }

    // Test case for join(Iterator strings, String sep)
    @Test
    public void testJoinIterator() {
        String[] strings = {"abc", "def", "ghi"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String sep = ",";
        String expected = "abc,def,ghi";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expected, result);
    }

    // Test case for join(String[] strings, String sep)
    @Test
    public void testJoinArray() {
        String[] strings = {"abc", "def", "ghi"};
        String sep = ",";
        String expected = "abc,def,ghi";
        String result = StringUtil.join(strings, sep);
        assertEquals(expected, result);
    }

    // Test case for padding(int width)
    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        assertEquals(expected, result);
    }

    // Test case for isBlank(String string)
    @Test
    public void testIsBlank() {
        String blankString = ""; // blank string
        String nullString = null; // null string
        String whitespaceString = "   "; // string with only whitespace
        String nonBlankString = "abc"; // non-blank string

        assertTrue(StringUtil.isBlank(blankString));
        assertTrue(StringUtil.isBlank(nullString));
        assertTrue(StringUtil.isBlank(whitespaceString));
        assertFalse(StringUtil.isBlank(nonBlankString));
    }

    // Test case for isNumeric(String string)
    @Test
    public void testIsNumeric() {
        String numericString = "12345"; // Contains only digits
        String blankString = ""; // Blank string
        String nullString = null; // Null string
        String nonNumericString = "abc"; // Contains non-digit characters

        assertTrue(StringUtil.isNumeric(numericString));
        assertFalse(StringUtil.isNumeric(blankString));
        assertFalse(StringUtil.isNumeric(nullString));
        assertFalse(StringUtil.isNumeric(nonNumericString));
    }

    // Test case for isWhitespace(int c)
    @Test
    public void testIsWhitespace() {
        char whitespace = ' '; // space
        char tab = '\t'; // tab
        char newline = '\n'; // newline
        char lineFeed = '\r'; // line feed
        char formFeed = '\f'; // form feed

        assertTrue(StringUtil.isWhitespace(whitespace));
        assertTrue(StringUtil.isWhitespace(tab));
        assertTrue(StringUtil.isWhitespace(newline));
        assertTrue(StringUtil.isWhitespace(lineFeed));
        assertTrue(StringUtil.isWhitespace(formFeed));
    }

    // Test case for isActuallyWhitespace(int c)
    @Test
    public void testIsActuallyWhitespace() {
        char whitespace = ' '; // space
        char tab = '\t'; // tab
        char newline = '\n'; // newline
        char lineFeed = '\r'; // line feed
        char formFeed = '\f'; // form feed
        char nonBreakingSpace = 160; // &nbsp;

        assertTrue(StringUtil.isActuallyWhitespace(whitespace));
        assertTrue(StringUtil.isActuallyWhitespace(tab));
        assertTrue(StringUtil.isActuallyWhitespace(newline));
        assertTrue(StringUtil.isActuallyWhitespace(lineFeed));
        assertTrue(StringUtil.isActuallyWhitespace(formFeed));
        assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));
    }

    // Test case for normaliseWhitespace(String string)
    @Test
    public void testNormaliseWhitespace() {
        String stringWithMultipleSpaces = "    abc   def   ghi   ";
        String expected = " abc def ghi ";
        String result = StringUtil.normaliseWhitespace(stringWithMultipleSpaces);
        assertEquals(expected, result);
    }

    // Test case for appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading)
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String stringWithMultipleSpaces = "    abc   def   ghi   ";
        String expected = " abc def ghi ";
        StringUtil.appendNormalisedWhitespace(accum, stringWithMultipleSpaces, false);
        assertEquals(expected, accum.toString());
    }

    // Test case for in(String needle, String... haystack)
    @Test
    public void testIn() {
        String needle = "def";
        String[] haystack = {"abc", "def", "ghi"};

        assertTrue(StringUtil.in(needle, haystack));
    }

    // Test case for inSorted(String needle, String[] haystack)
    @Test
    public void testInSorted() {
        String needle = "def";
        String[] haystack = {"abc", "def", "ghi"};

        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    // Test case for resolve(URL base, String relUrl)
    @Test
    public void testResolveURL() {
        try {
            URL base = new URL("http://www.example.com");
            String relUrl = "/test";
            URL expected = new URL("http://www.example.com/test");

            URL result = StringUtil.resolve(base, relUrl);
            assertEquals(expected, result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Test case for resolve(String baseUrl, String relUrl)
    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "/test";
        String expected = "http://www.example.com/test";

        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    // Test case for stringBuilder()
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        assertNotEquals(sb1, sb2);
        assertEquals(0, sb1.length());
        assertEquals(0, sb2.length());
    }

}