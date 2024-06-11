package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the StringUtil class
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String actual = StringUtil.join(strings.iterator(), "");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "!"};
        String expected = "helloworld!";
        String actual = StringUtil.join(strings, "");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNull() {
        String string = null;
        boolean actual = StringUtil.isBlank(string);
        assertTrue(actual);
    }

    @Test
    public void testIsBlankEmpty() {
        String string = "";
        boolean actual = StringUtil.isBlank(string);
        assertTrue(actual);
    }

    @Test
    public void testIsBlankSpace() {
        String string = " ";
        boolean actual = StringUtil.isBlank(string);
        assertTrue(actual);
    }

    @Test
    public void testIsBlankWhitespace() {
        String string = "\t\n\r\f ";
        boolean actual = StringUtil.isBlank(string);
        assertTrue(actual);
    }

    @Test
    public void testIsBlankNonBlank() {
        String string = "hello";
        boolean actual = StringUtil.isBlank(string);
        assertFalse(actual);
    }

    @Test
    public void testIsNumericTrue() {
        String string = "123";
        boolean actual = StringUtil.isNumeric(string);
        assertTrue(actual);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String string = "";
        boolean actual = StringUtil.isNumeric(string);
        assertFalse(actual);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String string = "abc";
        boolean actual = StringUtil.isNumeric(string);
        assertFalse(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        int width = -1;
        String actual = StringUtil.padding(width);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testPaddingZero() {
        int width = 0;
        String actual = StringUtil.padding(width);
        assertEquals("", actual);
    }

    @Test
    public void testPaddingLengthLessThanPaddingSize() {
        int width = 5;
        String actual = StringUtil.padding(width);
        assertEquals("   ", actual);
    }

    @Test
    public void testPaddingLengthGreaterThanPaddingSize() {
        int width = 22;
        String actual = StringUtil.padding(width);
        assertEquals("                 ", actual);
    }

    @Test
    public void testIsWhitespaceTrue() {
        int c = ' ';
        boolean actual = StringUtil.isWhitespace(c);
        assertTrue(actual);
    }

    @Test
    public void testIsWhitespaceFalse() {
        int c = 'a';
        boolean actual = StringUtil.isWhitespace(c);
        assertFalse(actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrue() {
        int c = ' ';
        boolean actual = StringUtil.isActuallyWhitespace(c);
        assertTrue(actual);
    }

    @Test
    public void testIsActuallyWhitespaceFalse() {
        int c = 'a';
        boolean actual = StringUtil.isActuallyWhitespace(c);
        assertFalse(actual);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        int c = 160;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        assertTrue(actual);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String string = "";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String string = " ";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals(" ", actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String string = " \t\n\r\f";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals(" ", actual);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailing() {
        String string = " \t\n hello\t world! \r\n ";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals(" hello world!", actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String string = "hello\tworld! \n\r \t";
        String actual = StringUtil.normaliseWhitespace(string);
        assertEquals("hello world!", actual);
    }

    @Test
    public void testInEmptyString() {
        String needle = "";
        String[] haystack = new String[]{"hello", "world"};
        boolean actual = StringUtil.in(needle, haystack);
        assertFalse(actual);
    }

    @Test
    public void testInSingleNeedleSingleString() {
        String needle = "hello";
        String[] haystack = new String[]{"hello"};
        boolean actual = StringUtil.in(needle, haystack);
        assertTrue(actual);
    }

    @Test
    public void testInSingleNeedleMultipleStrings() {
        String needle = "hello";
        String[] haystack = new String[]{"hello", "world"};
        boolean actual = StringUtil.in(needle, haystack);
        assertTrue(actual);
    }

    @Test
    public void testInMultipleNeedlesSingleString() {
        String[] needles = new String[]{"hello", "world"};
        String string = "hello";
        boolean actual = StringUtil.in(needles, string);
        assertTrue(actual);
    }

    @Test
    public void testInMultipleNeedlesMultipleStrings() {
        String[] needles = new String[]{"hello", "world"};
        String[] strings = new String[]{"hello", "world", "!"};
        boolean actual = StringUtil.in(needles, strings);
        assertTrue(actual);
    }

    @Test
    public void testInSortedEmptyString() {
        String needle = "";
        String[] haystack = new String[]{"hello", "world"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        assertFalse(actual);
    }

    @Test
    public void testInSortedSingleNeedleSingleString() {
        String needle = "hello";
        String[] haystack = new String[]{"hello"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        assertTrue(actual);
    }

    @Test
    public void testInSortedSingleNeedleMultipleStrings() {
        String needle = "hello";
        String[] haystack = new String[]{"hello", "world"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        assertTrue(actual);
    }

    @Test
    public void testInSortedMultipleNeedlesSingleString() {
        String[] needles = new String[]{"hello", "world"};
        String string = "hello";
        boolean actual = StringUtil.inSorted(needles, string);
        assertTrue(actual);
    }

    @Test
    public void testInSortedMultipleNeedlesMultipleStrings() {
        String[] needles = new String[]{"hello", "world"};
        String[] strings = new String[]{"hello", "world", "!"};
        boolean actual = StringUtil.inSorted(needles, strings);
        assertTrue(actual);
    }

    @Test
    public void testResolveNullBaseUrl() {
        String relUrl = "hello";
        String actual = StringUtil.resolve(null, relUrl);
        assertEquals("", actual);
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        String relUrl = "hello";
        String actual = StringUtil.resolve("", relUrl);
        assertEquals("hello", actual);
    }

    @Test
    public void testResolveAbsoluteRelUrl() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "path/to/other";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path/to/other", actual);
    }

    @Test
    public void testResolveRelativeUrl() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "../to/other";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/to/other", actual);
    }

    @Test
    public void testResolveInvalidRelUrl() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "//example.com/to/other";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("/to/other", actual);
    }

    @Test
    public void testResolveMalformedBaseUrl() {
        String relUrl = "hello";
        String actual = StringUtil.resolve("http://example.com", relUrl);
        assertEquals("hello", actual);
    }

    @Test
    public void testResolveMalformedRelUrl() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "/to/other";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/to/other", actual);
    }

    @Test
    public void testResolveInvalidScheme() {
        String baseUrl = "file:/path/to/file";
        String relUrl = "http://example.com/to/other";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("file:/path/to/other/http://example.com/to/other", actual);
    }

    @Test
    public void testResolveInvalidRelScheme() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "file:/to/other";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/file:/to/other", actual);
    }

    @Test
    public void testResolveSameUrls() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "http://example.com/path/to/file";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(baseUrl, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidUrl() {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "this:is:not:a:url";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        fail("Expected MalformedURLException");
    }

    @Test
    public void testBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        sb.append("world");
        String expected = "helloworld";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testBuilderMultipleCalls() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        StringBuilder sb2 = StringUtil.stringBuilder();
        sb2.append("world");
        String expected = "helloworld";
        String actual = sb.append(sb2).toString();
        assertEquals(expected, actual);
    }

}