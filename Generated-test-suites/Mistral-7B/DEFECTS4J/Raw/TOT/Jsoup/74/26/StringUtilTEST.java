package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

    @Test
    public void testJoinCollectionWithEmptyCollection() {
        List<String> emptyCollection = Collections.emptyList();
        assertEquals("", StringUtil.join(emptyCollection, ""));
    }

    @Test
    public void testJoinCollectionWithOneElement() {
        List<String> oneElementList = Arrays.asList("test");
        assertEquals("test", StringUtil.join(oneElementList, ""));
    }

    @Test
    public void testJoinCollectionWithMultipleElements() {
        List<String> multipleElementsList = Arrays.asList("test1", "test2", "test3");
        assertEquals("test1test2test3", StringUtil.join(multipleElementsList, ""));
    }

    @Test
    public void testJoinCollectionWithMultipleElementsAndSeparator() {
        List<String> multipleElementsList = Arrays.asList("test1", "test2", "test3");
        assertEquals("test1-test2-test3", StringUtil.join(multipleElementsList, "-"));
    }

    @Test
    public void testJoinIteratorWithEmptyIterator() {
        String emptyIterator = StringUtil.join(Collections.emptyIterator(), "");
        assertEquals("", emptyIterator);
    }

    @Test
    public void testJoinIteratorWithOneElement() {
        Iterator<String> oneElementIterator = Arrays.asList("test").iterator();
        assertEquals("test", StringUtil.join(oneElementIterator, ""));
    }

    @Test
    public void testJoinIteratorWithMultipleElements() {
        Iterator<String> multipleElementsIterator = Arrays.asList("test1", "test2", "test3").iterator();
        assertEquals("test1test2test3", StringUtil.join(multipleElementsIterator, ""));
    }

    @Test
    public void testJoinIteratorWithMultipleElementsAndSeparator() {
        Iterator<String> multipleElementsIterator = Arrays.asList("test1", "test2", "test3").iterator();
        assertEquals("test1-test2-test3", StringUtil.join(multipleElementsIterator, "-"));
    }

    @Test
    public void testJoinArrayWithEmptyArray() {
        String[] emptyArray = {};
        assertEquals("", StringUtil.join(emptyArray, ""));
    }

    @Test
    public void testJoinArrayWithOneElement() {
        String[] oneElementArray = {"test"};
        assertEquals("test", StringUtil.join(oneElementArray, ""));
    }

    @Test
    public void testJoinArrayWithMultipleElements() {
        String[] multipleElementsArray = {"test1", "test2", "test3"};
        assertEquals("test1test2test3", StringUtil.join(multipleElementsArray, ""));
    }

    @Test
    public void testJoinArrayWithMultipleElementsAndSeparator() {
        String[] multipleElementsArray = {"test1", "test2", "test3"};
        assertEquals("test1-test2-test3", StringUtil.join(multipleElementsArray, "-"));
    }

    @Test
    public void testPaddingZero() {
        assertEquals("", StringUtil.padding(0));
    }

    @Test
    public void testPaddingPositive() {
        assertEquals("         ", StringUtil.padding(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegative() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmpty() {
        assertTrue(StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlankWhitespace() {
        assertTrue(StringUtil.isBlank("   "));
    }

    @Test
    public void testIsBlankNonWhitespace() {
        assertFalse(StringUtil.isBlank("test"));
    }

    @Test
    public void testIsNumericEmpty() {
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericDigits() {
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsNumericNonDigits() {
        assertFalse(StringUtil.isNumeric("123aBc"));
    }

    @Test
    public void testIsWhitespaceSpace() {
        assertTrue(StringUtil.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        assertTrue(StringUtil.isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewline() {
        assertTrue(StringUtil.isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        assertTrue(StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsWhitespaceReturn() {
        assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
    }

    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
    }

    @Test
    public void testIsActuallyWhitespaceReturn() {
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceNbsp() {
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0'));
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        assertEquals(" ", StringUtil.normaliseWhitespace(" "));
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        assertEquals(" ", StringUtil.normaliseWhitespace("  "));
    }

    @Test
    public void testNormaliseWhitespaceLeadingSpace() {
        assertEquals("test ", StringUtil.normaliseWhitespace(" test "));
    }

    @Test
    public void testNormaliseWhitespaceTrailingSpace() {
        assertEquals("test ", StringUtil.normaliseWhitespace(" test "));
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingSpace() {
        assertEquals("test", StringUtil.normaliseWhitespace(" test "));
    }

    @Test
    public void testNormaliseWhitespaceNewline() {
        assertEquals("", StringUtil.normaliseWhitespace("\n"));
    }

    @Test
    public void testNormaliseWhitespaceTab() {
        assertEquals("", StringUtil.normaliseWhitespace("\t"));
    }

    @Test
    public void testNormaliseWhitespaceFormFeed() {
        assertEquals("", StringUtil.normaliseWhitespace("\f"));
    }

    @Test
    public void testNormaliseWhitespaceReturn() {
        assertEquals("", StringUtil.normaliseWhitespace("\r"));
    }

    @Test
    public void testNormaliseWhitespaceNbsp() {
        assertEquals("", StringUtil.normaliseWhitespace("\u00A0"));
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        assertEquals("test test", StringUtil.normaliseWhitespace(" test  test "));
    }

    @Test
    public void testInEmptyArray() {
        assertFalse(StringUtil.in("needle", new String[0]));
    }

    @Test
    public void testInSingleElementArray() {
        assertTrue(StringUtil.in("needle", new String[]{"needle"}));
    }

    @Test
    public void testInMultipleElementsArray() {
        assertTrue(StringUtil.in("needle0", new String[]{"needle1", "needle0"}));
        assertFalse(StringUtil.in("needle2", new String[]{"needle1", "needle0"}));
    }

    @Test
    public void testInSortedArray() {
        assertTrue(StringUtil.inSorted("needle0", new String[]{"needle1", "needle0"}));
        assertFalse(StringUtil.inSorted("needle2", new String[]{"needle1", "needle0"}));
    }

    @Test
    public void testResolveBaseNull() {
        assertNull(StringUtil.resolve(null, "relative"));
    }

    @Test
    public void testResolveBaseEmpty() {
        assertEquals("", StringUtil.resolve("", "relative"));
    }

    @Test
    public void testResolveBaseAbsolute() {
        String baseUrl = "http://example.com/path/to/base";
        String relUrl = "relative";
        String expectedResult = "http://example.com/path/to/base/relative";
        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testResolveBaseRelativeToAbsolute() {
        String baseUrl = "http://example.com/";
        String relUrl = "./path/to/relative";
        String expectedResult = "http://example.com/path/to/relative";
        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testResolveBaseRelativeToRelative() {
        String baseUrl = "http://example.com/path/to/base";
        String relUrl = "../path/to/relative";
        String expectedResult = "http://example.com/path/to/relative";
        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testResolveBaseRelativeToAnotherRelative() {
        String baseUrl = "http://example.com/path/to/base";
        String relUrl1 = "../path/to/relative1";
        String relUrl2 = "./path/to/relative2";
        String expectedResult = "http://example.com/path/to/relative1/path/to/relative2";
        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl1, relUrl2));
    }

    @Test
    public void testResolveBaseAbsoluteToAnotherAbsolute() {
        String baseUrl = "http://example.com/path/to/base";
        String relUrl1 = "http://example.com/another/path/to/relative1";
        String relUrl2 = "http://example.com/another/path/to/relative2";
        String expectedResult = "http://example.com/another/path/to/relative1/http://example.com/another/path/to/relative2";
        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl1, relUrl2));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformedURL() {
        String baseUrl = "http://example.com/path/to/base";
        String relUrl = "malformed://relative";
        String expectedResult = "";
        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl));
    }
}