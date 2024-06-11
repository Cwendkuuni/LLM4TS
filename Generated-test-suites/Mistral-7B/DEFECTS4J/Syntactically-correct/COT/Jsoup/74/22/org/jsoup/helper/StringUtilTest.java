package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world");
        String expected = "helloworld";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = Collections.emptyList();
        String result = StringUtil.join(strings, "");
        assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("hello", "world");
        Iterator<String> iterator = strings.iterator();
        String expected = "helloworld";
        String result = StringUtil.join(iterator, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world"};
        String expected = "helloworld";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testPaddingNegativeWidth() {
        try {
            String result = StringUtil.padding(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPaddingLongWidth() {
        String expected = "                                 ";
        String result = StringUtil.padding(15);
        assertEquals(expected, result);
    }

    @Test
    public void testIsBlankNullString() {
        boolean result = StringUtil.isBlank(null);
        assertTrue(result);
    }

    @Test
    public void testIsBlankEmptyString() {
        boolean result = StringUtil.isBlank("");
        assertTrue(result);
    }

    @Test
    public void testIsBlankStringWithWhitespace() {
        boolean result = StringUtil.isBlank("   ");
        assertTrue(result);
    }

    @Test
    public void testIsBlankStringWithNonWhitespace() {
        boolean result = StringUtil.isBlank("hello");
        assertFalse(result);
    }

    @Test
    public void testIsNumericEmptyString() {
        boolean result = StringUtil.isNumeric("");
        assertFalse(result);
    }

    @Test
    public void testIsNumericNullString() {
        boolean result = StringUtil.isNumeric(null);
        assertFalse(result);
    }

    @Test
    public void testIsNumericStringWithDigits() {
        boolean result = StringUtil.isNumeric("123456");
        assertTrue(result);
    }

    @Test
    public void testIsNumericStringWithNonDigits() {
        boolean result = StringUtil.isNumeric("123abcd");
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean result = StringUtil.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean result = StringUtil.isWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceNewline() {
        boolean result = StringUtil.isWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        boolean result = StringUtil.isWhitespace('\f');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        boolean result = StringUtil.isWhitespace('\r');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean result = StringUtil.isActuallyWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        boolean result = StringUtil.isActuallyWhitespace('\f');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        boolean result = StringUtil.isActuallyWhitespace('\r');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        boolean result = StringUtil.isActuallyWhitespace(160);
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespaceEmptyString() {
        String result = StringUtil.normaliseWhitespace("");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithSingleSpace() {
        String result = StringUtil.normaliseWhitespace(" ");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithMultipleSpaces() {
        String result = StringUtil.normaliseWhitespace("   ");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithTab() {
        String result = StringUtil.normaliseWhitespace("\t");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithNewline() {
        String result = StringUtil.normaliseWhitespace("\n");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithFormFeed() {
        String result = StringUtil.normaliseWhitespace("\f");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithCarriageReturn() {
        String result = StringUtil.normaliseWhitespace("\r");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithNonBreakingSpace() {
        String result = StringUtil.normaliseWhitespace("\u00a0");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithWhitespaceCharactersAndText() {
        String result = StringUtil.normaliseWhitespace("   hello   \nworld");
        assertEquals("hello world", result);
    }

    @Test
    public void testNormaliseWhitespaceStringWithLeadingWhitespace() {
        String result = StringUtil.normaliseWhitespace("   hello   ");
        assertEquals("hello", result);
    }

    @Test
    public void testIsInArrayNullNeedle() {
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.in(null, haystack);
        assertFalse(result);
    }

    @Test
    public void testIsInArrayEmptyNeedle() {
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.in("", haystack);
        assertFalse(result);
    }

    @Test
    public void testIsInArrayEmptyHaystack() {
        String needle = "hello";
        boolean result = StringUtil.in(needle, new String[]{});
        assertFalse(result);
    }

    @Test
    public void testIsInArrayNeedleInHaystack() {
        String needle = "hello";
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testIsInArrayNeedleNotInHaystack() {
        String needle = "world";
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testIsInSortedNullNeedle() {
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.inSorted(null, haystack);
        assertFalse(result);
    }

    @Test
    public void testIsInSortedEmptyNeedle() {
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.inSorted("", haystack);
        assertFalse(result);
    }

    @Test
    public void testIsInSortedEmptyHaystack() {
        String needle = "hello";
        boolean result = StringUtil.inSorted(needle, new String[]{});
        assertFalse(result);
    }

    @Test
    public void testIsInSortedNeedleInSortedHaystack() {
        String needle = "hello";
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testIsInSortedNeedleNotInSortedHaystack() {
        String needle = "world";
        String[] haystack = new String[]{"hello", "world"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testIsInSortedNeedleInMiddleOfSortedHaystack() {
        String needle = "world";
        String[] haystack = new String[]{"hello", "world", "example"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testResolveNullBaseUrl() {
        String relUrl = "http://example.com";
        String result = StringUtil.resolve(null, relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveNullRelUrl() {
        String baseUrl = "http://example.com";
        String result = StringUtil.resolve(baseUrl, null);
        assertEquals(baseUrl, result);
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        String relUrl = "";
        String result = StringUtil.resolve("", relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveBaseUrlWithRelativeUrl() {
        String baseUrl = "http://example.com";
        String relUrl = "/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path/to/file.html", result);
    }

    @Test
    public void testResolveBaseUrlWithAbsoluteUrl() {
        String baseUrl = "http://example.com";
        String relUrl = "http://example2.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(relUrl, result);
    }

    @Test
    public void testResolveBaseUrlWithRelativeUrlWithQuery() {
        String baseUrl = "http://example.com";
        String relUrl = "/path/to/file.html?query=param";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path/to/file.html?query=param", result);
    }

    @Test
    public void testResolveBaseUrlWithRelativeUrlWithFragment() {
        String baseUrl = "http://example.com";
        String relUrl = "/path/to/file.html#fragment";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path/to/file.html#fragment", result);
    }

}