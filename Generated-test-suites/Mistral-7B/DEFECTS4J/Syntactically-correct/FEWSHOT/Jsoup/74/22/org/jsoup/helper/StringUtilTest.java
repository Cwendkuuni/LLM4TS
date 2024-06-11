package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    // Test join method

    @Test
    public void testJoinCollection() {
        List<String> stringList = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo, bar, baz", StringUtil.join(stringList, ", "));
    }

    @Test
    public void testJoinIterator() {
        String[] stringArray = new String[] {"foo", "bar", "baz"};
        assertEquals("foo, bar, baz", StringUtil.join(Arrays.asList(stringArray).iterator(), ", "));
    }

    @Test
    public void testJoinArray() {
        assertEquals("foo,bar,baz", StringUtil.join(new String[] {"foo", "bar", "baz"}, ","));
    }

    // Test padding method

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            assertEquals(padding[i], StringUtil.padding(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    // Test isBlank method

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
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\f"));
        assertTrue(StringUtil.isBlank("\r"));
    }

    @Test
    public void testIsBlankNonWhitespace() {
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("123"));
        assertFalse(StringUtil.isBlank("123foo"));
    }

    // Test isNumeric method

    @Test
    public void testIsNumericEmpty() {
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericNull() {
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsNumericDigitsOnly() {
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("0123"));
        assertTrue(StringUtil.isNumeric("1234567890"));
    }

    @Test
    public void testIsNumericNonDigits() {
        assertFalse(StringUtil.isNumeric("123a"));
        assertFalse(StringUtil.isNumeric("123aB"));
        assertFalse(StringUtil.isNumeric("123aBc"));
    }

    // Test isWhitespace method

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
    public void testIsWhitespaceNonWhitespace() {
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('z'));
        assertFalse(StringUtil.isWhitespace('!'));
        assertFalse(StringUtil.isWhitespace('?'));
    }

    // Test isActuallyWhitespace method

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
    public void testIsActuallyWhitespaceNonWhitespace() {
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('z'));
        assertFalse(StringUtil.isActuallyWhitespace('!'));
        assertFalse(StringUtil.isActuallyWhitespace('?'));
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        assertTrue(StringUtil.isActuallyWhitespace((int) 160));
    }

    // Test normaliseWhitespace method

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo bar"));
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo   bar"));
    }

    @Test
    public void testNormaliseWhitespaceLeadingTrailing() {
        assertEquals("foo bar", StringUtil.normaliseWhitespace(" foo bar "));
    }

    @Test
    public void testNormaliseWhitespaceNewLineTab() {
        assertEquals("foo bar\n\t baz", StringUtil.normaliseWhitespace("foo bar\n\t baz"));
    }

    // Test in method

    @Test
    public void testInNull() {
        assertFalse(StringUtil.in(null, "foo", "bar"));
    }

    @Test
    public void testInEmpty() {
        assertFalse(StringUtil.in("", "foo", "bar"));
    }

    @Test
    public void testInSingleElementCollection() {
        assertTrue(StringUtil.in("foo", "foo"));
    }

    @Test
    public void testInSingleMatchString() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
    }

    @Test
    public void testInMultipleMatchesString() {
        assertTrue(StringUtil.in("foo bar", "foo", "bar", "baz"));
    }

    @Test
    public void testInNoMatchString() {
        assertFalse(StringUtil.in("qux", "foo", "bar", "baz"));
    }

    // Test inSorted method

    @Test
    public void testInSortedNull() {
        assertFalse(StringUtil.inSorted(null, new String[] {"foo", "bar"}));
    }

    @Test
    public void testInSortedEmpty() {
        assertFalse(StringUtil.inSorted("", new String[] {"foo", "bar"}));
    }

    @Test
    public void testInSortedSingleMatch() {
        assertTrue(StringUtil.inSorted("foo", new String[] {"foo", "bar"}));
    }

    @Test
    public void testInSortedMultipleMatches() {
        assertTrue(StringUtil.inSorted("bar foo", new String[] {"foo", "bar"}));
    }

    @Test
    public void testInSortedNoMatch() {
        assertFalse(StringUtil.inSorted("qux", new String[] {"foo", "bar"}));
    }

    // Test resolve method

    @Test
    public void testResolveNullBaseUrl() {
        assertNull(StringUtil.resolve(null, ""));
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        assertNull(StringUtil.resolve("", ""));
    }

    @Test
    public void testResolveRelativeUrlOnly() {
        URL url = new URL("http://example.com/");
        assertEquals("http://example.com/", StringUtil.resolve(url, ""));
    }

    @Test
    public void testResolveAbsoluteUrl() {
        URL url = new URL("http://example.com/foo/bar.html");
        assertEquals("http://example.com/foo/bar.html", StringUtil.resolve(url, ""));
    }

    @Test
    public void testResolveRelativeUrlWithTrailingSlash() {
        URL url = new URL("http://example.com/foo/");
        assertEquals("http://example.com/foo/", StringUtil.resolve(url, ""));
    }

    @Test
    public void testResolveRelativeUrl() {
        URL url = new URL("http://example.com/foo/");
        assertEquals("http://example.com/foo/baz.html", StringUtil.resolve(url, "baz.html"));
    }

    @Test
    public void testResolveRelativeUrlWithLeadingSlash() {
        URL url = new URL("http://example.com/foo/");
        assertEquals("http://example.com/baz.html", StringUtil.resolve(url, "/baz.html"));
    }

    @Test
    public void testResolveRelativeUrlWithDotLeading() {
        URL url = new URL("http://example.com/foo/");
        assertEquals("http://example.com/foo/baz.html", StringUtil.resolve(url, ".baz.html"));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlBase() {
        String baseUrl = "example://example.com";
        String relUrl = "";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlRelative() {
        String baseUrl = "http://example.com";
        String relUrl = "//example.com";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlRelativeTrailingSlash() {
        String baseUrl = "http://example.com";
        String relUrl = "/..";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlRelativeNonExistent() {
        String baseUrl = "http://example.com";
        String relUrl = "foo/bar/baz";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test
    public void testResolveMalformedUrlRelativeWithDot() {
        String baseUrl = "http://example.com";
        String relUrl = ".html";
        assertEquals("http://example.com/", StringUtil.resolve(new URL(baseUrl), relUrl));
    }

    // Test stringBuilder method

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertFalse(sb.length() > 0);
    }

    @Test
    public void testStringBuilderAppend() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("foo");
        assertEquals("foo", sb.toString());
    }

    @Test
    public void testStringBuilderAppendedTwice() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("foo");
        sb.append("bar");
        assertEquals("foobar", sb.toString());
    }
}