package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.jsoup.helper.StringUtil.*;

public class StringUtilTest {

    @Test
    public void testJoin_Collection() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        String result = join(strings, ",");
        Assert.assertEquals("Hello, World, !", result);
    }

    @Test
    public void testJoin_Iterator() {
        String[] strings = new String[]{"Hello", "World", "!"};
        Iterator iterator = Arrays.asList(strings).iterator();
        String result = join(iterator, ",");
        Assert.assertEquals("Hello, World, !", result);
    }

    @Test
    public void testJoin_Array() {
        String[] strings = new String[]{"Hello", "World", "!"};
        String result = join(strings, ",");
        Assert.assertEquals("Hello, World, !", result);
    }

    @Test
    public void testPadding_ValidWidth() {
        for (int i = 0; i <= padding.length; i++) {
            String result = padding(i);
            Assert.assertEquals(padding[i], result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_NegativeWidth() {
        padding(-1);
    }

    @Test
    public void testIsBlank_NullString() {
        Assert.assertTrue(isBlank(null));
    }

    @Test
    public void testIsBlank_EmptyString() {
        Assert.assertTrue(isBlank(""));
    }

    @Test
    public void testIsBlank_WhitespaceString() {
        Assert.assertTrue(isBlank("   "));
    }

    @Test
    public void testIsBlank_NonBlankString() {
        Assert.assertFalse(isBlank("Hello"));
    }

    @Test
    public void testIsNumeric_ValidNumericString() {
        Assert.assertTrue(isNumeric("12345"));
        Assert.assertTrue(isNumeric("1234567890"));
    }

    @Test
    public void testIsNumeric_EmptyString() {
        Assert.assertFalse(isNumeric(""));
    }

    @Test
    public void testIsNumeric_NonNumericString() {
        Assert.assertFalse(isNumeric("abc123"));
        Assert.assertFalse(isNumeric("123abc"));
    }

    @Test
    public void testIsWhitespace_Space() {
        Assert.assertTrue(isWhitespace(' '));
    }

    @Test
    public void testIsWhitespace_Tab() {
        Assert.assertTrue(isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespace_Newline() {
        Assert.assertTrue(isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespace_Format() {
        Assert.assertTrue(isWhitespace('\f'));
        Assert.assertTrue(isWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespace_Space() {
        Assert.assertTrue(isActuallyWhitespace(' '));
    }

    @Test
    public void testIsActuallyWhitespace_Tab() {
        Assert.assertTrue(isActuallyWhitespace('\t'));
    }

    @Test
    public void testIsActuallyWhitespace_Newline() {
        Assert.assertTrue(isActuallyWhitespace('\n'));
    }

    @Test
    public void testIsActuallyWhitespace_NonBreakingSpace() {
        Assert.assertTrue(isActuallyWhitespace((int) 160));
    }

    @Test
    public void testNormaliseWhitespace_SingleSpace() {
        String input = "   Hello World!   ";
        String output = normaliseWhitespace(input);
        Assert.assertEquals("Hello World!", output);
    }

    @Test
    public void testNormaliseWhitespace_MultipleSpaces() {
        String input = "   Hello    World!   ";
        String output = normaliseWhitespace(input);
        Assert.assertEquals("Hello World!", output);
    }

    @Test
    public void testNormaliseWhitespace_TabAndNewline() {
        String input = "\tHello\nWorld!\t";
        String output = normaliseWhitespace(input);
        Assert.assertEquals("Hello World!", output);
    }

    @Test
    public void testNormaliseWhitespace_Mixed() {
        String input = "   Hello\nWorld!\t  ";
        String output = normaliseWhitespace(input);
        Assert.assertEquals("Hello World!", output);
    }

    @Test
    public void testIn_StringInArray() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World", "!"};
        boolean result = in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testIn_StringNotInArray() {
        String needle = "World";
        String[] haystack = {"Hello", "!", "World"};
        boolean result = in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInSorted_StringInSortedArray() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World", "!"};
        boolean result = inSorted(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInSorted_StringNotInSortedArray() {
        String needle = "World";
        String[] haystack = {"Hello", "!", "World"};
        boolean result = inSorted(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testResolve_ValidBaseUrlAndRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "/path/to/another.html";
        URL result = resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("http://example.com/path/to/another.html", result.toExternalForm());
    }

    @Test
    public void testResolve_EmptyBaseUrlAndRelUrl() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "/path/to/another.html";
        URL result = resolve(baseUrl, relUrl);
        Assert.assertEquals("", result.toExternalForm());
    }

    @Test
    public void testResolve_NullBaseUrlAndRelUrl() throws MalformedURLException {
        String baseUrl = null;
        String relUrl = "/path/to/another.html";
        URL result = resolve(baseUrl, relUrl);
        Assert.assertEquals("", result.toExternalForm());
    }

    @Test
    public void testResolve_BaseUrlWithTrailingSlashAndRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html/";
        String relUrl = "another.html";
        URL result = resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("http://example.com/path/to/another.html", result.toExternalForm());
    }

    @Test
    public void testResolve_BaseUrlWithoutTrailingSlashAndRelUrlWithLeadingSlash() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "/another.html";
        URL result = resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("http://example.com/path/to/another.html", result.toExternalForm());
    }

    @Test
    public void testResolve_BaseUrlWithoutTrailingSlashAndRelUrlWithoutLeadingSlash() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "another.html";
        URL result = resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("http://example.com/path/to/another.html", result.toExternalForm());
    }

    @Test
    public void testResolve_BaseUrlWithProtocolRelativeRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "//another.com/another.html";
        URL result = resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("http://another.com/another.html", result.toExternalForm());
    }

    @Test
    public void testResolve_InvalidBaseUrlAndRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "//another.com/path/to/another.html";
        URL result = resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("", result.toExternalForm());
    }

}