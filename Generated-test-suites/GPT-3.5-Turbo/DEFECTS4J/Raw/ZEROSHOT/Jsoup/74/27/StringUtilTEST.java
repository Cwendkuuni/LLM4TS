
package org.jsoup.helper;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoin_collection() {
        Collection<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");
        strings.add("test");
        String sep = " ";
        String expected = "hello world test";
        String actual = StringUtil.join(strings, sep);
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_iterator() {
        Collection<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");
        strings.add("test");
        Iterator<String> iterator = strings.iterator();
        String sep = " ";
        String expected = "hello world test";
        String actual = StringUtil.join(iterator, sep);
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_array() {
        String[] strings = {"hello", "world", "test"};
        String sep = " ";
        String expected = "hello world test";
        String actual = StringUtil.join(strings, sep);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String actual = StringUtil.padding(width);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_invalidWidth() {
        int width = -1;
        StringUtil.padding(width);
    }

    @Test
    public void testIsBlank_nullString() {
        String string = null;
        assertTrue(StringUtil.isBlank(string));
    }

    @Test
    public void testIsBlank_emptyString() {
        String string = "";
        assertTrue(StringUtil.isBlank(string));
    }

    @Test
    public void testIsBlank_whitespaceString() {
        String string = "   ";
        assertTrue(StringUtil.isBlank(string));
    }

    @Test
    public void testIsBlank_nonBlankString() {
        String string = "hello";
        assertFalse(StringUtil.isBlank(string));
    }

    @Test
    public void testIsNumeric_emptyString() {
        String string = "";
        assertFalse(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsNumeric_nullString() {
        String string = null;
        assertFalse(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsNumeric_numericString() {
        String string = "12345";
        assertTrue(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsNumeric_nonNumericString() {
        String string = "hello";
        assertFalse(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsWhitespace_space() {
        int c = ' ';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsWhitespace_tab() {
        int c = '\t';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsWhitespace_newline() {
        int c = '\n';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsWhitespace_formfeed() {
        int c = '\f';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsWhitespace_carriageReturn() {
        int c = '\r';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsWhitespace_nonWhitespace() {
        int c = 'a';
        assertFalse(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace_space() {
        int c = ' ';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace_tab() {
        int c = '\t';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace_newline() {
        int c = '\n';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace_formfeed() {
        int c = '\f';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace_carriageReturn() {
        int c = '\r';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace_nonBreakingSpace() {
        int c = 160;
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testNormaliseWhitespace_multipleSpaces() {
        String string = " multiple   spaces ";
        String expected = "multiple spaces";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespace_newlineTab() {
        String string = "\n\t";
        String expected = " ";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendNormalisedWhitespace_stripLeading() {
        StringBuilder sb = new StringBuilder();
        String string = " \n\texample";
        String expected = "example";
        StringUtil.appendNormalisedWhitespace(sb, string, true);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testIn_matchExists() {
        String needle = "needle";
        String[] haystack = {"needle", "hay", "stack"};
        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testIn_noMatchExists() {
        String needle = "needle";
        String[] haystack = {"one", "two", "three"};
        assertFalse(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted_matchExists() {
        String needle = "needle";
        String[] haystack = {"hay", "needle", "stack"};
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testInSorted_noMatchExists() {
        String needle = "needle";
        String[] haystack = {"one", "two", "three"};
        assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve_validUrls() {
        String baseUrl = "http://www.example.com/";
        String relUrl = "page.html";
        String expected = "http://www.example.com/page.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);
    }

    @Test
    public void testResolve_invalidBaseUrl() {
        String baseUrl = "invalidUrl";
        String relUrl = "page.html";
        String expected = "";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }
}
