package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        String expected = "HelloWorld!";
        String result = StringUtil.join(strings.iterator(), "");
        assertEquals(expected, result);

        strings = Arrays.asList();
        expected = "";
        result = StringUtil.join(strings.iterator(), "");
        assertEquals(expected, result);

        strings = Arrays.asList("", "World");
        expected = "World";
        result = StringUtil.join(strings.iterator(), "");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinWithNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= 21; i++) {
            String result = StringUtil.padding(i);
            assertEquals(StringUtil.padding(i).length(), result.length());
        }

        try {
            StringUtil.padding(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String s1 = "";
        String s2 = null;
        String s3 = " ";
        String s4 = "   ";
        String s5 = "\t";
        String s6 = "\n";
        String s7 = "\r";
        String s8 = "\f";
        String s9 = "   Hello   ";
        String s10 = "\nHello\n";
        String s11 = "Hello\n";

        assertTrue(StringUtil.isBlank(s1));
        assertFalse(StringUtil.isBlank(s3));
        assertFalse(StringUtil.isBlank(s4));
        assertFalse(StringUtil.isBlank(s5));
        assertFalse(StringUtil.isBlank(s6));
        assertFalse(StringUtil.isBlank(s7));
        assertFalse(StringUtil.isBlank(s8));
        assertFalse(StringUtil.isBlank(s9));
        assertFalse(StringUtil.isBlank(s10));
        assertFalse(StringUtil.isBlank(s11));
        assertTrue(StringUtil.isBlank(s2));
    }

    @Test
    public void testIsNumeric() {
        String s1 = "123";
        String s2 = "1A3";
        String s3 = "123abc";
        String s4 = "";
        String s5 = null;

        assertTrue(StringUtil.isNumeric(s1));
        assertFalse(StringUtil.isNumeric(s2));
        assertFalse(StringUtil.isNumeric(s3));
        assertFalse(StringUtil.isNumeric(s4));
        assertFalse(StringUtil.isNumeric(s5));
    }

    @Test
    public void testIsWhitespace() {
        for (int i = 0; i <= 127; i++) {
            boolean result = StringUtil.isWhitespace((char) i);
            if (isWhitespace(i)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }

        assertTrue(StringUtil.isWhitespace('\u00a0')); // non-breaking space
    }

    @Test
    public void testIsActuallyWhitespace() {
        for (int i = 0; i <= 127; i++) {
            boolean result = StringUtil.isActuallyWhitespace((char) i);
            if (isActuallyWhitespace(i)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }

        assertTrue(StringUtil.isActuallyWhitespace('\u00a0')); // non-breaking space
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = " a   b   c\t\r\n d\r\n e\t f\n g ";
        String expected = " a b c f g ";
        String result = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "example";
        String[] haystack = {"apple", "banana", "cherry", "example", "fig"};
        assertTrue(StringUtil.in(needle, haystack));

        needle = "Apple";
        assertFalse(StringUtil.in(needle, haystack));

        needle = null;
        assertFalse(StringUtil.in(needle, haystack));

        needle = "";
        assertFalse(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "example";
        String[] haystack = {"Apple", "banana", "Cherry", "example", "Fig"};
        assertTrue(StringUtil.inSorted(needle, haystack));

        needle = "apple";
        assertFalse(StringUtil.inSorted(needle, haystack));

        needle = null;
        assertFalse(StringUtil.inSorted(needle, haystack));

        needle = "";
        assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithMalformedBaseUrl() {
        String baseUrl = "http://example.com";
        String relUrl = "/index.html";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithMalformedRelUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "//example.com";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/";
        String relUrl = "/about";
        String expected = "http://example.com/about";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithAbsoluteRelUrl() {
        String baseUrl = "http://example.com/";
        String relUrl = "http://example.com/about";
        String expected = relUrl;
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithEmptyBaseUrl() {
        String relUrl = "http://example.com/about";
        String expected = relUrl;
        String result = StringUtil.resolve("", relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithNullBaseUrl() {
        String relUrl = "http://example.com/about";
        String expected = relUrl;
        String result = StringUtil.resolve(null, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithNullRelUrl() {
        String baseUrl = "http://example.com/";
        String expected = baseUrl;
        String result = StringUtil.resolve(baseUrl, null);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        assertEquals(0, sb1.length());
        sb1.append("Hello");
        assertEquals("Hello", sb1.toString());

        StringBuilder sb2 = StringUtil.stringBuilder();
        sb2.append("World");
        assertEquals("World", sb2.toString());

        StringBuilder sb3 = StringUtil.stringBuilder();
        sb3.append("Hello");
        sb3.append("World");
        assertEquals("HelloWorld", sb3.toString());

        StringBuilder sb4 = StringUtil.stringBuilder();
        sb4.append("Hello");
        sb4.append("World");
        sb4.append("!");
        assertEquals("HelloWorld!", sb4.toString());

        StringBuilder sb5 = StringUtil.stringBuilder();
        sb5.append("HelloWorld!".substring(0, 5));
        assertEquals("Hello", sb5.toString());

        StringBuilder sb6 = StringUtil.stringBuilder();
        sb6.append("HelloWorld!".substring(6));
        assertEquals("World!", sb6.toString());

        StringBuilder sb7 = StringUtil.stringBuilder();
        sb7.append("Hello");
        sb7.append("World!");
        assertEquals("HelloWorld!", sb7.toString());
    }

    private boolean isWhitespace(int c) {
        switch (c) {
            case ' ':
            case '\t':
            case '\n':
            case '\f':
            case '\r':
                return true;
            default:
                return false;
        }
    }
}