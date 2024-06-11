package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("hello", "world", "!");
        String result = StringUtil.join(list, " ");
        assertEquals("hello world !", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> list = Arrays.asList("hello", "world", "!");
        String result = StringUtil.join(list.iterator(), " ");
        assertEquals("hello world !", result);
    }

    @Test
    public void testJoinArray() {
        String[] array = {"hello", "world", "!"};
        String result = StringUtil.join(array, " ");
        assertEquals("hello world !", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("123a45"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace(" hello   world  !");
        assertEquals(" hello world !", result);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("hello", "hello", "world", "!"));
        assertFalse(StringUtil.in("foo", "hello", "world", "!"));
    }

    @Test
    public void testInSorted() {
        String[] array = {"hello", "world", "!"};
        assertTrue(StringUtil.inSorted("hello", array));
        assertFalse(StringUtil.inSorted("foo", array));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "/foo";
        URL resultUrl = StringUtil.resolve(base, relUrl);
        assertEquals("http://www.example.com/foo", resultUrl.toString());
    }

    @Test
    public void testResolveWithBaseUrl() {
        String baseUrl = "http://www.example.com";
        String relUrl = "/foo";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://www.example.com/foo", result);
    }


    // Your additional test cases for edge cases and exception handling scenarios go here
}