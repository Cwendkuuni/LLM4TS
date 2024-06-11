package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Test class for StringUtil.
 */
public class StringUtilTest {

    @Test
    public void testJoinEmptyCollection() {
        assertEquals("", StringUtil.join(Collections.emptyList(), ""));
    }

    @Test
    public void testJoinSingleString() {
        assertEquals("foo", StringUtil.join(Arrays.asList("foo"), ""));
    }

    @Test
    public void testJoinMultipleStrings() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo bar baz", StringUtil.join(strings, " "));
    }

    @Test
    public void testJoinMultipleStringsWithSeparator() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo|bar|baz", StringUtil.join(strings, "|"));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("   ", StringUtil.padding(3));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("0"));
        assertTrue(StringUtil.isNumeric("1234567890"));
    }

    @Test
    public void testIsWhitespace() {
        assertFalse(StringUtil.isWhitespace(33));
        assertFalse(StringUtil.isWhitespace(127));
        assertFalse(StringUtil.isWhitespace(160));
        assertTrue(StringUtil.isWhitespace('\u0020'));
        assertTrue(StringUtil.isWhitespace('\u0009'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertTrue(StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertFalse(StringUtil.isActuallyWhitespace(33));
        assertFalse(StringUtil.isActuallyWhitespace(127));
        assertFalse(StringUtil.isActuallyWhitespace(160));
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0')); // no-break space
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(null));
        assertEquals(" ", StringUtil.normaliseWhitespace(" "));
        assertEquals("foo bar baz\nbar baz", StringUtil.normaliseWhitespace("foo bar baz\n bar baz"));
        assertEquals("foo bar baz\n bar baz\n", StringUtil.normaliseWhitespace("foo bar baz\n bar baz\n"));
        assertEquals("foo bar baz\n bar baz\n foo", StringUtil.normaliseWhitespace("foo bar baz\n bar baz\n foo"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("needle", "haystack1", "haystack2"));
        assertFalse(StringUtil.in("needle", "haystack1"));
        assertFalse(StringUtil.in("needle", "haystack2"));
        assertFalse(StringUtil.in("needle", null));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        assertTrue(StringUtil.inSorted("cherry", haystack));
        assertTrue(StringUtil.inSorted("banana", haystack));
        assertTrue(StringUtil.inSorted("apple", haystack));
        assertFalse(StringUtil.inSorted("orange", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl1 = "about";
        String relUrl2 = "contact.html";

        URL result1 = StringUtil.resolve(new URL(baseUrl), relUrl1);
        assertEquals("https://example.com/about", result1.toExternalForm());

        URL result2 = StringUtil.resolve(new URL(baseUrl), relUrl2);
        assertEquals("https://example.com/contact.html", result2.toExternalForm());

        String badRelUrl = "/../";
        try {
            URL result3 = StringUtil.resolve(new URL(baseUrl), badRelUrl);
            fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }
}