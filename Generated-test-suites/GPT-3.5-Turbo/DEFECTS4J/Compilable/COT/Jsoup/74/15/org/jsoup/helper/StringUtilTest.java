package org.jsoup.helper;
import org.jsoup.helper.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoin_Collection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String expected = "Hello-World";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoin_Iterator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        String expected = "Hello-World";
        String result = StringUtil.join(iterator, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoin_Array() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String expected = "Hello-World";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlank_NullString() {
        String string = null;
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlank_EmptyString() {
        String string = "";
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlank_WhitespaceString() {
        String string = "   ";
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlank_NonBlankString() {
        String string = "Hello";
        boolean expected = false;
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumeric_NullString() {
        String string = null;
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumeric_EmptyString() {
        String string = "";
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumeric_NumericString() {
        String string = "12345";
        boolean expected = true;
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumeric_NonNumericString() {
        String string = "Hello";
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespace_WhitespaceChars() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespace_NonWhitespaceChars() {
        int c = 'A';
        boolean expected = false;
        boolean result = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespace_WhitespaceChars() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespace_NonWhitespaceChars() {
        int c = 'A';
        boolean expected = false;
        boolean result = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "Hello   World ";
        String expected = "Hello World";
        String result = StringUtil.normaliseWhitespace(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "Hello   World ";
        boolean stripLeading = false;
        String expected = "Hello World";
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        String result = accum.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn_NotInHaystack() {
        String needle = "Foo";
        String[] haystack = {"Hello", "World"};
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSorted_NotInHaystack() {
        String needle = "Foo";
        String[] haystack = {"Hello", "World"};
        boolean expected = false;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com/");
        String relUrl = "page.html";
        String expected = "http://www.example.com/page.html";
        URL result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testResolve_RelUrlStartsWithQuestionMark() throws MalformedURLException {
        URL base = new URL("http://www.example.com/");
        String relUrl = "?param=value";
        String expected = "http://www.example.com/page.html?param=value"; // where page.html is the base.getPath()
        URL result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testResolve_RelUrlStartsWithDotAndBaseFileNotStartsWithSlash() throws MalformedURLException {
        URL base = new URL("http://www.example.com/foo");
        String relUrl = "./bar";
        String expected = "http://www.example.com/./bar";
        URL result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testResolve_InvalidRelUrl() {
        String baseUrl = "http://www.example.com/";
        String relUrl = "invalid-url";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        Assert.assertEquals("", sb.toString());
    }
}