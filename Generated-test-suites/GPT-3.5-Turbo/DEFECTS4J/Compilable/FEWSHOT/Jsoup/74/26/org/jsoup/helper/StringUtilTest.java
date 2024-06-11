
package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));
        
        Collection<String> emptyStrings = Arrays.asList();
        assertEquals("", StringUtil.join(emptyStrings, ","));
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        Iterator<String> iterator = strings.iterator();
        assertEquals("foo,bar,baz", StringUtil.join(iterator, ","));
        
        Collection<String> emptyStrings = Arrays.asList();
        Iterator<String> emptyIterator = emptyStrings.iterator();
        assertEquals("", StringUtil.join(emptyIterator, ","));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"foo", "bar", "baz"};
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));
        
        String[] emptyStrings = {};
        assertEquals("", StringUtil.join(emptyStrings, ","));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("       ", StringUtil.padding(7));
        assertEquals("                    ", StringUtil.padding(20));
        
        try {
            StringUtil.padding(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("   "));
        assertFalse(StringUtil.isNumeric("foo"));
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo    bar \t  baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("\n\nfoo \nbar\n \t baz\r\r"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo \u00A0 bar baz"));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "foo    bar \t  baz", false);
        assertEquals("foo bar baz", sb.toString());
        
        StringBuilder sb2 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb2, "foo    bar \t  baz", true);
        assertEquals("foo bar baz", sb2.toString());
    }

    @Test
    public void testIn() {
        String[] haystack = {"foo", "bar", "baz"};
        assertTrue(StringUtil.in("foo", haystack));
        assertTrue(StringUtil.in("bar", haystack));
        assertFalse(StringUtil.in("qux", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"bar", "baz", "foo"};
        assertTrue(StringUtil.inSorted("bar", haystack));
        assertTrue(StringUtil.inSorted("baz", haystack));
        assertFalse(StringUtil.inSorted("qux", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "foo").toString());
        assertEquals("http://example.com/foo?bar", StringUtil.resolve(base, "?bar").toString());
        assertEquals("http://example.com/dir/foo", StringUtil.resolve(base, "./foo").toString());
        assertEquals("http://example.com/foo", StringUtil.resolve("http://example.com/", "foo"));
        
        assertEquals("", StringUtil.resolve("", ""));
    }

    @Test
    public void testResolveFailure() {
        String result = StringUtil.resolve("http://example.com/", ":test");
        assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        sb1.append("foo");
        assertEquals("foo", sb1.toString());
        
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertEquals("", sb2.toString());
    }
}
