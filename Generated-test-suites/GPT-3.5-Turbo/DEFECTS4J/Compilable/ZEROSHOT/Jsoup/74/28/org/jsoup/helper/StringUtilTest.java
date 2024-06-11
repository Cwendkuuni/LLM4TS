package org.jsoup.helper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoin_CollectionString() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");

        String joined = StringUtil.join(strings, " ");
        assertEquals("Hello World", joined);
    }

    @Test
    public void testJoin_IteratorString() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");

        Iterator<String> iterator = strings.iterator();
        String joined = StringUtil.join(iterator, " ");
        assertEquals("Hello World", joined);
    }

    @Test
    public void testJoin_StringArrayString() {
        String[] strings = {"Hello", "World"};

        String joined = StringUtil.join(strings, " ");
        assertEquals("Hello World", joined);
    }

    @Test
    public void testPadding() {
        String padded = StringUtil.padding(5);
        assertEquals("     ", padded);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("Hello"));
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
        String normalized = StringUtil.normaliseWhitespace("Hello  World\t!");
        assertEquals("Hello World !", normalized);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "Hello  World\t!", false);
        assertEquals("Hello World !", sb.toString());
    }

    @Test
    public void testIn() {
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in("Hello", haystack));
        assertFalse(StringUtil.in("foo", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.inSorted("Hello", haystack));
        assertFalse(StringUtil.inSorted("foo", haystack));
    }

    @Test
    public void testResolve_URLString() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "../foo";
        URL resolved = StringUtil.resolve(base, relUrl);
        assertEquals("http://www.example.com/foo", resolved.toString());
    }

    @Test
    public void testResolve_StringString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "../foo";
        String resolved = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://www.example.com/foo", resolved);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals("", sb.toString());
    }
}