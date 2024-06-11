package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.net.URL;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("test1", "test2", "test3");
        String expected = "test1,test2,test3";
        String result = StringUtil.join(strings, ",");
        assertEquals(expected, result);

        Iterator<String> iterator = Collections.singletonList("single").iterator();
        String resultIterator = StringUtil.join(iterator, ",");
        assertEquals("single", resultIterator);

        String emptyJoin = StringUtil.join(Collections.emptyList(), "");
        assertEquals("", emptyJoin);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> emptyIterator = Collections.emptyIterator();
        String result = StringUtil.join(emptyIterator, ",");
        assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"test1", "test2", "test3"};
        String expected = "test1,test2,test3";
        String result = StringUtil.join(strings, ",");
        assertEquals(expected, result);

        String[] emptyArray = {};
        String emptyArrayJoin = StringUtil.join(emptyArray, "");
        assertEquals("", emptyArrayJoin);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= StringUtil.padding.length; i++) {
            String paddingResult = StringUtil.padding(i);
            assertEquals(StringUtil.padding[i], paddingResult);
        }

        // test negative values
        try {
            StringUtil.padding(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] blankStrings = {"", " ", "   ", "    ", "     ", "      ", "       ", "        ", "         ",
                "          ", "           ", "            ", "             ", "              ", "               ", "                ",
                "                 ", "                  ", "                   ", "                    ", "                "};

        for (String string : blankStrings) {
            boolean result = StringUtil.isBlank(string);
            assertTrue(result);
        }

        String notBlank = "not blank";
        boolean result = StringUtil.isBlank(notBlank);
        assertFalse(result);

        String whitespaceOnly = "   ";
        boolean whitespaceOnlyIsBlank = StringUtil.isBlank(whitespaceOnly);
        assertTrue(whitespaceOnlyIsBlank);
    }

    @Test
    public void testIsNumeric() {
        String[] numericStrings = {"123", "1234567890", "0123456789012345678901234567890"};

        for (String string : numericStrings) {
            boolean result = StringUtil.isNumeric(string);
            assertTrue(result);
        }

        String notNumeric = "not numeric";
        boolean notNumericResult = StringUtil.isNumeric(notNumeric);
        assertFalse(notNumericResult);

        String emptyString = "";
        boolean emptyStringIsNumeric = StringUtil.isNumeric(emptyString);
        assertFalse(emptyStringIsNumeric);
    }

    @Test
    public void testIsWhitespace() {
        int[] whitespaceCodes = {32, 9, 10, 12, 13};
        for (int i : whitespaceCodes) {
            boolean result = StringUtil.isWhitespace(i);
            assertTrue(result);
        }

        int notWhitespaceCode = 65;
        boolean notWhitespaceResult = StringUtil.isWhitespace(notWhitespaceCode);
        assertFalse(notWhitespaceResult);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int[] whitespaceCodes = {32, 9, 10, 12, 13, 160};
        for (int c : whitespaceCodes) {
            boolean result = StringUtil.isActuallyWhitespace(c);
            assertTrue(result);
        }

        int notWhitespaceCode = 65;
        boolean notWhitespaceResult = StringUtil.isActuallyWhitespace(notWhitespaceCode);
        assertFalse(notWhitespaceResult);
    }

    @Test
    public void testNormaliseWhitespace() {
        String multipleSpaces = "test test   test";
        String expected = "test test test";
        String result = StringUtil.normaliseWhitespace(multipleSpaces);
        assertEquals(expected, result);

        String newlineTab = "test\n\ttest";
        String resultNewlineTab = StringUtil.normaliseWhitespace(newlineTab);
        assertEquals("test test", resultNewlineTab);

        String emptyString = "";
        String emptyStringNormalised = StringUtil.normaliseWhitespace(emptyString);
        assertEquals("", emptyStringNormalised);

        String whitespaceOnly = "   ";
        String whitespaceOnlyNormalised = StringUtil.normaliseWhitespace(whitespaceOnly);
        assertEquals("", whitespaceOnlyNormalised);
    }

    @Test
    public void testIn() {
        String[] strings = {"test1", "test2", "test3", "test4"};
        boolean result = StringUtil.in(null, strings);
        assertFalse(result);

        boolean result1 = StringUtil.in("test1", strings);
        assertTrue(result1);

        boolean result2 = StringUtil.in("test5", strings);
        assertFalse(result2);

        boolean result3 = StringUtil.in("test3", strings);
        assertTrue(result3);
    }

    @Test
    public void testInSorted() {
        String[] strings = {"test1", "test2", "test3", "test4"};
        boolean result = StringUtil.inSorted("test3", strings);
        assertTrue(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidBaseUrl() throws MalformedURLException {
        URL baseUrl = new URL("invalid://example.com");
        String relUrl = "";
        StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelUrl() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com");
        String relUrl = "invalid://example.com";
        StringUtil.resolve(baseUrl, relUrl);
    }

    @Test
    public void testResolveSimple() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com/path/to/file");
        String relUrl = "relative/path.html";
        String expected = "http://example.com/path/to/file/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithAbsoluteRelUrl() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com/path/to/file");
        String relUrl = "http://example.org/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(relUrl, result);
    }

    @Test
    public void testResolveWithFragment() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com/path/to/file#fragment");
        String relUrl = "#fragment2";
        String expected = "http://example.com/path/to/file#fragment2";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithTrailingSlash() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com/path/to/file/");
        String relUrl = "relative/path.html";
        String expected = "http://example.com/path/to/file/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithTrailingSlashInRelUrl() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com/path/to/file/");
        String relUrl = "relative/path.html/";
        String expected = "http://example.com/path/to/file/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithBaseAsRoot() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com");
        String relUrl = "/path/to/file";
        String expected = "http://example.com/path/to/file";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithBaseAsRootAndFragment() throws MalformedURLException {
        URL baseUrl = new URL("http://example.com#fragment");
        String relUrl = "/path/to/file";
        String expected = "http://example.com/path/to/file#fragment";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

}