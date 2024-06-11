package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String expected = "HelloWorld!";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Arrays.asList(null);
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Arrays.asList("Hello", null, "World", "!");
        expected = "HelloWorld!";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();

        String expected = "HelloWorld!";
        String result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);

        iterator = Collections.emptyIterator();
        expected = "";
        result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);

        iterator = Arrays.asList(null).iterator();
        expected = "";
        result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);

        iterator = Arrays.asList("Hello", null, "World", "!").iterator();
        expected = "HelloWorld!";
        result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"Hello", "World", "!"};
        String expected = "HelloWorld!";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[]{null};
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[]{"Hello", null, "World", "!"};
        expected = "HelloWorld!";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(1);
        String expected = " ";
        Assert.assertEquals(expected, result);

        result = StringUtil.padding(-1);
        Assert.assertNull(result);

        result = StringUtil.padding(21);
        expected = "                 ";
        Assert.assertEquals(expected, result);

        result = StringUtil.padding(60);
        expected = "                                                                               ";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlank() {
        String empty = "";
        String nullString = null;
        String whitespace = " ";
        String tab = "\t";
        String newline = "\n";
        String whitespaceOnly = " \t\r\n ";
        String singleWhitespace = " ";

        Assert.assertTrue(StringUtil.isBlank(empty));
        Assert.assertTrue(StringUtil.isBlank(nullString));
        Assert.assertTrue(StringUtil.isBlank(whitespace));
        Assert.assertTrue(StringUtil.isBlank(tab));
        Assert.assertTrue(StringUtil.isBlank(newline));
        Assert.assertTrue(StringUtil.isBlank(whitespaceOnly));
        Assert.assertTrue(StringUtil.isBlank(singleWhitespace));

        String stringWithNonWhitespace = "Non whitespace";
        Assert.assertFalse(StringUtil.isBlank(stringWithNonWhitespace));
    }

    @Test
    public void testIsNumeric() {
        String empty = "";
        String nullString = null;
        String number = "12345";
        String notANumber = "abcd";
        String numberWithLeadingZero = "012345";

        Assert.assertFalse(StringUtil.isNumeric(empty));
        Assert.assertFalse(StringUtil.isNumeric(nullString));
        Assert.assertTrue(StringUtil.isNumeric(number));
        Assert.assertFalse(StringUtil.isNumeric(notANumber));
        Assert.assertTrue(StringUtil.isNumeric(numberWithLeadingZero));
    }

    @Test
    public void testIsWhitespace() {
        char whitespace = ' ';
        char tab = '\t';
        char newline = '\n';

        Assert.assertTrue(StringUtil.isWhitespace(whitespace));
        Assert.assertTrue(StringUtil.isWhitespace(tab));
        Assert.assertTrue(StringUtil.isWhitespace(newline));

        char notWhitespace = 'A';
        Assert.assertFalse(StringUtil.isWhitespace(notWhitespace));
    }

    @Test
    public void testIsActuallyWhitespace() {
        char whitespace = ' ';
        char tab = '\t';
        char newline = '\n';
        char nonBreakingSpace = '\u00A0';

        Assert.assertTrue(StringUtil.isActuallyWhitespace(whitespace));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(tab));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(newline));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));

        char notWhitespace = 'A';
        Assert.assertFalse(StringUtil.isActuallyWhitespace(notWhitespace));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "   Hello World   ";
        String expected = "Hello World";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);

        input = "   \r\n  Hello World  \r\n   ";
        expected = "Hello World";
        result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);

        input = "   \r\n  Hello\tWorld  \r\n   ";
        expected = "Hello World";
        result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);

        input = "   \r\n  Hello\rWorld  \r\n   ";
        expected = "Hello World";
        result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "test";
        String[] haystack = new String[]{"hello", "test", "world"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);

        String[] emptyHaystack = new String[0];
        needle = "test";
        expected = false;
        result = StringUtil.in(needle, emptyHaystack);
        Assert.assertEquals(expected, result);

        needle = "test";
        haystack = new String[]{"hello", "test", "world", "test"};
        expected = true;
        result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "hello";
        haystack = new String[]{"hello", "test", "world"};
        expected = true;
        result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "world";
        haystack = new String[]{"hello", "test", "world"};
        expected = true;
        result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String needle = "test";
        String[] haystack = new String[]{"hello", "test", "world"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "zoo";
        haystack = new String[]{"hello", "test", "world"};
        expected = false;
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "test";
        haystack = new String[]{"test", "test", "test"};
        expected = true;
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);

        haystack = new String[]{"apple", "banana", "cherry"};
        needle = "cherry";
        expected = true;
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "apple";
        expected = false;
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolve() {
        String baseUrl = "https://example.com/main";
        String relUrl = "/sub/page.html";
        String expected = "https://example.com/sub/page.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = "//sub.example.com/page.html";
        expected = "https://sub.example.com/page.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = "/../sub/page.html";
        expected = "https://example.com/sub/page.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = "../sub/page.html";
        expected = "https://example.com/sub/page.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = "page.html";
        expected = "https://example.com/page.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = "/page.html";
        expected = "https://example.com/page.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = "";
        expected = "https://example.com/main";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://example.com/main";
        relUrl = null;
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }
}