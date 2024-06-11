package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("one", "two", "three");
        String expected = "one two three";
        String result = StringUtil.join(list, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        String result = StringUtil.join(Collections.emptyIterator(), " ");
        assertEquals("", result);
    }

    @Test
    public void testJoinIteratorOneElement() {
        Iterator<String> iterator = Arrays.asList("one").iterator();
        String expected = "one";
        String result = StringUtil.join(iterator, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorMultipleElements() {
        List<String> list = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = list.iterator();
        String expected = "one two three";
        String result = StringUtil.join(iterator, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String expected = "";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayOneElement() {
        String[] strings = {"one"};
        String expected = "one";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayMultipleElements() {
        String[] strings = {"one", "two", "three"};
        String expected = "one two three";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String result = StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        boolean result = StringUtil.isBlank(null);
        assertTrue(result);
    }

    @Test
    public void testIsBlankEmpty() {
        String string = "";
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespace() {
        String string = "   ";
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankNonBlank() {
        String string = "abc";
        boolean result = StringUtil.isBlank(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericEmpty() {
        String string = "";
        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericDigits() {
        String string = "123";
        boolean result = StringUtil.isNumeric(string);
        assertTrue(result);
    }

    @Test
    public void testIsNumericNonDigits() {
        String string = "abcd";
        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceSpace() {
        int result = StringUtil.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceTab() {
        int result = StringUtil.isWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceNewline() {
        int result = StringUtil.isWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFalse() {
        int result = StringUtil.isWhitespace('a');
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        int result = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        int result = StringUtil.isActuallyWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        int result = StringUtil.isActuallyWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNonWhitespace() {
        int result = StringUtil.isActuallyWhitespace('a');
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        int result = StringUtil.isActuallyWhitespace((int) 160);
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String result = StringUtil.normaliseWhitespace("");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String result = StringUtil.normaliseWhitespace(" ");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String result = StringUtil.normaliseWhitespace("   ");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSingleTab() {
        String result = StringUtil.normaliseWhitespace("\t");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleTabs() {
        String result = StringUtil.normaliseWhitespace("\t\t");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceNewline() {
        String result = StringUtil.normaliseWhitespace("\n");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleNewlines() {
        String result = StringUtil.normaliseWhitespace("\n\n");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSpaceTabMixed() {
        String result = StringUtil.normaliseWhitespace(" a\tb c ");
        assertEquals("a c", result);
    }

    @Test
    public void testNormaliseWhitespaceNonBreakingSpace() {
        String result = StringUtil.normaliseWhitespace("a\u00A0b");
        assertEquals("a b", result);
    }

    @Test
    public void testInNullNeedle() {
        boolean result = StringUtil.in(null, "one", "two", "three");
        assertFalse(result);
    }

    @Test
    public void testInNullHaystack() {
        boolean result = StringUtil.in("needle", null);
        assertFalse(result);
    }

    @Test
    public void testInEmptyHaystack() {
        boolean result = StringUtil.in("needle", new String[0]);
        assertFalse(result);
    }

    @Test
    public void testInEmptyNeedle() {
        boolean result = StringUtil.in("", "one", "two", "three");
        assertFalse(result);
    }

    @Test
    public void testInMatch() {
        boolean result = StringUtil.in("one", "one", "two", "three");
        assertTrue(result);
    }

    @Test
    public void testInNoMatch() {
        boolean result = StringUtil.in("two", "one", "two", "three");
        assertFalse(result);
    }

    @Test
    public void testInSortedMatch() {
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted("one", haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedNoMatch() {
        String[] haystack = {"one", "two", "three"};
        boolean result = StringUtil.inSorted("four", haystack);
        assertFalse(result);
    }

    @Test
    public void testResolveBaseNull() {
        String result = StringUtil.resolve(null, "relative");
        assertEquals("", result);
    }

    @Test
    public void testResolveBaseAbsolute() {
        String base = "http://example.com/path/to/base";
        String relUrl = "relative";
        String expected = "http://example.com/path/to/base/relative";
        String result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelativeOnly() {
        String base = "http://example.com/path/to/base";
        String relUrl = "./relative";
        String expected = "http://example.com/path/to/base/relative";
        String result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseAbsRelative() {
        String base = "http://example.com/path/to/base";
        String relUrl = "/relative";
        String expected = "http://example.com/relative";
        String result = StringUtil.resolve(base, relUrl);
        assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformedUrl() {
        String base = "http://example.com/path/to/base%";
        String relUrl = "relative";
        String result = StringUtil.resolve(base, relUrl);
    }

    @Test
    public void testResolveBaseMalformedUrlRelative() {
        String base = "http://example.com/path/to/base%";
        String relUrl = "relative";
        try {
            String result = StringUtil.resolve(base, relUrl);
        } catch (MalformedURLException e) {
            // expected
        }
        assertEquals("", result);
    }

    @Test
    public void testStringBuilderCached() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();

        sb1.append("hello");
        sb2.append("world");
        sb3.append("!");

        StringBuilder sb4 = StringUtil.stringBuilder();
        sb4.append(sb1);
        sb4.append(sb2);
        sb4.append(sb3);

        String expected = "helloworld!";
        String result = sb4.toString();
        assertEquals(expected, result);

        // verify the StringBuilder instances were not modified
        assertEquals("hello", sb1.toString());
        assertEquals("world", sb2.toString());
        assertEquals("!", sb3.toString());
        assertEquals("", sb4.toString());
    }
}