package org.jsoup.helper.test;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Test class for StringUtil class
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("one", "two", "three");
        String expected = "onetwothree";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = strings.iterator();
        String expected = "onetwothree";
        String result = StringUtil.join(iterator, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"one", "two", "three"};
        String expected = "onetwothree";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        String expected = "          ";
        String result = StringUtil.padding(8);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlank() {
        String emptyString = "";
        String whitespaceString = "   ";
        String nonEmptyString = "non-empty";
        String nullString = null;
        boolean result;

        result = StringUtil.isBlank(emptyString);
        assertTrue(result);

        result = StringUtil.isBlank(whitespaceString);
        assertTrue(result);

        result = StringUtil.isBlank(nonEmptyString);
        assertFalse(result);

        result = StringUtil.isBlank(nullString);
        assertFalse(result);
    }

    @Test
    public void testIsNumeric() {
        String emptyString = "";
        String oneNumber = "1";
        String multipleNumbers = "123";
        String nonNumber = "non-number";
        String nullString = null;
        boolean result;

        result = StringUtil.isNumeric(emptyString);
        assertFalse(result);

        result = StringUtil.isNumeric(oneNumber);
        assertTrue(result);

        result = StringUtil.isNumeric(multipleNumbers);
        assertTrue(result);

        result = StringUtil.isNumeric(nonNumber);
        assertFalse(result);

        result = StringUtil.isNumeric(nullString);
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        int space = ' ';
        int tab = '\t';
        int lineFeed = '\n';
        int formFeed = '\f';
        int carriageReturn = '\r';
        boolean result;

        result = StringUtil.isWhitespace(space);
        assertTrue(result);

        result = StringUtil.isWhitespace(tab);
        assertTrue(result);

        result = StringUtil.isWhitespace(lineFeed);
        assertTrue(result);

        result = StringUtil.isWhitespace(formFeed);
        assertTrue(result);

        result = StringUtil.isWhitespace(carriageReturn);
        assertTrue(result);

        result = StringUtil.isWhitespace('\u00A0'); // non-breaking space
        assertTrue(result);

        result = StringUtil.isWhitespace('A');
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int space = ' ';
        int tab = '\t';
        int lineFeed = '\n';
        int formFeed = '\f';
        int carriageReturn = '\r';
        int nonBreakingSpace = '\u00A0';
        boolean result;

        result = StringUtil.isActuallyWhitespace(space);
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace(tab);
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace(lineFeed);
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace(formFeed);
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace(carriageReturn);
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace(nonBreakingSpace);
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "\t one\n two\r three\f four\n five \t six\r seven\n eight";
        String expected = " one two three four five six seven eight";
        String result = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String haystack = "one,two,three,four,five";
        String needle = "two";
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack.split(","));
        assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String haystack = "one,two,three,four,five";
        String needle = "two";
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack.split(","));
        assertEquals(expected, result);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/";
        String relUrl = "relative.html";
        String expected = "http://example.com/relative.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveNullBaseUrl() {
        String relUrl = "relative.html";
        String expected = "";
        String result = StringUtil.resolve(null, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveNullRelUrl() {
        String baseUrl = "http://example.com/";
        String expected = baseUrl;
        String result = StringUtil.resolve(baseUrl, null);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        String relUrl = "relative.html";
        String expected = "";
        String result = StringUtil.resolve("", relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveEmptyRelUrl() {
        String baseUrl = "http://example.com/";
        String expected = baseUrl;
        String result = StringUtil.resolve(baseUrl, "");
        assertEquals(expected, result);
    }

    @Test
    public void testResolveInvalidRelUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "../";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "://sub.domain.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedBaseUrl() {
        String relUrl = "relative.html";
        String result = StringUtil.resolve(null, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedRelUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "://sub.domain.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }
}