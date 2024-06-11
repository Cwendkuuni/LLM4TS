package org.jsoup.helper;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.net.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> stringList = Arrays.asList("one", "two", "three");
        String result = StringUtil.join(stringList, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> stringList = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = stringList.iterator();
        String result = StringUtil.join(iterator, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinArray() {
        String[] stringArray = {"one", "two", "three"};
        String result = StringUtil.join(stringArray, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        String blankString = "";
        assertTrue(StringUtil.isBlank(blankString));
    }

    @Test
    public void testIsBlankWithWhitespace() {
        String whitespaceString = "   ";
        assertTrue(StringUtil.isBlank(whitespaceString));
    }

    @Test
    public void testIsBlankWithNull() {
        String nullString = null;
        assertTrue(StringUtil.isBlank(nullString));
    }

    @Test
    public void testIsNotBlank() {
        String nonBlankString = "Hello";
        assertFalse(StringUtil.isBlank(nonBlankString));
    }

    @Test
    public void testIsNumeric() {
        String numericString = "123";
        assertTrue(StringUtil.isNumeric(numericString));
    }

    @Test
    public void testIsNumericWithEmptyString() {
        String emptyString = "";
        assertFalse(StringUtil.isNumeric(emptyString));
    }

    @Test
    public void testIsNumericWithNull() {
        String nullString = null;
        assertFalse(StringUtil.isNumeric(nullString));
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        String nonNumericString = "abc";
        assertFalse(StringUtil.isNumeric(nonNumericString));
    }

    @Test
    public void testIsWhitespace() {
        int whitespaceCharacter = ' ';
        assertTrue(StringUtil.isWhitespace(whitespaceCharacter));
    }

    @Test
    public void testIsNotWhitespace() {
        int nonWhitespaceCharacter = 'a';
        assertFalse(StringUtil.isWhitespace(nonWhitespaceCharacter));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int whitespaceCharacter = ' ';
        assertTrue(StringUtil.isActuallyWhitespace(whitespaceCharacter));
    }

    @Test
    public void testIsActuallyWhitespaceWithNonBreakingSpace() {
        int nonBreakingSpaceCharacter = 160;
        assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpaceCharacter));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   Hello   World   ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(" Hello World ", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        String string = "   Hello   World   ";
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        String result = sb.toString();
        assertEquals(" Hello World ", result);
    }

    @Test
    public void testIn() {
        String needle = "one";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInWithNonExistingElement() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testInSorted() {
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedWithNonExistingElement() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com/base/");
        String relUrl = "./page.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/page.html", result.toString());
    }

    @Test
    public void testResolveWithAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("https://www.example.com/base/");
        String relUrl = "https://www.example.com/absolute.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/absolute.html", result.toString());
    }

    @Test
    public void testResolveWithStringBase() {
        String baseUrl = "https://www.example.com/base/";
        String relUrl = "./page.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/base/page.html", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
    }

    @Test
    public void testInitalizeStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertEquals(0, sb.length());
    }
}
