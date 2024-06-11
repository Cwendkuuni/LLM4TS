package org.jsoup.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jsoup.helper.StringUtil;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("a", "b", "c");
        String expected = "a b c";
        String actual = StringUtil.join(strings, " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinCollectionEmpty() {
        List<String> strings = Collections.emptyList();
        String actual = StringUtil.join(strings, " ");
        assertEquals("", actual);
    }

    @Test
    public void testJoinCollectionSingleElement() {
        String string = "a";
        String expected = "a";
        String actual = StringUtil.join(Collections.singletonList(string), " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"a", "b", "c"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "a b c";
        String actual = StringUtil.join(iterator, " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> iterator = Collections.emptyIterator();
        String actual = StringUtil.join(iterator, " ");
        assertEquals("", actual);
    }

    @Test
    public void testJoinIteratorSingleElement() {
        String string = "a";
        String expected = "a";
        String actual = StringUtil.join(Collections.singleton(string).iterator(), " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"a", "b", "c"};
        String expected = "a b c";
        String actual = StringUtil.join(strings, " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String actual = StringUtil.join(strings, " ");
        assertEquals("", actual);
    }

    @Test
    public void testJoinArraySingleElement() {
        String string = "a";
        String expected = "a";
        String actual = StringUtil.join(new String[]{string}, " ");
        assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        String actual = StringUtil.padding(5);
        String expected = "     ";
        assertEquals(expected, actual);

        String actual2 = StringUtil.padding(-5);
        assertNull(actual2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        boolean actual = StringUtil.isBlank(null);
        assertTrue(actual);
    }

    @Test
    public void testIsBlankEmpty() {
        String actual = StringUtil.isBlank("");
        assertTrue(actual);
    }

    @Test
    public void testIsBlankWhitespace() {
        String actual = StringUtil.isBlank("   ");
        assertTrue(actual);
    }

    @Test
    public void testIsBlankNonBlank() {
        String actual = StringUtil.isBlank("abc");
        assertFalse(actual);
    }

    @Test
    public void testIsNumericTrue() {
        String actual = StringUtil.isNumeric("123");
        assertTrue(actual);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String actual = StringUtil.isNumeric("");
        assertFalse(actual);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String actual = StringUtil.isNumeric("abc");
        assertFalse(actual);
    }

    @Test
    public void testIsWhitespaceTrue() {
        int codePoint = ' ';
        boolean actual = StringUtil.isWhitespace(codePoint);
        assertTrue(actual);
    }

    @Test
    public void testIsWhitespaceFalse() {
        int codePoint = 'x';
        boolean actual = StringUtil.isWhitespace(codePoint);
        assertFalse(actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrue() {
        int codePoint = ' ';
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        assertTrue(actual);
    }

    @Test
    public void testIsActuallyWhitespaceFalse() {
        int codePoint = 'x';
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        assertFalse(actual);
    }

    @Test
    public void testIsActuallyWhitespaceNbsp() {
        int codePoint = 160;
        boolean actual = StringUtil.isActuallyWhitespace(codePoint);
        assertTrue(actual);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String actual = StringUtil.normaliseWhitespace("");
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String actual = StringUtil.normaliseWhitespace(" ");
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String actual = StringUtil.normaliseWhitespace("   ");
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceSingleLineBreak() {
        String actual = StringUtil.normaliseWhitespace("\n");
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleLineBreaks() {
        String actual = StringUtil.normaliseWhitespace("\n\n");
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceTab() {
        String actual = StringUtil.normaliseWhitespace("\t");
        assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String actual = StringUtil.normaliseWhitespace("  \t\nfoo  bar\n baz ");
        assertEquals("foo bar baz", actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed2() {
        String actual = StringUtil.normaliseWhitespace("foo\nbar\nbaz");
        assertEquals("foo bar baz", actual);
    }

    @Test
    public void testInArray() {
        String[] haystack = new String[]{"a", "b", "c"};
        String needle = "a";
        boolean actual = StringUtil.in(needle, haystack);
        assertTrue(actual);
    }

    @Test
    public void testInArrayFalse() {
        String[] haystack = new String[]{"a", "b", "c"};
        String needle = "d";
        boolean actual = StringUtil.in(needle, haystack);
        assertFalse(actual);
    }

    @Test
    public void testInSortedArray() {
        String[] haystack = new String[]{"a", "b", "c"};
        String needle = "a";
        boolean actual = StringUtil.inSorted(needle, haystack);
        assertTrue(actual);
    }

    @Test
    public void testInSortedArrayFalse() {
        String[] haystack = new String[]{"a", "b", "c"};
        String needle = "d";
        boolean actual = StringUtil.inSorted(needle, haystack);
        assertFalse(actual);
    }

    @Test
    public void testResolveAbsolute() throws MalformedURLException {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "/relative/path.html";
        URL actual = StringUtil.resolve(new URL(base), relUrl);
        String expected = "http://www.example.com/relative/path.html";
        assertEquals(expected, actual.toExternalForm());
    }

    @Test
    public void testResolveAbsoluteWithAbsoluteRelUrl() throws MalformedURLException {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "http://www.example.com/another/path.html";
        URL actual = StringUtil.resolve(new URL(base), relUrl);
        String expected = "http://www.example.com/another/path.html";
        assertEquals(expected, actual.toExternalForm());
    }

    @Test
    public void testResolveAbsoluteWithEmptyBase() throws MalformedURLException {
        String base = "";
        String relUrl = "/relative/path.html";
        URL actual = StringUtil.resolve(new URL(""), relUrl);
        String expected = "/relative/path.html";
        assertEquals(expected, actual.toExternalForm());
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveAbsoluteInvalidBase() throws MalformedURLException {
        String base = "http:/www.example.com/path/to/file.html";
        String relUrl = "/relative/path.html";
        StringUtil.resolve(new URL(base), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveAbsoluteInvalidRelUrl() throws MalformedURLException {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "/http://www.example.com/another/path.html";
        StringUtil.resolve(new URL(base), relUrl);
    }

    @Test
    public void testResolveEmpty() {
        String base = "";
        String relUrl = "http://www.example.com/path/to/file.html";
        String actual = StringUtil.resolve(base, relUrl);
        assertEquals("", actual);
    }

    @Test
    public void testResolveNullBase() {
        String relUrl = "http://www.example.com/path/to/file.html";
        String actual = StringUtil.resolve(null, relUrl);
        assertNull(actual);
    }

    @Test
    public void testResolveNullRelUrl() {
        String base = "http://www.example.com/path/to/file.html";
        String actual = StringUtil.resolve(base, null);
        assertEquals("", actual);
    }

    @Test
    public void testGetStringBuilder() {
        StringBuilder builder = StringUtil.stringBuilder();
        assertEquals("", builder.toString());

        builder.append("hello");
        assertEquals("hello", builder.toString());
        builder.setLength(0);
        assertEquals("", builder.toString());

        StringBuilder builder2 = StringUtil.stringBuilder();
        assertSame(builder, builder2);
    }
}