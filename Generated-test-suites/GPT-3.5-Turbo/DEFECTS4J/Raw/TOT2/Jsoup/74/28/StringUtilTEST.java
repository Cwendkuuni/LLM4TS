package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        assertEquals("Hello-World", StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String sep = "-";
        assertEquals("Hello-World", StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        assertEquals("Hello-World", StringUtil.join(strings, sep));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testPadding() {
        assertEquals("     ", StringUtil.padding(5));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12A45"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertFalse(StringUtil.isWhitespace('A'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello     World"));
    }

    @Test
    public void testIn() {
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in("Hello", haystack));
        assertFalse(StringUtil.in("JSoup", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Apple", "Banana", "Orange"};
        assertTrue(StringUtil.inSorted("Banana", haystack));
        assertFalse(StringUtil.inSorted("JSoup", haystack));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "./products";
        StringUtil.resolve(base, relUrl);
    }

    @Test
    public void testResolveString() {
        assertEquals("https://www.example.com/products", StringUtil.resolve("https://www.example.com", "./products"));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertNotSame(sb1, sb2);
    }
}