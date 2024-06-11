package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ", "));
        assertEquals("foo bar baz", StringUtil.join(strings, " "));
        assertEquals("foo,bar,baz", StringUtil.join(strings.iterator(), ", "));
        assertEquals("foo bar baz", StringUtil.join(strings.iterator(), " "));
        assertNull(StringUtil.join(null, ","));
        assertNull(StringUtil.join(Arrays.asList().iterator(), ""));
        assertNull(StringUtil.join(Arrays.asList().iterator(), null));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(3));
        assertEquals("    ", StringUtil.padding(5));
        assertEquals("      ", StringUtil.padding(7));
        assertEquals("         ", StringUtil.padding(9));
        assertEquals("        ", StringUtil.padding(11));
        assertEquals("       ", StringUtil.padding(12));
        assertEquals("      ", StringUtil.padding(13));
        assertEquals("   ", StringUtil.padding(15));
        assertEquals("  ", StringUtil.padding(16));
        assertEquals(" ", StringUtil.padding(17));
        assertEquals("foo", StringUtil.padding(5) + "foo");
        assertEquals("foo", StringUtil.padding(5, 'X') + "foo");
        assertEquals("foo", StringUtil.padding(5, 'X').substring(0, 5) + "foo");
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo bar"));
        assertFalse(StringUtil.isBlank("foo\tbar"));
        assertFalse(StringUtil.isBlank("foo\nbar"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("123foo"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("012345"));
        assertTrue(StringUtil.isNumeric("1234567890"));
    }

    @Test
    public void testIsWhitespace() {
        assertFalse(StringUtil.isWhitespace(123));
        assertFalse(StringUtil.isWhitespace(123L));
        assertFalse(StringUtil.isWhitespace(123d));
        assertFalse(StringUtil.isWhitespace(123f));
        assertFalse(StringUtil.isWhitespace('A'));
        assertFalse(StringUtil.isWhitespace('B'));
        assertFalse(StringUtil.isWhitespace('\u0001'));
        assertFalse(StringUtil.isWhitespace('\u0002'));
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertTrue(StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertFalse(StringUtil.isActuallyWhitespace(123));
        assertFalse(StringUtil.isActuallyWhitespace(123L));
        assertFalse(StringUtil.isActuallyWhitespace(123d));
        assertFalse(StringUtil.isActuallyWhitespace(123f));
        assertFalse(StringUtil.isActuallyWhitespace('A'));
        assertFalse(StringUtil.isActuallyWhitespace('B'));
        assertFalse(StringUtil.isActuallyWhitespace('\u0001'));
        assertFalse(StringUtil.isActuallyWhitespace('\u0002'));
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("", StringUtil.normaliseWhitespace(null));
        assertEquals("", StringUtil.normaliseWhitespace("\n"));
        assertEquals("", StringUtil.normaliseWhitespace("\r"));
        assertEquals("", StringUtil.normaliseWhitespace("\f"));
        assertEquals("", StringUtil.normaliseWhitespace("\t"));
        assertEquals("", StringUtil.normaliseWhitespace(" "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace(" foo bar baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace(" foo\nbartest baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace(" foo\tbaz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\n"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz  "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\t"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\n\t"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\n\t\r"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\n\t\r\f"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("foo", "foo"));
        assertFalse(StringUtil.in("foo", "bar", "baz"));
        assertFalse(StringUtil.in("foo", null));
        assertFalse(StringUtil.in("foo", new String[0]));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("foo", new String[]{"foo", "bar", "baz"}));
        assertFalse(StringUtil.inSorted("foo", new String[]{"bar", "baz"}));
        assertFalse(StringUtil.inSorted("foo", new String[0]));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://example.com/");
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "/foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "//example.com/foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "/foo.html/"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "./foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "../foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "./../foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "../foo.html/"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html/"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html/"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html\r\n"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html\r\n\n"));
        assertEquals("https://example.com/foo.html", StringUtil.resolve(base, "foo.html\r\n\n\r\f"));
        assertEquals("", StringUtil.resolve(base, ""));
        assertEquals("", StringUtil.resolve(base, null));
        assertNull(StringUtil.resolve(null, ""));
        assertNull(StringUtil.resolve(null, null));
    }

}