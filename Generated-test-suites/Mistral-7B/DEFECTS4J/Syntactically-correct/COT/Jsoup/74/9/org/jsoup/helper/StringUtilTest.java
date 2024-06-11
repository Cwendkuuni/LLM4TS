package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StringUtil test cases.
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        String expected = "HelloWorld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String result = StringUtil.join(strings, "");
        assertEquals("", result);
    }

    @Test
    public void testJoinIteratorSingle() {
        Iterator<String> strings = Arrays.asList("Hello").iterator();
        String result = StringUtil.join(strings, "");
        assertEquals("Hello", result);
    }

    @Test
    public void testJoinIteratorMultiple() {
        Iterator<String> strings = Arrays.asList("Hello", "World", "!").iterator();
        String expected = "HelloWorld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String result = StringUtil.join(strings, "");
        assertEquals("", result);
    }

    @Test
    public void testJoinArraySingle() {
        String[] strings = {"Hello"};
        String result = StringUtil.join(strings, "");
        assertEquals("Hello", result);
    }

    @Test
    public void testJoinArrayMultiple() {
        String[] strings = {"Hello", "World", "!"};
        String expected = "HelloWorld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String result = StringUtil.padding(-5);
    }

    @Test
    public void testIsBlankNull() {
        boolean result = StringUtil.isBlank(null);
        assertTrue(result);
    }

    @Test
    public void testIsBlankEmpty() {
        String str = "";
        boolean result = StringUtil.isBlank(str);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespace() {
        String str = "   ";
        boolean result = StringUtil.isBlank(str);
        assertTrue(result);
    }

    @Test
    public void testIsBlankNonBlank() {
        String str = "HelloWorld";
        boolean result = StringUtil.isBlank(str);
        assertFalse(result);
    }

    @Test
    public void testIsNumericTrue() {
        String str = "123";
        boolean result = StringUtil.isNumeric(str);
        assertTrue(result);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String str = "";
        boolean result = StringUtil.isNumeric(str);
        assertFalse(result);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String str = "abc";
        boolean result = StringUtil.isNumeric(str);
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceTrue() {
        int c = ' ';
        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFalseNonWhitespace() {
        int c = 'a';
        boolean result = StringUtil.isWhitespace(c);
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceTrue() {
        int c = '\n';
        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceFalseNonWhitespace() {
        int c = 'a';
        boolean result = StringUtil.isActuallyWhitespace(c);
        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String str = "";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String str = " ";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String str = "   ";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSingleTab() {
        String str = "\t";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleTabs() {
        String str = "\t\t";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMixedSpacesAndTabs() {
        String str = "   \t  ";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSingleNewline() {
        String str = "\n";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleNewlines() {
        String str = "\n\n";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMixedNewlinesAndSpaces() {
        String str = "\n   \t  ";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMixedNewlinesSpacesAndTabs() {
        String str = "\n   \t  \n   \t  ";
        String result = StringUtil.normaliseWhitespace(str);
        assertEquals("", result);
    }

    @Test
    public void testInNullNeedle() {
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.in(null, haystack);
        assertFalse(result);
    }

    @Test
    public void testInEmptyNeedle() {
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.in( "", haystack);
        assertFalse(result);
    }

    @Test
    public void testInEmptyHaystack() {
        String needle = "Hello";
        boolean result = StringUtil.in(needle, new String[0]);
        assertFalse(result);
    }

    @Test
    public void testInSingleMatch() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInMultipleMatches() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World", "Hello"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInNoMatch() {
        String needle = "Hello";
        String[] haystack = {"World", "!"};
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testInSortedNullNeedle() {
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.inSorted(null, haystack);
        assertFalse(result);
    }

    @Test
    public void testInSortedEmptyNeedle() {
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.inSorted("", haystack);
        assertFalse(result);
    }

    @Test
    public void testInSortedEmptyHaystack() {
        String needle = "Hello";
        boolean result = StringUtil.inSorted(needle, new String[0]);
        assertFalse(result);
    }

    @Test
    public void testInSortedSingleMatch() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedMultipleMatches() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World", "Hello"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedNoMatch() {
        String needle = "Hello";
        String[] haystack = {"World", "!"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testResolveBaseNull() {
        String relUrl = "";
        String result = StringUtil.resolve(null, relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveBaseEmpty() {
        String relUrl = "";
        String result = StringUtil.resolve("", relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveBaseAbsolute() {
        String relUrl = "/path/file";
        String baseUrl = "http://example.com/";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path/file", result);
    }

    @Test
    public void testResolveBaseRelative() {
        String relUrl = "path/file";
        String baseUrl = "http://example.com/some/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/some/path/path/file", result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveRelativeInvalid() {
        String relUrl = "://path/file";
        String baseUrl = "http://example.com/some/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveRelativeAbsolute() {
        String relUrl = "/path/file";
        String baseUrl = "http://example.com/";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveRelativeDoubleSlash() {
        String relUrl = "//path/file";
        String baseUrl = "http://example.com/some/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveRelativeTrailingSlash() {
        String relUrl = "/path/";
        String baseUrl = "http://example.com/some/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }
}