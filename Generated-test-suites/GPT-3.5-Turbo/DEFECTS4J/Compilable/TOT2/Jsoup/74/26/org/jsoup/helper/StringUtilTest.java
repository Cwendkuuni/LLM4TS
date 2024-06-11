package org.jsoup.helper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String result = StringUtil.join(strings, "-");
        assertEquals("Hello-World", result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> iterator = Arrays.asList("Test", "String").iterator();
        String result = StringUtil.join(iterator, ",");
        assertEquals("Test,String", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"One", "Two", "Three"};
        String result = StringUtil.join(strings, "/");
        assertEquals("One/Two/Three", result);
    }

    @Test
    public void testPadding() {
        String paddedString = StringUtil.padding(3);
        assertEquals("   ", paddedString);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("abc123"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("  Hello  World  ");
        assertEquals(" Hello World ", normalized);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("two", "one", "two", "three"));
        assertFalse(StringUtil.in("four", "one", "two", "three"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("three", new String[]{"one", "three", "five"}));
        assertFalse(StringUtil.inSorted("two", new String[]{"one", "three", "five"}));
    }

    @Test
    public void testResolveUrl() {
        try {
            URL baseUrl = new URL("http://example.com");
            String relUrl = "/test";
            URL resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
            assertEquals("http://example.com/test", resolvedUrl.toString());
        } catch (MalformedURLException e) {
            fail("MalformedURLException thrown");
        }
    }

    @Test
    public void testResolveStringUrl() {
        String baseUrl = "http://example.com";
        String relUrl = "/test";
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/test", resolvedUrl);
    }
}