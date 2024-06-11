package org.jsoup.helper;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void join_collectionOfStrings_joinedStringWithSeparator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";

        String result = StringUtil.join(strings, sep);

        assertEquals("Hello-World", result);
    }

    @Test
    public void join_iteratorOfStrings_joinedStringWithSeparator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String sep = "-";

        String result = StringUtil.join(strings, sep);

        assertEquals("Hello-World", result);
    }

    @Test
    public void join_arrayOfStrings_joinedStringWithSeparator() {
        String[] strings = {"Hello", "World"};
        String sep = "-";

        String result = StringUtil.join(strings, sep);

        assertEquals("Hello-World", result);
    }

    @Test
    public void padding_validWidth_stringOfSpaces() {
        int width = 5;

        String result = StringUtil.padding(width);

        assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void padding_negativeWidth_throwIllegalArgumentException() {
        int width = -5;

        StringUtil.padding(width);
    }

    @Test
    public void isBlank_nullString_returnTrue() {
        String string = null;

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void isBlank_emptyString_returnTrue() {
        String string = "";

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void isBlank_whitespaceString_returnTrue() {
        String string = "   ";

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void isBlank_nonBlankString_returnFalse() {
        String string = "Hello";

        boolean result = StringUtil.isBlank(string);

        assertFalse(result);
    }

    @Test
    public void isNumeric_nullString_returnFalse() {
        String string = null;

        boolean result = StringUtil.isNumeric(string);

        assertFalse(result);
    }

    @Test
    public void isNumeric_emptyString_returnFalse() {
        String string = "";

        boolean result = StringUtil.isNumeric(string);

        assertFalse(result);
    }

    @Test
    public void isNumeric_onlyDigitChars_returnTrue() {
        String string = "12345";

        boolean result = StringUtil.isNumeric(string);

        assertTrue(result);
    }

    @Test
    public void isNumeric_containsNonDigitChars_returnFalse() {
        String string = "12345a";

        boolean result = StringUtil.isNumeric(string);

        assertFalse(result);
    }

    @Test
    public void isWhitespace_whitespaceCodePoint_returnTrue() {
        int c = ' ';

        boolean result = StringUtil.isWhitespace(c);

        assertTrue(result);
    }

    @Test
    public void isWhitespace_nonWhitespaceCodePoint_returnFalse() {
        int c = 'H';

        boolean result = StringUtil.isWhitespace(c);

        assertFalse(result);
    }

    @Test
    public void isActuallyWhitespace_whitespaceCodePoint_returnTrue() {
        int c = ' ';

        boolean result = StringUtil.isActuallyWhitespace(c);

        assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_nonWhitespaceCodePoint_returnFalse() {
        int c = 'H';

        boolean result = StringUtil.isActuallyWhitespace(c);

        assertFalse(result);
    }

    @Test
    public void normaliseWhitespace_multipleSpacesAndWhitespaceCharacters_collapseToSingleSpace() {
        String string = "Hello \r\n   World\t";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals("Hello World ", result);
    }

    @Test
    public void appendNormalisedWhitespace_multipleSpacesAndWhitespaceCharacters_collapseToSingleSpace() {
        StringBuilder accum = new StringBuilder();
        String string = "Hello \r\n   World\t";

        StringUtil.appendNormalisedWhitespace(accum, string, false);

        assertEquals("Hello World ", accum.toString());
    }

    @Test
    public void in_matchFound_returnTrue() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};

        boolean result = StringUtil.in(needle, haystack);

        assertTrue(result);
    }

    @Test
    public void in_matchNotFound_returnFalse() {
        String needle = "Hello";
        String[] haystack = {"World"};

        boolean result = StringUtil.in(needle, haystack);

        assertFalse(result);
    }

    @Test
    public void inSorted_matchFound_returnTrue() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};

        boolean result = StringUtil.inSorted(needle, haystack);

        assertTrue(result);
    }

    @Test
    public void inSorted_matchNotFound_returnFalse() {
        String needle = "Hello";
        String[] haystack = {"World"};

        boolean result = StringUtil.inSorted(needle, haystack);

        assertFalse(result);
    }

    @Test
    public void resolve_validUrls_resolveToAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("https://example.com/");
        String relUrl = "path/page.html";
        String expected = "https://example.com/path/page.html";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(expected, result.toString());
    }

    @Test
    public void resolve_invalidBaseUrl_returnEmptyString() {
        String baseUrl = "invalid";
        String relUrl = "path/page.html";
        String expected = "";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals(expected, result);
    }

    @Test
    public void stringBuilder_maxCacheSize_notExceededMaxSize() {
        int expectedMaxSize = 8 * 1024;

        StringBuilder sb = StringUtil.stringBuilder();

        assertEquals(expectedMaxSize, sb.capacity());
    }
}