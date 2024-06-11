package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollectionString() {
        Collection<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");
        strings.add("!");

        assertEquals("hello, world, !", StringUtil.join(strings, ", "));
        assertEquals("", StringUtil.join(new ArrayList<String>(), ", "));
    }

    @Test
    public void testJoinIteratorString() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        Iterator<String> iterator = strings.iterator();

        assertEquals("hello, world, !", StringUtil.join(iterator, ", "));
    }

    @Test
    public void testJoinStringArrayString() {
        String[] strings = {"hello", "world", "!"};

        assertEquals("hello, world, !", StringUtil.join(strings, ", "));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("    ", StringUtil.padding(4));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("hello"));
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(null));
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("hello world !", StringUtil.normaliseWhitespace("hello   world \n\t!"));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, null, false);
        assertEquals("", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "", false);
        assertEquals("", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "hello   world \n\t!", false);
        assertEquals("hello world !", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "hello   world \n\t!", true);
        assertEquals("hello world !", sb.toString());
    }

    @Test
    public void testIn() {
        assertFalse(StringUtil.in("", ""));
        assertFalse(StringUtil.in("", "a", "b", "c"));
        assertFalse(StringUtil.in("d", "a", "b", "c"));
        assertTrue(StringUtil.in("a", "a", "b", "c"));
        assertTrue(StringUtil.in("b", "a", "b", "c"));
        assertTrue(StringUtil.in("c", "a", "b", "c"));
    }

    @Test
    public void testInSorted() {
        assertFalse(StringUtil.inSorted("", new String[] {}));
        assertFalse(StringUtil.inSorted("", new String[] {"a", "b", "c"}));
        assertFalse(StringUtil.inSorted("d", new String[] {"a", "b", "c"}));
        assertTrue(StringUtil.inSorted("a", new String[] {"a", "b", "c"}));
        assertTrue(StringUtil.inSorted("b", new String[] {"a", "b", "c"}));
        assertTrue(StringUtil.inSorted("c", new String[] {"a", "b", "c"}));
    }

    @Test
    public void testResolveURLString() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "foo").toExternalForm());
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "./foo").toExternalForm());
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "/foo").toExternalForm());
        assertEquals("http://example.com/foo?query", StringUtil.resolve(base, "./foo?query").toExternalForm());
    }

    @Test
    public void testResolveStringString() {
        String base = "http://example.com/";
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "foo"));
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "./foo"));
        assertEquals("http://example.com/foo", StringUtil.resolve(base, "/foo"));
        assertEquals("http://example.com/foo?query", StringUtil.resolve(base, "./foo?query"));
        assertEquals("http://example.com/foo?query", StringUtil.resolve(base, "?query"));
        assertEquals("", StringUtil.resolve(base, ""));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        assertTrue(sb1.length() < StringUtil.MaxCachedBuilderSize);
        assertTrue(sb2.length() < StringUtil.MaxCachedBuilderSize);
    }
}