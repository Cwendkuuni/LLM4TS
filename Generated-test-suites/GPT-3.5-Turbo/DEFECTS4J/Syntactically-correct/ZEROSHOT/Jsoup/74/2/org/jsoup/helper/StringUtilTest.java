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
    public void testJoinCollection() {
        Collection<String> collection = Arrays.asList("one", "two", "three");
        String result = StringUtil.join(collection, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> collection = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = collection.iterator();
        String result = StringUtil.join(iterator, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinArray() {
        String[] array = {"one", "two", "three"};
        String result = StringUtil.join(array, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlankForNullString() {
        boolean result = StringUtil.isBlank(null);
        assertTrue(result);
    }

    @Test
    public void testIsBlankForEmptyString() {
        boolean result = StringUtil.isBlank("");
        assertTrue(result);
    }

    @Test
    public void testIsBlankForWhitespaces() {
        boolean result = StringUtil.isBlank("     ");
        assertTrue(result);
    }

    @Test
    public void testIsBlankForNonEmptyString() {
        boolean result = StringUtil.isBlank("not blank");
        assertFalse(result);
    }

    @Test
    public void testIsNumericForNullString() {
        boolean result = StringUtil.isNumeric(null);
        assertFalse(result);
    }

    @Test
    public void testIsNumericForEmptyString() {
        boolean result = StringUtil.isNumeric("");
        assertFalse(result);
    }

    @Test
    public void testIsNumericForNonNumericString() {
        boolean result = StringUtil.isNumeric("123abc");
        assertFalse(result);
    }

    @Test
    public void testIsNumericForNumericString() {
        boolean result = StringUtil.isNumeric("123");
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceForSpaceCharacter() {
        boolean result = StringUtil.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceForTabCharacter() {
        boolean result = StringUtil.isWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceForNewLineCharacter() {
        boolean result = StringUtil.isWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceForFormFeedCharacter() {
        boolean result = StringUtil.isWhitespace('\f');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceForCarriageReturnCharacter() {
        boolean result = StringUtil.isWhitespace('\r');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceForSpaceCharacter() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceForTabCharacter() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceForNewLineCharacter() {
        boolean result = StringUtil.isActuallyWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceForFormFeedCharacter() {
        boolean result = StringUtil.isActuallyWhitespace('\f');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceForCarriageReturnCharacter() {
        boolean result = StringUtil.isActuallyWhitespace('\r');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceForNonBreakingSpaceCharacter() {
        boolean result = StringUtil.isActuallyWhitespace(160);
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("  this is  a test   ");
        assertEquals("this is a test", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  this is  a test   ", false);
        assertEquals("  this is  a test   ", sb.toString());
    }

    @Test
    public void testInForExistingElement() {
        boolean result = StringUtil.in("one", "one", "two", "three");
        assertTrue(result);
    }

    @Test
    public void testInForNonExistingElement() {
        boolean result = StringUtil.in("four", "one", "two", "three");
        assertFalse(result);
    }

    @Test
    public void testInSortedForExistingElement() {
        String[] array = {"one", "two", "three"};
        boolean result = StringUtil.inSorted("one", array);
        assertTrue(result);
    }

    @Test
    public void testInSortedForNonExistingElement() {
        String[] array = {"one", "two", "three"};
        boolean result = StringUtil.inSorted("four", array);
        assertFalse(result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String result = StringUtil.resolve(base, "path");
        assertEquals("https://www.example.com/path", result);
    }

    @Test
    public void testResolveForQuestionMarkPrefix() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String result = StringUtil.resolve(base, "?foo");
        assertEquals("https://www.example.com/path?foo", result);
    }

    @Test
    public void testResolveForDotPrefix() throws MalformedURLException {
        URL base = new URL("https://www.example.com/foo");
        String result = StringUtil.resolve(base, "./bar");
        assertEquals("https://www.example.com/foo/./bar", result);
    }

    @Test
    public void testResolveForInvalidUrls() {
        String result = StringUtil.resolve("https://www.example.com", ":");
        assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder result = StringUtil.stringBuilder();
        assertNotNull(result);
        assertEquals("", result.toString());
    }
}