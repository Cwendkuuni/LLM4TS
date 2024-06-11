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
 * Test class for StringUtil
 */
public class StringUtilTest {

    /**
     * Test join method with Collection
     */
    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("test1", "test2", "test3");
        String result = StringUtil.join(strings, ",");
        String expected = "test1,test2,test3";
        assertEquals(expected, result);
    }

    /**
     * Test join method with Iterator
     */
    @Test
    public void testJoinIterator() {
        String[] strings = {"test1", "test2", "test3"};
        Iterator<String> it = Arrays.asList(strings).iterator();
        String result = StringUtil.join(it, ",");
        String expected = "test1,test2,test3";
        assertEquals(expected, result);
    }

    /**
     * Test join method with Array
     */
    @Test
    public void testJoinArray() {
        String[] strings = {"test1", "test2", "test3"};
        String result = StringUtil.join(strings, ",");
        String expected = "test1,test2,test3";
        assertEquals(expected, result);
    }

    /**
     * Test padding method with valid width
     */
    @Test
    public void testPaddingValidWidth() {
        String result = StringUtil.padding(5);
        String expected = "     ";
        assertEquals(expected, result);
    }

    /**
     * Test padding method with invalid width
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-5);
    }

    /**
     * Test isBlank method with empty string
     */
    @Test
    public void testIsBlankEmptyString() {
        String string = "";
        assertTrue(StringUtil.isBlank(string));
    }

    /**
     * Test isBlank method with null string
     */
    @Test
    public void testIsBlankNullString() {
        String string = null;
        assertTrue(StringUtil.isBlank(string));
    }

    /**
     * Test isBlank method with whitespace string
     */
    @Test
    public void testIsBlankWhitespaceString() {
        String string = "   ";
        assertTrue(StringUtil.isBlank(string));
    }

    /**
     * Test isNumeric method with numeric string
     */
    @Test
    public void testIsNumericNumericString() {
        String string = "123";
        assertTrue(StringUtil.isNumeric(string));
    }

    /**
     * Test isNumeric method with non-numeric string
     */
    @Test
    public void testIsNumericNonNumericString() {
        String string = "abc";
        assertFalse(StringUtil.isNumeric(string));
    }

    /**
     * Test isWhitespace method with whitespace character
     */
    @Test
    public void testIsWhitespaceWhitespaceCharacter() {
        int c = ' ';
        assertTrue(StringUtil.isWhitespace(c));
    }

    /**
     * Test isWhitespace method with non-whitespace character
     */
    @Test
    public void testIsWhitespaceNonWhitespaceCharacter() {
        int c = 'a';
        assertFalse(StringUtil.isWhitespace(c));
    }

    /**
     * Test isActuallyWhitespace method with whitespace character
     */
    @Test
    public void testIsActuallyWhitespaceWhitespaceCharacter() {
        int c = ' ';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    /**
     * Test isActuallyWhitespace method with non-whitespace character
     */
    @Test
    public void testIsActuallyWhitespaceNonWhitespaceCharacter() {
        int c = 'a';
        assertFalse(StringUtil.isActuallyWhitespace(c));
    }

    /**
     * Test normaliseWhitespace method with empty string
     */
    @Test
    public void testNormaliseWhitespaceEmptyString() {
        String string = "";
        StringBuilder sb = StringUtil.stringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        String result = sb.toString();
        String expected = "";
        assertEquals(expected, result);
    }

    /**
     * Test normaliseWhitespace method with whitespace string
     */
    @Test
    public void testNormaliseWhitespaceWhitespaceString() {
        String string = "   test   ";
        StringBuilder sb = StringUtil.stringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        String result = sb.toString();
        String expected = " test ";
        assertEquals(expected, result);
    }

    /**
     * Test normaliseWhitespace method with non-whitespace string
     */
    @Test
    public void testNormaliseWhitespaceNonWhitespaceString() {
        String string = "test";
        StringBuilder sb = StringUtil.stringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        String result = sb.toString();
        String expected = "test";
        assertEquals(expected, result);
    }

    /**
     * Test in method with match found in array
     */
    @Test
    public void testInArrayMatchFound() {
        String needle = "test";
        String[] haystack = {"test1", "test2", "test"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    /**
     * Test in method with no match found in array
     */
    @Test
    public void testInArrayNoMatchFound() {
        String needle = "test4";
        String[] haystack = {"test1", "test2", "test3"};
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    /**
     * Test inSorted method with match found in sorted array
     */
    @Test
    public void testInSortedArrayMatchFound() {
        String needle = "test2";
        String[] haystack = {"test1", "test2", "test3"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    /**
     * Test inSorted method with no match found in sorted array
     */
    @Test
    public void testInSortedArrayNoMatchFound() {
        String needle = "test4";
        String[] haystack = {"test1", "test2", "test3"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    /**
     * Test resolve method with valid base and relative URLs
     */
    @Test
    public void testResolveValidBaseAndRelativeURLs() throws MalformedURLException {
        String baseUrl = "http://test.com/path/to/file";
        String relUrl = "/new/path/to/file2.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://test.com/new/path/to/file2.html";
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with invalid base and relative URLs
     */
    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidBaseAndRelativeURLs() throws MalformedURLException {
        String baseUrl = "http://test.com/path/to/file";
        String relUrl = "//test.com/path/to/file2.html";
        StringUtil.resolve(baseUrl, relUrl);
    }

}