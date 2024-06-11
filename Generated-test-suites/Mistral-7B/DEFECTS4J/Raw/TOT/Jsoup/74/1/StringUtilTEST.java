package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!", " ");
        String expected = "Hello World ! ";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
        
        strings = Arrays.asList();
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
        
        strings = Arrays.asList("", "Hello");
        expected = "Hello";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
        
        strings = Arrays.asList(null);
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
        
        strings = Arrays.asList("Hello", null, "World");
        expected = "Hello World";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = {"Hello", "World", "!", " "};
        Iterator<String> it = Arrays.asList(strings).iterator();
        String expected = "Hello World ! ";
        String result = StringUtil.join(it, " ");
        assertEquals(expected, result);

        it = Arrays.asList().iterator();
        expected = "";
        result = StringUtil.join(it, " ");
        assertEquals(expected, result);

        it = Arrays.asList("", "Hello").iterator();
        expected = "Hello";
        result = StringUtil.join(it, " ");
        assertEquals(expected, result);

        it = Arrays.asList(null).iterator();
        expected = "";
        result = StringUtil.join(it, " ");
        assertEquals(expected, result);

        it = Arrays.asList("Hello", null, "World").iterator();
        expected = "Hello World";
        result = StringUtil.join(it, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!", " "};
        String expected = "Hello World ! ";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[1];
        strings[0] = "";
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[1];
        strings[0] = "Hello";
        expected = "Hello";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[1];
        strings[0] = null;
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[2];
        strings[0] = "Hello";
        strings[1] = null;
        expected = "Hello";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= 21; i++) {
            String result = StringUtil.padding(i);
            String expected = "";
            for (int j = 0; j < i; j++) {
                expected += " ";
            }
            assertEquals(expected, result);
        }
        try {
            StringUtil.padding(-1);
            fail("padding should throw IllegalArgumentException for negative width");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = {"", "Hello", " World", "  ", "   ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    "};
        List<String> list = Arrays.asList(strings);
        for (String string : list) {
            boolean result = StringUtil.isBlank(string);
            boolean expected = (string.isEmpty() || string.equals(" "));
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = {"1", "123", "1234", "12345", "123456", "1234567", "12345678", "123456789", "1234567890", "12345678901", "123.45", "123.456", "123.456.7", "123.456.7.8", "123.456.7.8.9", "123.456.7.8.9.0", "123ABC", "123+45", "123-45", "123e45", "123e+45", "123e-45"};
        List<String> list = Arrays.asList(strings);
        for (String string : list) {
            boolean result = StringUtil.isNumeric(string);
            boolean expected = (string.matches("^[-+]?[0-9]+(\\.[0-9]+)$"));
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsWhitespace() {
        int[] codes = {32, 9, 10, 11, 12, 13, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
        List<Integer> list = Arrays.asList(codes);
        for (Integer code : list) {
            boolean result = StringUtil.isWhitespace(code);
            boolean expected = (Arrays.asList(32, 9, 10, 11).contains(code));
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsActuallyWhitespace() {
        int[] codes = {32, 9, 10, 11, 12, 13, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 160};
        List<Integer> list = Arrays.asList(codes);
        for (Integer code : list) {
            boolean result = StringUtil.isActuallyWhitespace(code);
            boolean expected = (Arrays.asList(32, 9, 10, 11, 160).contains(code));
            assertEquals(expected, result);
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] strings = {"    Hello World!   ", "   Hello\tWorld!  ", "Hello\nWorld!", "Hello\rWorld!", "Hello World!\r\n"};
        List<String> list = Arrays.asList(strings);
        for (String string : list) {
            String expected = "Hello World!";
            String result = StringUtil.normaliseWhitespace(string);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIn() {
        String[] strings = {"Hello", "World", "!", " ", "Java", "Testing"};
        String needle = "Java";
        boolean result = StringUtil.in(needle, strings);
        assertTrue(result);

        needle = "Testing";
        result = StringUtil.in(needle, strings);
        assertTrue(result);

        needle = "JavaScript";
        result = StringUtil.in(needle, strings);
        assertFalse(result);
    }

    @Test
    public void testInSorted() {
        String[] strings = {"Java", "Testing", "Hello", "World!", "!", " ", "JavaScript"};
        String needle = "Java";
        boolean result = StringUtil.inSorted(needle, strings);
        assertTrue(result);

        needle = "World!";
        result = StringUtil.inSorted(needle, strings);
        assertTrue(result);

        needle = "Zoo";
        result = StringUtil.inSorted(needle, strings);
        assertFalse(result);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "/about.html";
        String expected = "http://example.com/about.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/index.html";
        relUrl = "../about.html";
        expected = "http://example.com/about.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/index.html";
        relUrl = "./about.html";
        expected = "http://example.com/about.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/index.html";
        relUrl = "/about/index.html";
        expected = "http://example.com/about/index.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/index.html";
        relUrl = "about.html";
        expected = "http://example.com/about.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/index.html";
        relUrl = "//example.net/test.html"; // Note: This is typically not allowed, but we'll accept it for testing
        expected = "http://example.net/test.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/index.html";
        relUrl = "";
        expected = "http://example.com/index.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }
}